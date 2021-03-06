package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.screen.LoadingScreen;

public class TaflCompanyScreen extends LoadingScreen<TaflGame> {

    public TaflCompanyScreen(TaflGame game,
            TextureAtlas atlas,
            AbstractScreen<TaflGame> nextScreen) {
        super(game,
                null,
                nextScreen,
                new Image(atlas.createSprite(Assets.Backgrounds.COMPANY_LOGO)),
                Constants.ScreenConstants.SPLASH_SCREEN_DISPLAY_TIME,
                Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void initialize() {
    }
}
