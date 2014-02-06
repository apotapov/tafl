package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.CompleteEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.PieceCountEvaluator;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class AiFactory {

    public static AiStrategy getAiStrategy(AiType type, TaflMatch match, TaflDatabaseService dbService) {
        switch (type) {
        case AI_RANDOM:
            return new RandomMoveStrategy();
        case AI_MINIMAX_PIECE_COUNT:
            return new MiniMaxStrategy(
                    new PieceCountEvaluator(),
                    Constants.AiConstants.MAX_TREE_DEPTH);
        case AI_ALPHA_BETA:
            return new AlphaBetaMoveStrategy(
                    dbService.transpositionTable,
                    dbService.historyTable,
                    new CompleteEvaluator(match.board.boardSize),
                    match.rulesEngine);
        case AI_MTDF:
            return new MtdfMoveStrategy(
                    dbService.transpositionTable,
                    dbService.historyTable,
                    new CompleteEvaluator(match.board.boardSize),
                    match.rulesEngine,
                    dbService.openings);
        default:
            return null;
        }
    }

}
