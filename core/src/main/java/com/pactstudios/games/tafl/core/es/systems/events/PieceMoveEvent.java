package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class PieceMoveEvent extends SystemEvent {
    public Move move;

    @Override
    protected void resetForPooling() {
        Move.movePool.free(move);
        move = null;
    }
}
