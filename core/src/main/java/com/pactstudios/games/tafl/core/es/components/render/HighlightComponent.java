package com.pactstudios.games.tafl.core.es.components.render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;

public class HighlightComponent implements Component {

    public ModelCell cell;
    public Color color;

    public HighlightComponent() {
    }

    @Override
    public void reset() {
        cell = null;
        color = null;
    }
}
