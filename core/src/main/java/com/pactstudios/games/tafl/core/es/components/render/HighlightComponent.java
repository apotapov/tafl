package com.pactstudios.games.tafl.core.es.components.render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class HighlightComponent implements Component {

    public int cellId;
    public Color color;

    public HighlightComponent() {
    }

    @Override
    public void reset() {
        cellId = 0;
        color = null;
    }
}
