package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
                Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.BOARD);
        setBackgroundImage(new Image(background));

        Table table = new Table();
        table.setFillParent(true);

        LabelStyle titleStyle = new LabelStyle();
        titleStyle.font = game.graphicsService.getFont(game.deviceSettings.rulesTitleFont);
        titleStyle.fontColor = Constants.ScreenConstants.TEXT_COLOR;
        LabelStyle style = new LabelStyle();
        style.font = game.graphicsService.getFont(game.deviceSettings.rulesFont);
        style.fontColor = Constants.ScreenConstants.TEXT_COLOR;

        createInfo(table, titleStyle, style);
        createCredit(table, titleStyle, style);
        createRules(table, titleStyle, style);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }

        stage.addActor(table);

        gestureDetector = new GestureDetector(new ChangeScreenGestureListener(this));
    }

    private void createInfo(Table table, LabelStyle titleStyle, LabelStyle style) {
        String text = game.localeService.get(LocalizedStrings.MainMenu.GAME_TITLE);
        Label label = new Label(text, titleStyle);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
        table.row();

        text = game.localeService.get(LocalizedStrings.AboutInfo.ABOUT_VERSION);
        label = new Label(text, style);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
        table.row();

        text = game.localeService.get(LocalizedStrings.AboutInfo.ABOUT_COPYRIGHT);
        label = new Label(text, style);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
        table.row();

        text = game.localeService.get(LocalizedStrings.AboutInfo.ABOUT_RIGHTS_RESERVED);
        label = new Label(text, style);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();
    }

    private void createCredit(Table table, LabelStyle titleStyle, LabelStyle style) {
        int i = 0;
        LocalizedStrings.AboutCredit[] credit = LocalizedStrings.AboutCredit.values();
        for (LocalizedStrings.AboutCredit item : credit) {
            String text = game.localeService.get(item);
            if (i++ % 2 == 0) {
                Label label = new Label(text, titleStyle);
                table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
            } else {
                Label label = new Label(text, style);
                if (i < credit.length - 1) {
                    table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 4);
                } else {
                    table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
                }

            }
            table.row();
        }
    }

    private void createRules(Table table, LabelStyle titleStyle, LabelStyle style) {
        int i = 0;
        LocalizedStrings.AboutRules[] rules = LocalizedStrings.AboutRules.values();
        for (LocalizedStrings.AboutRules item : rules) {
            String text = game.localeService.get(item);
            if (i++ % 2 == 0) {
                Label label = new Label(text, titleStyle);
                table.add(label).spaceBottom(game.deviceSettings.menuSpacing / 8);
            } else {
                Label label = new Label(text, style);
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
