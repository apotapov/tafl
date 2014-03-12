package com.captstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class PieceMoveEvent extends SystemEvent {
    public Move move;

    @Override
    protected void resetForPooling() {
        move.free();
        move = null;
    }
}
