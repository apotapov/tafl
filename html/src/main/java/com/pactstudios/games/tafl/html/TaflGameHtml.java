package com.pactstudios.games.tafl.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.pactstudios.games.tafl.core.TaflGame;

public class TaflGameHtml extends GwtApplication {
    @Override
    public ApplicationListener getApplicationListener () {
        return new TaflGame();
    }

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(480, 320);
    }
}
