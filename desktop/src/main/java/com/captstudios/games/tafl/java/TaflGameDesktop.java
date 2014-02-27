package com.captstudios.games.tafl.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.utils.device.DeviceType;
import com.captstudios.games.tafl.core.utils.device.TaflGameConfig;

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
