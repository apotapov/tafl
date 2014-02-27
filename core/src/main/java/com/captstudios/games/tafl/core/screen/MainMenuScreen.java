package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class MainMenuScreen extends AbstractScreen<TaflGame> {

    public MainMenuScreen(final TaflGame game) {
        super(game, Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void show() {
        super.show();
        game.soundService.playMusic(Assets.Sounds.MENU_MUSIC);
    }

    @Override
    public void initialize() {
        Sprite background = game.graphicsService.getSprite(
                game.deviceSettings.backgroundAtlas, Assets.Graphics.MENU);
        setBackgroundImage(new Image(background));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        String labelText = game.localeService.get(LocalizedStrings.MainMenu.GAME_TITLE);
        Label label = new Label(labelText, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        String buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_START);
        Button button = game.createSwitchScreenButton(buttonText, game.levelSelectionScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);

        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_RESUME);
        button = game.createSwitchScreenButton(buttonText, game.loadGameScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_INSTRUCTIONS);
        button = game.createSwitchScreenButton(buttonText, game.instructionScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_OPTIONS);
        button = game.createSwitchScreenButton(buttonText, game.optionsScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }
}
