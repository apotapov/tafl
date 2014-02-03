package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class PieceMoveEvent extends SystemEvent {
    public TaflMove move = new TaflMove();

    @Override
    protected void resetForPooling() {
        move.reset();
    }
}
