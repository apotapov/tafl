package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.screen.LoadingScreen;
import com.roundtriangles.games.zaria.services.utils.GameAssetLoader;

public class TaflLoadingScreen extends LoadingScreen<TaflGame> {

    public TaflLoadingScreen(TaflGame game,
            TextureAtlas atlas,
            GameAssetLoader assetLoader,
            AbstractScreen<TaflGame> nextScreen) {
        super(game,
                assetLoader,
                nextScreen,
                new Image(atlas.createSprite(Assets.Graphics.SPLASH)),
                Constants.ScreenConstants.COMPANY_SCREEN_DISPLAY_TIME,
                Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.deviceSettings.initialize(width, height);
    }

}
