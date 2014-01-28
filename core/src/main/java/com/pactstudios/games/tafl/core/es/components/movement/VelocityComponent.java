package com.pactstudios.games.tafl.core.es.components.movement;

import com.artemis.Component;
import com.pactstudios.games.tafl.core.es.model.board.Move;

public class VelocityComponent implements Component {
    public Move move;
    public float distanceRemaining;

    @Override
    public void reset() {
        move = null;
        distanceRemaining = 0;
    }
}
