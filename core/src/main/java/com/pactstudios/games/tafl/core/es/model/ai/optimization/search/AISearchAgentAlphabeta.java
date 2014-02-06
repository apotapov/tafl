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
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;

public class AISearchAgentAlphabeta<T extends Move<?>, U extends GameBoard<T>> extends AISearchAgent<T, U> {

    int depth;

    // Construction
    public AISearchAgentAlphabeta(TranspositionTable transpositionTable,
            HistoryTable<T> historyTable,
            BoardEvaluator<U> evaluator,
            RulesChecker<T, U> rulesChecker,
            int depth) {
        super(transpositionTable, historyTable, evaluator, rulesChecker);
        this.depth = depth;
    }

    // jcMove PickBestMove
    // Implementation of the abstract method defined in the superclass
    @SuppressWarnings("unchecked")
    @Override
    public T pickBestMove(U board, int turn) {
        // Store the identity of the moving side, so that we can tell Evaluator
        // from whose perspective we need to evaluate positions
        fromWhosePerspective = turn;
        moveCounter++;

        // Should we erase the history table?
        if ((random.nextInt() % 4) == 2) {
            historyTable.forget();
        }

        numRegularNodes = 0;
        numEvaluationNodes = 0;
        numRegularTTHits = 0;
        numEvaluationTTHits = 0;

        // Find the moves
        Array<T> legalMoves = getLegalMoves(board, turn);
        historyTable.sortMoveList(legalMoves, turn);

        // The following code blocks look a lot like the MAX node case from
        // jcAISearchAgent.Alphabeta, with an added twist: we need to store the
        // actual best move, and not only pass around its minimax value
        int bestSoFar = ALPHABETA_MINVAL;
        T bestMove = null;
        int currentAlpha = ALPHABETA_MINVAL;

        // Loop on all pseudo-legal moves
        for (T move : legalMoves) {
            board.simulateMove(move);
            int movScore = min(board, turn, depth - 1, currentAlpha,
                    ALPHABETA_MAXVAL);
            if (movScore != ALPHABETA_ILLEGAL) {

                currentAlpha = Math.max(currentAlpha, movScore);

                if (movScore > bestSoFar) {
                    bestMove = (T) move.clone();
                    bestSoFar = movScore;
                }
            }
            board.undoSimulatedMove();
        }

        System.out.println("  --> Number of nodes: " + numRegularNodes);
        System.out.print("  --> Transposition Table hits for regular nodes: ");
        System.out.println(numRegularTTHits + " of " + numRegularNodes);
        System.out.println("  --> Number of cutoffs for regular nodes: "
                + numRegularCutoffs);

        return bestMove;
    }
}