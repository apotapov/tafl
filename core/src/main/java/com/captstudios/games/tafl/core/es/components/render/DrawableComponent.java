package com.captstudios.games.tafl.core.es.components.render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class DrawableComponent implements Component {
    public Sprite sprite;
    public String name;

    @Override
    public void reset() {
        name = null;
        sprite = null;
    }

    @Override
    public String toString() {
        return "Sprite[name=" + name + "]";
    }
}
