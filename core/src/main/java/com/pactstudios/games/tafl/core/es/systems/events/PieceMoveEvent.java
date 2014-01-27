package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.es.model.board.Move;

public class PieceMoveEvent extends SystemEvent {
    public Move move = new Move();

    @Override
    protected void resetForPooling() {
        move.reset();
    }
}
