package com.captstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.math.Vector2;

public class PieceDragEvent extends SystemEvent {

    public Vector2 touchPoint = new Vector2();

    @Override
    protected void resetForPooling() {
        touchPoint.x = 0;
        touchPoint.y = 0;
    }
}
