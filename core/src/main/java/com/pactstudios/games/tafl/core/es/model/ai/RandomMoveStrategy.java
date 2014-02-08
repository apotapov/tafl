package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class RandomMoveStrategy implements AiStrategy {

    @Override
    public Move search(TaflMatch match) throws InterruptedException {
        return match.board.rules.allLegalMoves(match.turn).random();
    }
}
