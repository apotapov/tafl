package com.pactstudios.games.tafl.core.es.components.render;

import com.artemis.Component;

public class DirectionComponent implements Component {

    public float angle;
    public float maxTurnAngle;
    public int flipHorizontal = 1;
    public int flipVertical = 1;

    @Override
    public void reset() {
        angle = 0;
        maxTurnAngle = 0;
        flipHorizontal = 1;
        flipVertical = 1;
    }

}
