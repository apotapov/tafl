package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LoadGameScreen extends AbstractScreen<TaflGame> {

    public LoadGameScreen(final TaflGame game) {
        super(game);
    }

    @Override
    public void initialize() {
        stage.addActor(new Image(game.graphicsService.getTexture(Assets.Graphics.SPLASH_IMAGE)));
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
