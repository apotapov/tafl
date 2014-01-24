package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
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
        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        String labelText = game.localeService.get(LocalizedStrings.MainMenu.GAME_TITLE);
        Label label = new Label(labelText, skin, Assets.Skin.MENU_STYLE_NAME);
        table.add(label).spaceBottom(20);
        table.row();

        String buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_START);
        Button button = game.createSwitchScreenButton(buttonText, game.levelSelectionScreen);
        table.add(button).size(Constants.ScreenConstants.BUTTON_WIDTH, Constants.ScreenConstants.BUTTON_HEIGHT).uniform().spaceBottom(10);
        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_RESUME);
        button = game.createSwitchScreenButton(buttonText, game.loadGameScreen);
        table.add(button).size(Constants.ScreenConstants.BUTTON_WIDTH, Constants.ScreenConstants.BUTTON_HEIGHT).uniform().spaceBottom(10);
        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_OPTIONS);
        button = game.createSwitchScreenButton(buttonText, game.optionsScreen);
        table.add(button).size(Constants.ScreenConstants.BUTTON_WIDTH, Constants.ScreenConstants.BUTTON_HEIGHT).uniform().spaceBottom(10);
        table.row();

        stage.addActor(table);
    }
}
