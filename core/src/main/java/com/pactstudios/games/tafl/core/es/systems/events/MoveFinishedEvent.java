package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class MoveFinishedEvent extends SystemEvent {
    public TaflMove move;

    @Override
    protected void resetForPooling() {
        move = null;
    }
}
