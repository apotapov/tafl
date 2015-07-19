package com.captstudios.games.tafl.core.es.components.render;

import com.artemis.Component;

public class OffsetComponent implements Component {

    public float xOffset;
    public float yOffset;

    @Override
    public void reset() {
        xOffset = 0;
        yOffset = 0;
    }

}
