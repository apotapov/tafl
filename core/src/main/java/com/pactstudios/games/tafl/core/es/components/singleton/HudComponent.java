package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class HudComponent implements Component {
    public int score;
    public float gameTime;
    public int gameSpeed = 1;

    public Vector2 mouseLocation;
    public int fps;

    public HudComponent() {
        mouseLocation = new Vector2();
    }

    @Override
    public void reset() {
        score = 0;
        gameTime = 0;
        gameSpeed = 1;
        mouseLocation.set(0, 0);
        fps = 0;
    }

}
