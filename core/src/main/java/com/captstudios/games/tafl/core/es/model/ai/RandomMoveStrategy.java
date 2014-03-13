package com.captstudios.games.tafl.core.es.model.ai;

import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class RandomMoveStrategy implements AiStrategy {

    @Override
    public Move search(TaflMatch match) {
        return match.board.rules.allLegalMoves(match.turn).random();
    }
}
