package com.captstudios.games.tafl.core.es.components.render;

import com.artemis.Component;

public class AiProcessingComponent implements Component {

    public float timeElapsed;
    public int index;

    @Override
    public void reset() {
        timeElapsed = 0;
        index = 0;
    }
}
