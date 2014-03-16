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
                Constants.ScreenConstants.SPLASH_SCREEN_DISPLAY_TIME,
                Constants.ScreenConstants.FADE_TIME);

    }

    @Override
    public void initialize() {
        Table topTable = new Table();
        topTable.top().left().setFillParent(true);
        topTable.defaults().pad(Gdx.graphics.getWidth() / 20).size(Gdx.graphics.getWidth() / 5);


        Texture cornerTexture = new Texture(Gdx.files.internal(Assets.GraphicFiles.CORNER));

        Image corner = new Image(cornerTexture);
        corner.setOrigin(Gdx.graphics.getWidth() / 10, Gdx.graphics.getWidth() / 10);
        corner.setRotation(180);
        corner.setPosition(corner.getWidth(), corner.getHeight());
        topTable.add(corner).expandX().left();

        corner = new Image(cornerTexture);
        corner.setOrigin(Gdx.graphics.getWidth() / 10, Gdx.graphics.getWidth() / 10);
        corner.setRotation(90);
        topTable.add(corner);

        Table bottomTable = new Table();
        bottomTable.bottom().setFillParent(true);
        bottomTable.defaults().pad(Gdx.graphics.getWidth() / 20).size(Gdx.graphics.getWidth() / 5);

        corner = new Image(cornerTexture);
        corner.setOrigin(Gdx.graphics.getWidth() / 10, Gdx.graphics.getWidth() / 10);
        corner.setRotation(270);
        bottomTable.add(corner).expandX().left();

        corner = new Image(cornerTexture);
        bottomTable.add(corner);

        if (Constants.GameConstants.DEBUG) {
            topTable.debug();
            bottomTable.debug();
        }

        stage.addActor(topTable);
        stage.addActor(bottomTable);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.deviceSettings.initialize(width, height);
    }

}
