package com.pactstudios.games.tafl.core.es.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 velocity;
    public float maxAngleChange;

    public VelocityComponent() {
        velocity = new Vector2();
    }

    @Override
    public void reset() {
        velocity.set(0, 0);
        maxAngleChange = 0;
    }

    @Override
    public String toString() {
        return "Velocity" + velocity;
    }

}
