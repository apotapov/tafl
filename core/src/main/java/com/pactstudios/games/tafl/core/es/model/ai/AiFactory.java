package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.CompleteEvaluator;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class AiFactory {

    public static AiStrategy getAiStrategy(AiType type, TaflMatch match, TaflDatabaseService dbService) {
        switch (type) {
        case AI_BEGINNER:
            return new RandomMoveStrategy();
        case AI_INTERMEDIATE:
            return new AlphaBetaMoveStrategy(
                    dbService.transpositionTable,
                    dbService.historyTable,
                    new CompleteEvaluator(match.board.boardSize),
                    match.rulesEngine,
                    Constants.AiConstants.INT_TREE_DEPTH);
        case AI_ADVANCED:
            return new AlphaBetaMoveStrategy(
                    dbService.transpositionTable,
                    dbService.historyTable,
                    new CompleteEvaluator(match.board.boardSize),
                    match.rulesEngine,
                    Constants.AiConstants.ADV_TREE_DEPTH);
        default:
            return null;
        }
    }

}
