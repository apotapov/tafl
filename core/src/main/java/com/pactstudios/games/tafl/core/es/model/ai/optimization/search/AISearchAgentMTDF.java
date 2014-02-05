/***************************************************************************
 * jcAISearchAgentMTDF - A sophisticated search agent
 *
 * Purpose: A (mostly) state-of-the-art search agent, implementing advanced
 * techniques like the iterative-deepening MTDF search algorithm, transposition
 * table, opening book and history table.
 *
 * History:
 * 05.10.00 Completed initial version
 **************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.optimization.search;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.enums.EvaluationType;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.OpeningBook;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;


public class AISearchAgentMTDF<T extends Move<?>, U extends GameBoard<T>> extends AISearchAgent<T, U> {
    // A reference to the game's opening book
    OpeningBook<T> openings;
    int depth;

    // A measure of the effort we are willing to expend on search
    private static final int MAX_SEARCH_SIZE = 50000;

    public AISearchAgentMTDF(TranspositionTable transpositionTable,
            HistoryTable<T> historyTable,
            BoardEvaluator<U> evaluator,
            RulesChecker<T, U> rulesChecker,
            OpeningBook<T> openings,
            int depth) {
        super(transpositionTable, historyTable, evaluator, rulesChecker);
        this.openings = openings;
        this.depth = depth;
    }

    // Move selection: An iterative-deepening paradigm calling MTD(f) repeatedly
    @Override
    public T pickBestMove(U board, int turn) {
        // First things first: look in the Opening Book, and if it contains a
        // move for this position, don't search anything
        moveCounter++;
        T move = openings.query(board.hashCode(), board.hashLock(), turn);
        if (move != null) {
            return move;
        }

        // Store the identity of the moving side, so that we can tell Evaluator
        // from whose perspective we need to evaluate positions
        fromWhosePerspective = turn;

        // Should we erase the history table?
        if ((random.nextInt() % 6) == 2) {
            historyTable.forget();
        }

        // Begin search. The search's maximum depth is determined on the fly,
        // according to how much effort has been spent; if it's possible to
        // search
        // to depth 8 in 5 seconds, then by all means, do it!
        int bestGuess = 0;
        int iterdepth = 1;

        while (true) {
            // Searching to depth 1 is not very effective, so we begin at 2
            iterdepth++;

            // Compute efficiency statistics
            numRegularNodes = 0;
            numEvaluationNodes = 0;
            numRegularTTHits = 0;
            numEvaluationTTHits = 0;
            numRegularCutoffs = 0;
            numEvaluationCutoffs = 0;

            // Look for a move at the current depth
            move = mtdf(board, turn, bestGuess, iterdepth);
            bestGuess = move.eval;

            // Get out if we have searched deep enough
            if ((numRegularNodes + numEvaluationNodes) > MAX_SEARCH_SIZE) {
                break;
            }
            if (iterdepth >= 15) {
                break;
            }
        }

        return move;
    }

    /***************************************************************************
     * PRIVATE METHODS
     **************************************************************************/

    // private jcMove MTDF
    // Use the MTDF algorithm to find a good move. MTDF repeatedly calls
    // alphabeta with a zero-width search window, which creates very many quick
    // cutoffs. If alphabeta fails low, the next call will place the search
    // window lower; in a sense, MTDF is a sort of binary search mechanism into
    // the minimax space.
    private T mtdf(U board, int turn, int target, int depth) {
        int beta;
        T move;
        int currentEstimate = target;
        int upperbound = ALPHABETA_MAXVAL;
        int lowerbound = ALPHABETA_MINVAL;

        // This is the trick: make repeated calls to alphabeta, zeroing in on
        // the
        // actual minimax value of theBoard by narrowing the bounds
        do {
            if (currentEstimate == lowerbound) {
                beta = currentEstimate + 1;
            } else {
                beta = currentEstimate;
            }

            move = unrolledAlphabeta(board, turn, depth, beta - 1, beta);
            currentEstimate = move.eval;

            if (currentEstimate < beta) {
                upperbound = currentEstimate;
            } else {
                lowerbound = currentEstimate;
            }

        } while (lowerbound < upperbound);

        return move;
    }

    // private jcMove UnrolledAlphabeta
    // The standard alphabeta, with the top level "unrolled" so that it can
    // return a jcMove structure instead of a mere minimax value
    // See jcAISearchAgent.Alphabeta for detailed comments on this code
    @SuppressWarnings("unchecked")
    private T unrolledAlphabeta(U board, int turn, int depth, int alpha,
            int beta) {

        Array<T> legalMoves = rulesChecker.generateLegalMoves(board, turn);
        historyTable.sortMoveList(legalMoves, turn);

        int bestSoFar = ALPHABETA_MINVAL;
        T bestMove = null;
        int currentAlpha = alpha;

        // Loop on the successors
        for (T move : legalMoves) {
            // Compute a board position resulting from the current successor
            board.simulateMove(move);

            // And search it in turn
            int movScore = alphaBeta(MINNODE, board, turn, depth - 1,
                    currentAlpha, beta);

            // Ignore illegal moves in the alphabeta evaluation
            if (movScore == ALPHABETA_ILLEGAL) {

                currentAlpha = Math.max(currentAlpha, movScore);

                // Is the current successor better than the previous best?
                if (movScore > bestSoFar) {
                    bestMove = (T) move.clone();
                    bestSoFar = movScore;

                    // Can we cutoff now?
                    if (bestSoFar >= beta) {
                        transTable.storeBoard(board.hashCode(), board.hashLock(), bestSoFar,
                                EvaluationType.UPPERBOUND, depth, moveCounter);

                        // Add this move's efficiency in the HistoryTable
                        historyTable.addCount(move, turn);
                        board.undoSimulatedMove();
                        return bestMove;
                    }
                }
            }
            board.undoSimulatedMove();
        }

        // If we haven't returned yet, we have found an accurate minimax score
        // for a position which is neither a checkmate nor a stalemate
        transTable.storeBoard(board.hashCode(), board.hashLock(), bestSoFar, EvaluationType.ACCURATE,
                depth, moveCounter);

        return bestMove;
    }
}