package com.pactstudios.games.tafl.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;

public class TaflGameHtml extends GwtApplication {
    @Override
    public ApplicationListener getApplicationListener () {
        return new TaflGame();
    }

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(
                Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT);
    }
}
