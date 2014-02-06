package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class RandomMoveStrategy implements AiStrategy {

    @Override
    public TaflMove search(TaflMatch match) {
        return match.rulesEngine.allLegalMoves(match.turn).random();
    }
}
