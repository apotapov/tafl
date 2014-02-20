package com.captstudios.games.tafl.core.screen;

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
        super(game);
    }

    @Override
    public void show() {
        super.show();
        game.soundService.playMusic(Assets.Sounds.MENU_MUSIC);
    }

    @Override
    public void initialize() {

        Image background = new Image(game.graphicsService.getSprite(
                Assets.Graphics.MENU_ATLAS, Assets.Graphics.MENU));
        background.setFillParent(true);
        stage.addActor(background);

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        String labelText = game.localeService.get(LocalizedStrings.MainMenu.GAME_TITLE);
        Label label = new Label(labelText, skin, game.deviceType.menuStyle);
        table.add(label).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        String buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_START);
        Button button = game.createSwitchScreenButton(buttonText, game.levelSelectionScreen);
        table.add(button).size(game.deviceType.menuButtonWidth,
                game.deviceType.menuButtonHeight).spaceBottom(game.deviceType.menuSpacing).uniform();

        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_RESUME);
        button = game.createSwitchScreenButton(buttonText, game.loadGameScreen);
        table.add(button).size(game.deviceType.menuButtonWidth,
                game.deviceType.menuButtonHeight).spaceBottom(game.deviceType.menuSpacing).uniform();
        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_OPTIONS);
        button = game.createSwitchScreenButton(buttonText, game.optionsScreen);
        table.add(button).size(game.deviceType.menuButtonWidth,
                game.deviceType.menuButtonHeight).uniform();

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }
}
