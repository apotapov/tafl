package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.ai.AiStrategy.AiType;

public class AiFactory {

    public static AiStrategy getAiStrategy(AiType type) {
        switch (type) {
        case RANDOM:
            return new RandomMoveStrategy();
        default:
            return null;
        }
    }

}
