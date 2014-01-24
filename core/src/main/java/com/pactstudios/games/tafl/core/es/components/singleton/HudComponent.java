package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class HudComponent implements Component {
    public TaflMatch match;

    public Vector2 mouseLocation;
    public int fps;

    public HudComponent() {
        this.mouseLocation = new Vector2();
    }

    @Override
    public void reset() {
        match = null;
        mouseLocation.set(0, 0);
        fps = 0;
    }

}
