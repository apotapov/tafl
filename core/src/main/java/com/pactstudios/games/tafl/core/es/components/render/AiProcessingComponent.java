package com.pactstudios.games.tafl.core.es.components.render;

import com.artemis.Component;

public class AiProcessingComponent implements Component {

    public String text;

    @Override
    public void reset() {
        text = null;
    }
}
