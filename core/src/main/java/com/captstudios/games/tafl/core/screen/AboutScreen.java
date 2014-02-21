package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.utils.ChangeScreenGestureListener;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class AboutScreen extends AbstractScreen<TaflGame> {

    public AbstractScreen<TaflGame> returnScreen;

    public GestureDetector gestureDetector;

    public AboutScreen(final TaflGame game, AbstractScreen<TaflGame> returnScreen) {
        super(game);
        this.returnScreen = returnScreen;
    }

    @Override
    public void initialize() {
        Image background = new Image(game.graphicsService.getSprite(
                Assets.Graphics.SPLASH_ATLAS, Assets.Graphics.SPLASH));
        background.setFillParent(true);
        background.setColor(Constants.ScreenConstants.ABOUT_COLOR);
        stage.addActor(background);

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        String text = game.localeService.get(LocalizedStrings.OptionsMenu.ABOUT);
        Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);
        table.add(label).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        for (LocalizedStrings.About item : LocalizedStrings.About.values()) {
            text = game.localeService.get(item);
            label = new Label(text, skin, Assets.Skin.SKIN_STYLE_MENU);
            table.add(label).spaceBottom(game.deviceType.menuSpacing);
            table.row();
        }

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);

        gestureDetector = new GestureDetector(new ChangeScreenGestureListener(this));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gestureDetector);
    }
}
