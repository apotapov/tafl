package com.captstudios.games.tafl.core.es.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class VelocityComponent implements Component {
    public Move move;
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
