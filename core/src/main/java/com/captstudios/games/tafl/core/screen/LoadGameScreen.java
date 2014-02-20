package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LoadGameScreen extends AbstractScreen<TaflGame> {

    public LoadGameScreen(final TaflGame game) {
        super(game);
    }

    @Override
    public void initialize() {
        stage.addActor(new Image(game.graphicsService.getSprite(Assets.Graphics.SPLASH_ATLAS, Assets.Graphics.SPLASH)));
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
