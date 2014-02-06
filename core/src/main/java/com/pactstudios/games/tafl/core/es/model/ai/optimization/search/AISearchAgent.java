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

public abstract class AISearchAgent<T extends Move<?>, U extends GameBoard<T>> {
    /***************************************************************************
     * DATA MEMBERS
     **************************************************************************/

    RulesChecker<T, U> rulesChecker;

    // A transposition table for this object
    TranspositionTable transTable;

    // A handle to the system's history table
    HistoryTable<T> historyTable;

    // How will we assess position strengths?
    protected BoardEvaluator<U> evaluator;
    protected int fromWhosePerspective;

    // Alphabeta search boundaries
    protected static final int ALPHABETA_MAXVAL = 30000;
    protected static final int ALPHABETA_MINVAL = -30000;
    protected static final int ALPHABETA_ILLEGAL = -31000;


    Random random;

    // Statistics
    int numRegularNodes;
    int numEvaluationNodes;
    int numRegularTTHits;
    int numEvaluationTTHits;
    int numRegularCutoffs;
    int numEvaluationCutoffs;

    // A move counter, so that the agent knows when it can delete old stuff from
    // its transposition table
    int moveCounter;

    Pool<Array<T>> arrayPool;


    // Construction
    public AISearchAgent(
            TranspositionTable transpositionTable,
            HistoryTable<T> historyTable,
            BoardEvaluator<U> evaluator,
            RulesChecker<T, U> rulesChecker) {
        this.transTable = transpositionTable;
        this.historyTable = historyTable;
        this.evaluator = evaluator;
        this.rulesChecker = rulesChecker;
        this.random = new Random();

        this.arrayPool = new Pool<Array<T>>(){
            @Override
            protected Array<T> newObject() {
                return new Array<T>();
            }

            @Override
            public void free (Array<T> object) {
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
    public int max(U board, int turn, int depth, int alpha, int beta) {
        // Count the number of nodes visited in the full-width search
        numRegularNodes++;

        TranspositionTableEntry entry = transTable.lookupBoard(board.hashCode(), board.hashLock());

        // First things first: let's see if there is already something useful
        // in the transposition table, which might save us from having to search
        // anything at all
        if (entry != null && (entry.depth >= depth)) {
            if ((entry.evalType == EvaluationType.ACCURATE)
                    || (entry.evalType == EvaluationType.LOWERBOUND)) {
                if (entry.eval >= beta) {
                    numRegularTTHits++;
                    return entry.eval;
                }
            }
        }

        // If we have reached the maximum depth of the search, stop recursion
        // and begin quiescence search
        if (depth == 0) {
            return evaluator.evaluate(board, fromWhosePerspective);
        }

        // Otherwise, generate successors and search them in turn
        // If ComputeLegalMoves returns false, then the current position is
        // illegal
        // because one or more moves could capture a king!
        // In order to slant the computer's strategy in favor of quick mates, we
        // give a bonus to king captures which occur at shallow depths, i.e.,
        // the
        // more plies left, the better. On the other hand, if you are losing, it
        // really doesn't matter how fast...
        if (rulesChecker.isGameOver(turn)) {
            return ALPHABETA_ILLEGAL;
        }

        Array<T> legalMoves = getLegalMoves(board, turn);

        // Sort the moves according to History heuristic values
        historyTable.sortMoveList(legalMoves, turn);

        // OK, now, get ready to search

        // Case #1: We are searching a Max Node
        int bestSoFar = doMax(board, turn, depth, alpha, beta, legalMoves);

        transTable.storeBoard(board.hashCode(), board.hashLock(), bestSoFar, EvaluationType.ACCURATE,
                depth, moveCounter);
        arrayPool.free(legalMoves);

        return bestSoFar;
    }

    public int min(U board, int turn, int depth, int alpha, int beta) {
        // Count the number of nodes visited in the full-width search
        numRegularNodes++;

        TranspositionTableEntry entry = transTable.lookupBoard(board.hashCode(), board.hashLock());

        // First things first: let's see if there is already something useful
        // in the transposition table, which might save us from having to search
        // anything at all
        if (entry != null && (entry.depth >= depth)) {
            if ((entry.evalType == EvaluationType.ACCURATE)
                    || (entry.evalType == EvaluationType.UPPERBOUND)) {
                if (entry.eval <= alpha) {
                    numRegularTTHits++;
                    return entry.eval;
                }
            }
        }

        // If we have reached the maximum depth of the search, stop recursion
        // and begin quiescence search
        if (depth == 0) {
            return evaluator.evaluate(board, fromWhosePerspective);
        }

        // Otherwise, generate successors and search them in turn
        // If ComputeLegalMoves returns false, then the current position is
        // illegal
        // because one or more moves could capture a king!
        // In order to slant the computer's strategy in favor of quick mates, we
        // give a bonus to king captures which occur at shallow depths, i.e.,
        // the
        // more plies left, the better. On the other hand, if you are losing, it
        // really doesn't matter how fast...
        if (rulesChecker.isGameOver(turn)) {
            return ALPHABETA_ILLEGAL;
        }

        Array<T> legalMoves = getLegalMoves(board, turn);

        // Sort the moves according to History heuristic values
        historyTable.sortMoveList(legalMoves, turn);

        // OK, now, get ready to search

        // Case #1: We are searching a Max Node
        int bestSoFar = doMin(board, turn, depth, alpha, beta, legalMoves);

        transTable.storeBoard(board.hashCode(), board.hashLock(), bestSoFar, EvaluationType.ACCURATE,
                depth, moveCounter);
        arrayPool.free(legalMoves);

        return bestSoFar;
    }


    private int doMin(U board, int turn, int depth, int alpha,
            int beta, Array<T> legalMoves) {
        // Case #2: Min Node
        int bestSoFar = ALPHABETA_MAXVAL;
        int currentBeta = beta;

        for (T move : legalMoves) {
            board.simulateMove(move);

            int movScore = max(board, turn, depth - 1, alpha, currentBeta);
            if (movScore != ALPHABETA_ILLEGAL) {

                currentBeta = Math.min(currentBeta, movScore);
                if (movScore < bestSoFar) {
                    bestSoFar = movScore;
                    // Cutoff?
                    if (bestSoFar <= alpha) {
                        transTable.storeBoard(board.hashCode(), board.hashLock(), bestSoFar,
                                EvaluationType.UPPERBOUND, depth, moveCounter);
                        historyTable.addCount(move, turn);
                        numRegularCutoffs++;
                        board.undoSimulatedMove();
                        return bestSoFar;
                    }
                }
            }
            board.undoSimulatedMove();
        }
        return bestSoFar;
    }

    private int doMax(U board, int turn, int depth, int alpha,
            int beta, Array<T> legalMoves) {
        int bestSoFar = ALPHABETA_MINVAL;
        int currentAlpha = alpha;

        // Loop on the successors
        for (T move : legalMoves) {
            // Compute a board position resulting from the current successor
            board.simulateMove(move);

            // And search it in turn
            int movScore = min(board, turn, depth - 1, currentAlpha, beta);
            // Ignore illegal moves in the alphabeta evaluation
            if (movScore != ALPHABETA_ILLEGAL) {


                currentAlpha = Math.max(currentAlpha, movScore);

                // Is the current successor better than the previous best?
                if (movScore > bestSoFar) {
                    bestSoFar = movScore;
                    // Can we cutoff now?
                    if (bestSoFar >= beta) {
                        // Store this best move in the TransTable
                        transTable.storeBoard(board.hashCode(), board.hashLock(), bestSoFar,
                                EvaluationType.UPPERBOUND, depth, moveCounter);

                        // Add this move's efficiency in the HistoryTable
                        historyTable.addCount(move, turn);
                        numRegularCutoffs++;
                        board.undoSimulatedMove();
                        return bestSoFar;
                    }
                }
                board.undoSimulatedMove();
            }
        }

        return bestSoFar;
    }

    @SuppressWarnings("unchecked")
    protected Array<T> getLegalMoves(U board, int turn) {
        Array<T> generatedMoves = rulesChecker.allLegalMoves(turn);
        Array<T> legalMoves = arrayPool.obtain();
        for (T move : generatedMoves) {
            legalMoves.add((T) move.clone());
        }
        return legalMoves;
    }

    /**
     * Each agent class needs some way of picking a move!
     * @param theBoard
     * @return
     */
    public abstract T pickBestMove(U board, int turn);
}