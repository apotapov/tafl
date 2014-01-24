package com.pactstudios.games.tafl.core.screen;

import com.pactstudios.games.tafl.core.TaflGame;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LoadGameScreen extends AbstractScreen<TaflGame> {

    public LoadGameScreen(final TaflGame game) {
        super(game);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void show() {
        if (game.gamePlayScreen.loadExistingMatch()) {
            game.setScreen(game.gamePlayScreen);
        } else {
            game.setScreen(game.levelSelectionScreen);
        }
    }
}
