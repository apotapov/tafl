package com.pactstudios.games.tafl.core.es.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 position;

    public PositionComponent() {
        position = new Vector2();
    }

    @Override
    public void reset() {
        position.set(0, 0);
    }

    @Override
    public String toString() {
        return "Position[position=" + position + "]";
    }
}
