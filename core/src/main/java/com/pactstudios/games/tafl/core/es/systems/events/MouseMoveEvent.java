package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;

public class MouseMoveEvent extends SystemEvent {
    public int x;
    public int y;

    @Override
    protected void resetForPooling() {
        x = 0;
        y = 0;
    }
}
