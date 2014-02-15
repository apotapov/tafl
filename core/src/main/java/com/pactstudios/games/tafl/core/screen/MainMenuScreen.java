package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class MainMenuScreen extends AbstractScreen<TaflGame> {

    Cell<?> loadButton;

    public MainMenuScreen(final TaflGame game) {
        super(game);
    }

    @Override
    public void show() {
        if (!game.preferenceService.hasSavedMatch()) {
            loadButton.size(0, 0);
        } else {
            loadButton.size(Constants.ScreenConstants.BUTTON_WIDTH, Constants.ScreenConstants.BUTTON_HEIGHT)
            .uniform()
            .spaceBottom(10);
        }
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
        Label label = new Label(labelText, skin, Assets.Skin.IN_GAME_STYLE_NAME);
        table.add(label);
        table.row();

        String buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_START);
        Button button = game.createSwitchScreenButton(buttonText, game.levelSelectionScreen);
        table.add(button).uniform();
        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_RESUME);
        button = game.createSwitchScreenButton(buttonText, game.loadGameScreen);
        loadButton = table.add(button).uniform();
        table.row();

        buttonText = game.localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_OPTIONS);
        button = game.createSwitchScreenButton(buttonText, game.optionsScreen);
        table.add(button).uniform();

        stage.addActor(table);
    }
}
