package com.pactstudios.games.tafl.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.utils.TaflGameConfig;

public class TaflGameHtml extends GwtApplication {
    @Override
    public ApplicationListener getApplicationListener () {
        return new TaflGame(getGameConfig());
    }

    private TaflGameConfig getGameConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(
                Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT);
    }
}
