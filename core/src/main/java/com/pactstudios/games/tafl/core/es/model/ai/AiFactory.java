package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.PieceCountEvaluator;

public class AiFactory {

    public static AiStrategy getAiStrategy(AiType type) {
        switch (type) {
        case AI_RANDOM:
            return new RandomMoveStrategy();
        case AI_MINIMAX_PIECE_COUNT:
            return new MiniMaxStrategy(
                    new PieceCountEvaluator(),
                    Constants.AiConstants.MAX_TREE_DEPTH);
        default:
            return null;
        }
    }

}
