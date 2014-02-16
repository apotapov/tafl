package com.pactstudios.games.tafl.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.DeviceType;
import com.pactstudios.games.tafl.core.utils.TaflGameConfig;

public class TaflGameDesktop {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = Constants.GameConstants.GAME_DESKTOP_WIDTH;
        config.height = Constants.GameConstants.GAME_DESKTOP_HEIGHT;

        config.useGL20 = Constants.GameConstants.USE_GL20;

        TaflGameConfig gameConfig = new TaflGameConfig();
        gameConfig.deviceType = DeviceType.DESKTOP;

        new LwjglApplication(new TaflGame(gameConfig), config);
    }
}
