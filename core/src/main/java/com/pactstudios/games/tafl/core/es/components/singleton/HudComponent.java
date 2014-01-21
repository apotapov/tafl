package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.pactstudios.games.tafl.core.utils.log.GameLog;

public class HudComponent implements Component {
    public float gameTime;
    public GameLog log;
    public Team turn;

    public Vector2 mouseLocation;
    public int fps;

    public HudComponent() {
        this.mouseLocation = new Vector2();
    }

    @Override
    public void reset() {
        gameTime = 0;
        log = null;
        turn = null;
        mouseLocation.set(0, 0);
        fps = 0;
    }

}
