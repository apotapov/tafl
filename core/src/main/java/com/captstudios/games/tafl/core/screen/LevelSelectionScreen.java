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
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.AiType;
import com.captstudios.games.tafl.core.utils.DoubleTextureDrawable;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LevelSelectionScreen extends AbstractScreen<TaflGame> {

    ImageButton boardSelector;
    Sprite[] boardValues;

    ImageButton sideSelector;
    Sprite[] sideValues;

    ImageButton difficultySelector;
    Sprite[] difficultyValues;


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
        table.defaults().space(game.deviceSettings.menuSpacing);

        createBoardSelector(table);
        createSideSelector(table);
        createDifficultySelector(table);
        createButtons();

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }

    private void createDifficultySelector(Table table) {
        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.DIFFICULTY);
        Image difficultyImageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));

        float height = game.deviceSettings.menuLabelHeight * 1.5f;
        float width = height * (difficultyImageLabel.getWidth() / difficultyImageLabel.getHeight());

        table.add(difficultyImageLabel).spaceBottom(game.deviceSettings.menuSpacing).size(width, height).expandX().right();

        difficultyValues = new Sprite[] {
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.NOVICE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.BEGINNER),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.INTERMEDIATE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.ADVANCED)
        };

        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_PRESSED);

        final AiType initialType = game.preferenceService.getAiType();
        Sprite selectorTest = difficultyValues[initialType.ordinal()];

        difficultySelector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(selectorTest)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(selectorTest)));

        height = game.deviceSettings.menuButtonHeight;
        width = height * (up.getWidth() / up.getHeight());

        table.add(difficultySelector).size(width, height).expandX().left();
        table.row();
    }

    private void createBoardSelector(Table table) {
        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.SIZE);
        Image imageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));

        float height = game.deviceSettings.menuLabelHeight;
        float width = height * (imageLabel.getWidth() / imageLabel.getHeight());

        table.add(imageLabel).spaceBottom(game.deviceSettings.menuSpacing).size(width, height).expandX().right();

        boardValues = new Sprite[] {
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.SIZE_11),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.SIZE_9),
        };

        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.SIZE_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.SIZE_PRESSED);

        Sprite selectorText = boardValues[game.preferenceService.getLevelIndex()];

        boardSelector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(selectorText)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(selectorText)));

        height = game.deviceSettings.menuButtonHeight;
        width = height * (up.getWidth() / up.getHeight());

        table.add(boardSelector).size(width, height).expandX().left();
        table.row();
    }

    private void createSideSelector(Table table) {

        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.PLAY_AS);
        Image imageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));

        float height = game.deviceSettings.menuLabelHeight * 1.5f;
        float width = height * (imageLabel.getWidth() / imageLabel.getHeight());

        table.add(imageLabel).spaceBottom(game.deviceSettings.menuSpacing).size(width, height).expandX().right();

        sideValues = new Sprite[] {
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.PLAY_WHITE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.PLAY_BLACK),
        };

        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_PRESSED);

        final int index = game.preferenceService.getComputerStarts() ?
                Constants.BoardConstants.WHITE_TEAM : Constants.BoardConstants.BLACK_TEAM;

        Sprite selectorText = sideValues[index];

        sideSelector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(selectorText)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(selectorText)));

        height = game.deviceSettings.menuButtonHeight;
        width = height * (up.getWidth() / up.getHeight());

        table.add(sideSelector).size(width, height).expandX().left();
        table.row();
    }

    private void createButtons() {
        Table buttonTable = new Table();
        buttonTable.right().bottom().setFillParent(true);
        buttonTable.defaults().pad(game.deviceSettings.menuSpacing).size(
                game.deviceSettings.menuButtonHeight * 1.5f, game.deviceSettings.menuButtonHeight);

        Sprite icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.BACK);
        Button button = game.createSwitchScreenButton(icon, this, game.mainMenuScreen);
        buttonTable.add(button).expandX().left();

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

    @Override
    public void show() {
        super.show();

        updateBoardSelector();
        updateSideSelector();
        updateDifficultySelector();
    }

    private void updateSideSelector() {
        final int index = game.preferenceService.getComputerStarts() ?
                Constants.BoardConstants.WHITE_TEAM : Constants.BoardConstants.BLACK_TEAM;

        ((DoubleTextureDrawable)sideSelector.getStyle().imageDown).setInnerRegion(sideValues[index]);
        ((DoubleTextureDrawable)sideSelector.getStyle().imageUp).setInnerRegion(sideValues[index]);

        sideSelector.addListener(new ChangeListener() {

            int selected = index;

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected = (selected + 1) % 2;
                ((DoubleTextureDrawable)sideSelector.getStyle().imageDown).setInnerRegion(sideValues[selected]);
                ((DoubleTextureDrawable)sideSelector.getStyle().imageUp).setInnerRegion(sideValues[selected]);

                game.preferenceService.setComputerStarts(selected == Constants.BoardConstants.WHITE_TEAM);
                game.preferenceService.setLevelIndex(selected);

                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
    }

    private void updateBoardSelector() {
        ((DoubleTextureDrawable)boardSelector.getStyle().imageDown).setInnerRegion(
                boardValues[game.preferenceService.getLevelIndex()]);
        ((DoubleTextureDrawable)boardSelector.getStyle().imageUp).setInnerRegion(
                boardValues[game.preferenceService.getLevelIndex()]);

        boardSelector.addListener(new ChangeListener() {

            int selected = game.preferenceService.getLevelIndex();

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected = (selected + 1) % boardValues.length;
                ((DoubleTextureDrawable)boardSelector.getStyle().imageDown).setInnerRegion(boardValues[selected]);
                ((DoubleTextureDrawable)boardSelector.getStyle().imageUp).setInnerRegion(boardValues[selected]);
                game.preferenceService.setLevelIndex(selected);
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
    }

    private void updateDifficultySelector() {
        final AiType initialType = game.preferenceService.getAiType();

        ((DoubleTextureDrawable)difficultySelector.getStyle().imageDown).setInnerRegion(difficultyValues[initialType.ordinal()]);
        ((DoubleTextureDrawable)difficultySelector.getStyle().imageUp).setInnerRegion(difficultyValues[initialType.ordinal()]);

        difficultySelector.addListener(new ChangeListener() {

            int selected = initialType.ordinal();

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected = (selected + 1) % difficultyValues.length;
                ((DoubleTextureDrawable)difficultySelector.getStyle().imageDown).setInnerRegion(difficultyValues[selected]);
                ((DoubleTextureDrawable)difficultySelector.getStyle().imageUp).setInnerRegion(difficultyValues[selected]);
                game.preferenceService.setAiType(AiType.values()[selected]);
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
    }
}
