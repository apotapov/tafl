package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.level.TaflLevel;
import com.captstudios.games.tafl.core.utils.DoubleTextureDrawable;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LevelSelectionScreen extends AbstractScreen<TaflGame> {

    public LevelSelectionScreen(TaflGame game) {
        super(game, game.mainMenuScreen, Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void initialize() {
        Sprite background = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.MENU);
        setBackgroundImage(new Image(background));

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().spaceBottom(game.deviceSettings.menuSpacing);

        Array<TaflLevel> levels = game.levelService.getLevels();

        createLevelList(levels, table);

        createPlayPreference(table);

        createButtons();

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }

    private void createLevelList(final Array<TaflLevel> levels, Table table) {
        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.SIZE);
        Image imageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));

        float height = game.deviceSettings.menuLabelHeight;
        float width = height * (imageLabel.getWidth() / imageLabel.getHeight());

        table.add(imageLabel).spaceBottom(game.deviceSettings.menuSpacing).size(width, height).expandX().right();

        final Sprite[] text = new Sprite[] {
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.SIZE_11),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.SIZE_9),
        };

        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.SIZE_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.SIZE_PRESSED);

        Sprite selectorTest = text[game.preferenceService.getLevelIndex()];

        final ImageButton selector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(selectorTest)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(selectorTest)));


        selector.addListener(new ChangeListener() {

            int selected = game.preferenceService.getLevelIndex();

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected = (selected + 1) % levels.size;
                ((DoubleTextureDrawable)selector.getStyle().imageDown).setInnerRegion(text[selected]);
                ((DoubleTextureDrawable)selector.getStyle().imageUp).setInnerRegion(text[selected]);
                game.preferenceService.setLevelIndex(selected);
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(selector).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight);
        table.row();
    }

    private void createPlayPreference(Table table) {

        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.PLAY_AS);
        Image imageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));

        float height = game.deviceSettings.menuLabelHeight * 1.5f;
        float width = height * (imageLabel.getWidth() / imageLabel.getHeight());

        table.add(imageLabel).spaceBottom(game.deviceSettings.menuSpacing).size(width, height).expandX().right();

        final Sprite[] text = new Sprite[] {
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.PLAY_WHITE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.PLAY_BLACK),
        };

        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_PRESSED);

        final int index = game.preferenceService.getComputerStarts() ? 0 : 1;

        Sprite selectorText = text[index];

        final ImageButton selector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(selectorText)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(selectorText)));


        selector.addListener(new ChangeListener() {

            int selected = index;

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected = (selected + 1) % 2;
                ((DoubleTextureDrawable)selector.getStyle().imageDown).setInnerRegion(text[selected]);
                ((DoubleTextureDrawable)selector.getStyle().imageUp).setInnerRegion(text[selected]);

                game.preferenceService.setComputerStarts(selected == Constants.BoardConstants.WHITE_TEAM);
                game.preferenceService.setLevelIndex(selected);

                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(selector).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight);
        table.row();
    }

    private void createButtons() {
        Table buttonTable = new Table();
        buttonTable.right().bottom().setFillParent(true);
        buttonTable.defaults().pad(game.deviceSettings.menuSpacing).size(
                game.deviceSettings.menuButtonHeight * 1.5f, game.deviceSettings.menuButtonHeight);
        Sprite icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.BACK);
        Button button = game.createSwitchScreenButton(icon, this, game.mainMenuScreen);
        buttonTable.add(button);

        icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.SETTINGS);
        button = new ImageButton(new TextureRegionDrawable(new TextureRegion(icon)));

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                game.settingsScreen.parentScreen = LevelSelectionScreen.this;
                switchScreen(game.settingsScreen);
            }
        });
        buttonTable.add(button).expandX();

        buttonTable.right().bottom().setFillParent(true);
        icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.PLAY);
        button = new ImageButton(new TextureRegionDrawable(new TextureRegion(icon)));
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                game.gamePlayScreen.createNewMatch();
                if (game.preferenceService.getShowHelpOnStart()) {
                    game.instructionScreen.parentScreen = game.gamePlayScreen;
                    switchScreen(game.instructionScreen);
                } else {
                    switchScreen(game.gamePlayScreen);
                }
            }
        });
        buttonTable.add(button);

        if (Constants.GameConstants.DEBUG) {
            buttonTable.debug();
        }

        stage.addActor(buttonTable);
    }
}
