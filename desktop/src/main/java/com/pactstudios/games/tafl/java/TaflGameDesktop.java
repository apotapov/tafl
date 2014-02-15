package com.pactstudios.games.tafl.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;

public class TaflGameDesktop {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 900;
        config.width = (config.height * Constants.GameConstants.GAME_WIDTH) / Constants.GameConstants.GAME_HEIGHT;

        config.useGL20 = true;
        new LwjglApplication(new TaflGame(), config);
    }
}
