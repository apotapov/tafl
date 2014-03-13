package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    public GestureDetector gestureDetector;

    public AboutScreen(final TaflGame game, AbstractScreen<TaflGame> parentScreen) {
        super(game, parentScreen, Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void initialize() {
        Sprite background = game.graphicsService.getSprite(
                game.deviceSettings.backgroundAtlas, Assets.Graphics.MENU);
        setBackgroundImage(new Image(background));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        createInfo(skin, table);
        createCredit(skin, table);
        createRules(skin, table);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);

        gestureDetector = new GestureDetector(new ChangeScreenGestureListener(this));
    }

    private void createInfo(Skin skin, Table table) {
        String text = game.localeService.get(LocalizedStrings.MainMenu.GAME_TITLE);
        Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
        table.row();

        text = game.localeService.get(LocalizedStrings.AboutInfo.ABOUT_VERSION);
        label = new Label(text, skin, Assets.Skin.SKIN_STYLE_RULES);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
        table.row();

        text = game.localeService.get(LocalizedStrings.AboutInfo.ABOUT_COPYRIGHT);
        label = new Label(text, skin, Assets.Skin.SKIN_STYLE_RULES);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
        table.row();

        text = game.localeService.get(LocalizedStrings.AboutInfo.ABOUT_RIGHTS_RESERVED);
        label = new Label(text, skin, Assets.Skin.SKIN_STYLE_RULES);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();
    }

    private void createCredit(Skin skin, Table table) {
        int i = 0;
        LocalizedStrings.AboutCredit[] credit = LocalizedStrings.AboutCredit.values();
        for (LocalizedStrings.AboutCredit item : credit) {
            String text = game.localeService.get(item);
            if (i++ % 2 == 0) {
                Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_GAME);
                table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
            } else {
                Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_MENU);
                if (i < credit.length - 1) {
                    table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 4);
                } else {
                    table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
                }

            }
            table.row();
        }
    }

    private void createRules(Skin skin, Table table) {
        int i = 0;
        LocalizedStrings.AboutRules[] rules = LocalizedStrings.AboutRules.values();
        for (LocalizedStrings.AboutRules item : rules) {
            String text = game.localeService.get(item);
            if (i++ % 2 == 0) {
                Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_GAME);
                table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
            } else {
                Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_RULES);
                if (i < rules.length - 1) {
                    table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 2);
                } else {
                    table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
                }
            }
            table.row();
        }
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(gestureDetector);
        backgroundImage.setColor(Constants.ScreenConstants.ABOUT_COLOR);
    }
}
