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
                Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.MENU);
        setBackgroundImage(new Image(background));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);

        Sprite text = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.START);
        Sprite up = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK);
        Sprite down = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK_PRESSED);
        Button button = game.createSwitchScreenButton(text, up, down, this, game.levelSelectionScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);

        table.row();

        text = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.RESUME);
        up = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK);
        down = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK_PRESSED);
        button = game.createSwitchScreenButton(text, up, down, this, game.loadGameScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        text = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.HELP);
        up = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK);
        down = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK_PRESSED);
        button = game.createSwitchScreenButton(text, up, down, this, game.instructionScreen);
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        Table iconTable = new Table(skin);
        iconTable.right().bottom().setFillParent(true);
        Sprite settingsIcon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.SETTINGS);
        button = game.createSwitchScreenButton(settingsIcon, this, game.settingsScreen);
        iconTable.add(button).pad(Constants.HudConstants.PLAYER_LABEL_PAD_TOP / 4);


        if (Constants.GameConstants.DEBUG) {
            table.debug();
            iconTable.debug();
        }

        float x = (stage.getWidth() - table.getWidth()) / 2;
        float y = (stage.getHeight() - table.getHeight()) * 5 / 12;

        table.setPosition(x, y);
        stage.addActor(table);
        stage.addActor(iconTable);
    }
}
