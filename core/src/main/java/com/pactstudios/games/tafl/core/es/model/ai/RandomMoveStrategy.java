package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class RandomMoveStrategy implements AiStrategy {

    @Override
    public TaflMove search(TaflMatch match) {
        return match.rulesEngine.legalMoves().random();
    }
}
