package com.captstudios.games.tafl.core.es.model.ai;

import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.AiType;
import com.captstudios.games.tafl.core.es.model.TaflBoard;
import com.captstudios.games.tafl.core.es.model.ai.evaluators.CompleteEvaluator;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.captstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;

public class AiFactory {

    public static AiStrategy getAiStrategy(AiType type, TaflBoard board) {

        TranspositionTable transpositionTable =
                new TranspositionTable(Constants.AiConstants.TRANSPOSITION_TABLE_SIZE);
        HistoryTable historyTable = new HistoryTable(board.boardSize);

        switch (type) {
        case AI_BEGINNER:
            return new AlphaBetaMoveStrategy(
                    transpositionTable,
                    historyTable,
                    new CompleteEvaluator(board),
                    board.rules,
                    board.boardType.beginnerDepth);
        case AI_INTERMEDIATE:
            return new AlphaBetaMoveStrategy(
                    transpositionTable,
                    historyTable,
                    new CompleteEvaluator(board),
                    board.rules,
                    board.boardType.intermediateDepth);
        case AI_ADVANCED:
            return new AlphaBetaMoveStrategy(
                    transpositionTable,
                    historyTable,
                    new CompleteEvaluator(board),
                    board.rules,
                    board.boardType.advancedDepth);
        default:
            return null;
        }
    }

}
