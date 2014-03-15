package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
                new Image(atlas.createSprite(Assets.Background.SPLASH)),
                Constants.ScreenConstants.COMPANY_SCREEN_DISPLAY_TIME,
                Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void initialize() {
        super.initialize();
        Table table = new Table();
        table.setFillParent(true);

        Texture cornerTexture = new Texture(Gdx.files.internal(Assets.GraphicFiles.CORNER));

        Image corner = new Image(cornerTexture);
        corner.setOrigin(corner.getWidth() / 2, corner.getHeight() / 2);
        corner.setRotation(180);
        corner.setScale(0.5f);
        table.add(corner).expand().left().top();
        corner = new Image(cornerTexture);
        corner.setOrigin(corner.getWidth() / 2, corner.getHeight() / 2);
        corner.setRotation(90);
        corner.setScale(0.5f);
        table.add(corner).expand().right().top();

        table.row();

        corner = new Image(cornerTexture);
        corner.setOrigin(corner.getWidth() / 2, corner.getHeight() / 2);
        corner.setRotation(270);
        corner.setScale(0.5f);
        table.add(corner).expand().left().bottom();
        corner = new Image(cornerTexture);
        corner.setOrigin(corner.getWidth() / 2, corner.getHeight() / 2);
        corner.setScale(0.5f);
        table.add(corner).expand().right().bottom();

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }

        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.deviceSettings.initialize(width, height);
    }

}
