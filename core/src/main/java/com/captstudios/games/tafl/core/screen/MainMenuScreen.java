package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
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
                game.deviceSettings.backgroundAtlas, Assets.Graphics.MAIN_MENU);
        setBackgroundImage(new Image(background));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);

        Sprite up = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_START);
        Sprite down = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_START_PRESSED);
        Button button = game.createSwitchScreenButton(up, down, this, game.levelSelectionScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);

        table.row();

        up = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_RESUME);
        down = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_RESUME_PRESSED);
        button = game.createSwitchScreenButton(up, down, this, game.loadGameScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        up = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_HELP);
        down = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_HELP_PRESSED);
        button = game.createSwitchScreenButton(up, down, this, game.instructionScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        up = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_SETTINGS);
        down = game.graphicsService.getSprite(Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BUTTON_SETTINGS_PRESSED);
        button = game.createSwitchScreenButton(up, down, this, game.loadGameScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }

        float x = (stage.getWidth() - table.getWidth()) / 2;
        float y = (stage.getHeight() - table.getHeight()) / 3;

        table.setPosition(x, y);
        stage.addActor(table);
    }
}
