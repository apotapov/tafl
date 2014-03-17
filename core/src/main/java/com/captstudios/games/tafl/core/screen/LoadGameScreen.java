package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LoadGameScreen extends AbstractScreen<TaflGame> {

    public LoadGameScreen(final TaflGame game) {
        super(game, game.mainMenuScreen, 0);
    }

    @Override
    public void initialize() {
        Sprite background = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Backgrounds.MENU);
        setBackgroundImage(new Image(background));
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
