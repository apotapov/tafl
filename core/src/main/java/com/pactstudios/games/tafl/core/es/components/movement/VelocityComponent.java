package com.pactstudios.games.tafl.core.es.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class VelocityComponent implements Component {
    public TaflMove move;
    public Vector2 velocity = new Vector2();
    public float distanceRemaining;

    @Override
    public void reset() {
        move = null;
        velocity.x = 0;
        velocity.y = 0;
        distanceRemaining = 0;
    }
}
