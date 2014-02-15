/**************************************************************************
 * jcAISearchAgent - An object which picks a best move according to a
 *                   variant of alphabeta search or another
 *
 * Purpose:
 * This is the object which picks a move for the computer player.  Implemented
 * as an abstract class to allow multiple search strategies to be played with.
 *
 * History
 * 07.08.00 Creation
 * 05.10.00 Added statistics and some corrections
 *************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.optimization.search;

import java.util.Random;

import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.enums.EvaluationType;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTableEntry;

public abstract class AISearchAgent<U extends GameBoard> {
    /***************************************************************************
     * DATA MEMBERS
     **************************************************************************/

    RulesChecker rulesChecker;

    // A transposition table for this object
    TranspositionTable transTable;

    // A handle to the system's history table
    HistoryTable historyTable;

    // How will we assess position strengths?
    protected BoardEvaluator<U> evaluator;
    protected int fromWhosePerspective;

    // Alphabeta search boundaries
    protected static final int ALPHABETA_MAXVAL = 30000;
    protected static final int ALPHABETA_MINVAL = -30000;

    protected static final float NANOS_IN_SECOND = 1000000000.0f;

    Random random;

    // Statistics
    protected int numRegularNodes;
    protected int numEvaluationNodes;
    protected int numRegularTTHits;
    protected int numEvaluationTTHits;
    protected int numRegularCutoffs;
    protected int numEvaluationCutoffs;

    protected Array<FloatCounter> depthCounters;
    protected int depth;

    Pool<Array<Move>> arrayPool;

    // Construction
    public AISearchAgent(
            TranspositionTable transpositionTable,
            HistoryTable historyTable,
            BoardEvaluator<U> evaluator,
            RulesChecker rulesChecker,
            int depth) {
        this.transTable = transpositionTable;
        this.historyTable = historyTable;
        this.evaluator = evaluator;
        this.rulesChecker = rulesChecker;
        this.depth = depth;
        this.random = new Random();

        depthCounters = new Array<FloatCounter>();
        for (int i = 0; i < depth; i++) {
            depthCounters.add(new FloatCounter(0){
                @Override
                public String toString() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{count = " + count);
                    sb.append(", total = " + total);
                    sb.append(", min = " + min);
                    sb.append(", max = " + max);
                    sb.append(", average = " + average);
                    sb.append(", value = " + value);
                    sb.append("}");
                    return sb.toString();
                }
            });
        }

        this.arrayPool = new Pool<Array<Move>>(){
            @Override
            protected Array<Move> newObject() {
                return new Array<Move>();
            }

            @Override
            public void free (Array<Move> object) {
                object.size = 0;
                super.free(object);
            }
        };
    }

    /**
     * Pick a function which the agent will use to assess the potency of a
     * position. This may change during the game; for example, a special
     * "mop-up" evaluator may replace the standard when it comes time to drive
     * a decisive advantage home at the end of the game.
     * 
     * @param eval
     */
    public void setEvaluator(BoardEvaluator<U> eval) {
        evaluator = eval;
    }


    /**
     * The basic alpha-beta algorithm, used in one disguise or another by
     * every search agent class.
     * 
     * @param nodeType
     * @param board
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    public int max(U board, int turn, int depth, int alpha, int beta) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        // Count the number of nodes visited in the full-width search
        numRegularNodes++;

        TranspositionTableEntry entry = transTable.lookupBoard(board.hashCode());

        // First things first: let's see if there is already something useful
        // in the transposition table, which might save us from having to search
        // anything at all
        if (entry != null &&
                (entry.evalType == EvaluationType.ACCURATE ||
                entry.evalType == EvaluationType.LOWERBOUND) &&
                entry.eval >= beta) {
            numRegularTTHits++;
            return entry.eval;
        } else if (depth == 0) {
            return evaluate(board);
        }

        Array<Move> legalMoves = getLegalMoves(board, turn);

        if (legalMoves.size == 0) {
            return evaluate(board);
        }

        // Sort the moves according to History heuristic values
        historyTable.sortMoveList(legalMoves, turn);

        // OK, now, get ready to search

        // Case #1: We are searching a Max Node
        int bestSoFar = doMax(board, turn, depth, alpha, beta, legalMoves);

        transTable.storeBoard(board.hashCode(), bestSoFar, EvaluationType.ACCURATE);

        clearLegalMoves(legalMoves);

        return bestSoFar;
    }

    public int min(U board, int turn, int depth, int alpha, int beta) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        // Count the number of nodes visited in the full-width search
        numRegularNodes++;

        TranspositionTableEntry entry = transTable.lookupBoard(board.hashCode());

        // First things first: let's see if there is already something useful
        // in the transposition table, which might save us from having to search
        // anything at all
        if (entry != null &&
                (entry.evalType == EvaluationType.ACCURATE ||
                entry.evalType == EvaluationType.UPPERBOUND) &&
                entry.eval <= alpha) {
            numRegularTTHits++;
            return entry.eval;
        } else if (depth == 0) {
            return evaluate(board);
        }

        Array<Move> legalMoves = getLegalMoves(board, turn);

        if (legalMoves.size == 0) {
            return evaluate(board);
        }

        // Sort the moves according to History heuristic values
        historyTable.sortMoveList(legalMoves, turn);

        // OK, now, get ready to search

        // Case #1: We are searching a Max Node
        int bestSoFar = doMin(board, turn, depth, alpha, beta, legalMoves);

        transTable.storeBoard(board.hashCode(), bestSoFar, EvaluationType.ACCURATE);

        clearLegalMoves(legalMoves);

        return bestSoFar;
    }

    private int evaluate(U board) {
        int val = evaluator.evaluate(board, fromWhosePerspective);
        transTable.storeBoard(board.hashCode(), val, EvaluationType.ACCURATE);
        return val;
    }

    private int doMin(U board, int turn, int depth, int alpha,
            int beta, Array<Move> legalMoves) throws InterruptedException {

        int bestSoFar = ALPHABETA_MAXVAL;
        int currentBeta = beta;

        for (Move move : legalMoves) {
            board.simulateMove(move);
            try {
                // And search it in turn
                int movScore = 0;
                long start = System.nanoTime();
                try {
                    movScore = max(board, (turn + 1) % 2, depth - 1, alpha, currentBeta);
                } finally {
                    depthCounters.get(depth-1).put((System.nanoTime() - start) / NANOS_IN_SECOND);
                }

                currentBeta = Math.min(currentBeta, movScore);
                if (movScore < bestSoFar) {
                    bestSoFar = movScore;
                    // Cutoff?
                    if (bestSoFar <= alpha) {
                        transTable.storeBoard(board.hashCode(), bestSoFar, EvaluationType.LOWERBOUND);
                        historyTable.addCount(move, turn);
                        numRegularCutoffs++;
                        return bestSoFar;
                    }
                }
            } finally {
                board.undoSimulatedMove(move);
            }
        }
        return bestSoFar;
    }

    private int doMax(U board, int turn, int depth, int alpha,
            int beta, Array<Move> legalMoves) throws InterruptedException {

        int bestSoFar = ALPHABETA_MINVAL;
        int currentAlpha = alpha;

        for (Move move : legalMoves) {
            board.simulateMove(move);
            try {
                // And search it in turn
                int movScore = 0;
                long start = System.nanoTime();
                try {
                    movScore = min(board, (turn + 1) % 2, depth - 1, currentAlpha, beta);
                } finally {
                    depthCounters.get(depth-1).put((System.nanoTime() - start) / NANOS_IN_SECOND);
                }

                currentAlpha = Math.max(currentAlpha, movScore);

                // Is the current successor better than the previous best?
                if (movScore > bestSoFar) {
                    bestSoFar = movScore;
                    // Can we cutoff now?
                    if (bestSoFar >= beta) {
                        // Store this best move in the TransTable
                        transTable.storeBoard(board.hashCode(), bestSoFar, EvaluationType.UPPERBOUND);

                        // Add this move's efficiency in the HistoryTable
                        historyTable.addCount(move, turn);
                        numRegularCutoffs++;
                        return bestSoFar;
                    }
                }
            } finally {
                board.undoSimulatedMove(move);
            }
        }

        return bestSoFar;
    }

    protected Array<Move> getLegalMoves(U board, int turn) {
        Array<Move> generatedMoves = rulesChecker.generateLegalMoves(turn);
        Array<Move> legalMoves = arrayPool.obtain();
        for (Move move : generatedMoves) {
            legalMoves.add(move.clone());
        }
        return legalMoves;
    }

    protected void clearLegalMoves(Array<Move> moves) {
        rulesChecker.freeMoves(moves);
        arrayPool.free(moves);
    }


    /**
     * Each agent class needs some way of picking a move!
     * @param theBoard
     * @return
     */
    public abstract Move pickBestMove(U board, int turn) throws InterruptedException;
}