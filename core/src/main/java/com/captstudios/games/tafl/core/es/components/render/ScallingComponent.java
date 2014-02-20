package com.captstudios.games.tafl.core.es.components.render;

import com.artemis.Component;

public class ScallingComponent implements Component {

    public float xScale = 1;
    public float yScale = 1;

    @Override
    public void reset() {
        xScale = 1;
        yScale = 1;
    }

}
