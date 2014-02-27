package com.captstudios.games.tafl.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.utils.device.DeviceType;
import com.captstudios.games.tafl.core.utils.device.TaflGameConfig;

public class TaflGameHtml extends GwtApplication {
    @Override
    public ApplicationListener getApplicationListener () {

        TaflGameConfig gameConfig = new TaflGameConfig();
        gameConfig.deviceType = DeviceType.HTML;
        gameConfig.vibrationSupported = false;

        return new TaflGame(null);
    }

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(
                Constants.GameConstants.GAME_HTML_WIDTH, Constants.GameConstants.GAME_HTML_HEIGHT);
    }
}
