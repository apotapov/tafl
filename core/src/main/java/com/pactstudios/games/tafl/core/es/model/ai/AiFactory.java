package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.CompleteEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;

public class AiFactory {

    public static AiStrategy getAiStrategy(AiType type, TaflBoard board) {

        TranspositionTable transpositionTable =
                new TranspositionTable(Constants.AiConstants.TRANSPOSITION_TABLE_SIZE);
        HistoryTable historyTable = new HistoryTable(board.boardSize);

        switch (type) {
        case AI_BEGINNER:
            return new RandomMoveStrategy();
        case AI_INTERMEDIATE:
            return new AlphaBetaMoveStrategy(
                    transpositionTable,
                    historyTable,
                    new CompleteEvaluator(board.boardSize, board.dimensions),
                    board.rules,
                    Constants.AiConstants.INT_TREE_DEPTH);
        case AI_ADVANCED:
            return new AlphaBetaMoveStrategy(
                    transpositionTable,
                    historyTable,
                    new CompleteEvaluator(board.boardSize, board.dimensions),
                    board.rules,
                    Constants.AiConstants.ADV_TREE_DEPTH);
        default:
            return null;
        }
    }

}
