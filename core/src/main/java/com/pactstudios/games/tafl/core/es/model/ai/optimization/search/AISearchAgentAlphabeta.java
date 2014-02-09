/***************************************************************************
 * jcAISearchAgentAlphabeta - The most basic search agent
 *
 * Purpose: Pick a best move using a simple, fixed-depth, full-width
 * alphabeta search, like they did in the Dark Ages ;-)
 *
 * History
 * 07.08.00 Initial writing
 **************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.optimization.search;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;

public class AISearchAgentAlphabeta<U extends GameBoard> extends AISearchAgent<U> {

    // Construction
    public AISearchAgentAlphabeta(TranspositionTable transpositionTable,
            HistoryTable historyTable,
            BoardEvaluator<U> evaluator,
            RulesChecker rulesChecker,
            int depth) {
        super(transpositionTable, historyTable, evaluator, rulesChecker, depth);
    }

    // Implementation of the abstract method defined in the superclass
    @Override
    public Move pickBestMove(U board, int turn) throws InterruptedException {
        // Store the identity of the moving side, so that we can tell Evaluator
        // from whose perspective we need to evaluate positions
        fromWhosePerspective = turn;

        // Should we erase the history table?
        if ((random.nextInt() % 4) == 2) {
            historyTable.forget();
        }

        numRegularNodes = 0;
        numEvaluationNodes = 0;
        numRegularTTHits = 0;
        numEvaluationTTHits = 0;

        Move bestMove = null;

        for (int i = 0; i < depth; i++) {
            depthCounters.get(i).reset();
        }

        long start = System.nanoTime();
        try {

            // Find the moves
            Array<Move> legalMoves = getLegalMoves(board, turn);
            historyTable.sortMoveList(legalMoves, turn);

            // The following code blocks look a lot like the MAX node case from
            // jcAISearchAgent.Alphabeta, with an added twist: we need to store the
            // actual best move, and not only pass around its minimax value
            int bestSoFar = ALPHABETA_MINVAL;
            int currentAlpha = ALPHABETA_MINVAL;

            // Loop on all pseudo-legal moves
            for (Move move : legalMoves) {
                board.simulateMove(move);
                int movScore = min(board, (turn + 1) % 2, depth - 1, currentAlpha,
                        ALPHABETA_MAXVAL);

                currentAlpha = Math.max(currentAlpha, movScore);

                if (movScore > bestSoFar) {
                    bestMove = move.clone();
                    bestMove.eval = movScore;
                    bestSoFar = movScore;
                }
                board.undoSimulatedMove(move);
            }

            clearLegalMoves(legalMoves);

        } finally {
            depthCounters.get(depth-1).put((System.nanoTime() - start) / NANOS_IN_SECOND);
            if (Constants.GameConstants.DEBUG) {
                for (int i = 0; i < depth; i++) {
                    System.out.println("  --> Performance at depth " + i + ": " + depthCounters.get(i));
                }
                System.out.println("  --> Number of nodes: " + numRegularNodes);
                System.out.print("  --> Transposition Table hits for regular nodes: ");
                System.out.println(numRegularTTHits + " of " + numRegularNodes);
                System.out.println("  --> Number of cutoffs for regular nodes: "
                        + numRegularCutoffs);
                if (bestMove != null) {
                    System.out.println("  --> Best move: " + bestMove + " value: " + bestMove.eval);
                }
            }
        }
        return bestMove;
    }
}