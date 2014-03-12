package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.enums.AiType;
import com.captstudios.games.tafl.core.level.TaflLevel;
import com.esotericsoftware.tablelayout.Cell;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LevelSelectionScreen extends AbstractScreen<TaflGame> {

    private static class ListItem<T> {
        //T value;
        String translatedValue;

        public ListItem(T value, String translatedValue) {
            //this.value = value;
            this.translatedValue = translatedValue;
        }

        @Override
        public String toString() {
            return translatedValue;
        }
    }


    public LevelSelectionScreen(TaflGame game) {
        super(game, game.mainMenuScreen, Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void initialize() {
        Sprite background = game.graphicsService.getSprite(
                game.deviceSettings.backgroundAtlas, Assets.Graphics.MENU);
        setBackgroundImage(new Image(background));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        Array<TaflLevel> levels = game.levelService.getLevels();

        createLevelList(levels, table, skin);

        createPlayPreference(skin, table);

        createAiPreference(skin, table);

        createButtons(table, skin);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }

    private void createLevelList(Array<TaflLevel> levels, Table table, Skin skin) {
        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.BOARD_SELECTION_TITLE);
        Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        Array<ListItem<String>> levelNames = new Array<ListItem<String>>(levels.size);
        for (TaflLevel level : levels) {
            String localizedName = game.localeService.get(level.name);
            levelNames.add(new ListItem<String>(level.name, localizedName));
        }

        final List<ListItem<String>> levelList = new List<ListItem<String>>(skin, Assets.Skin.SKIN_STYLE_MENU);
        levelList.setItems(levelNames);
        levelList.setSelectedIndex(game.preferenceService.getLevelIndex());
        levelList.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setLevelIndex(levelList.getSelectedIndex());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        table.add(levelList).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();
    }

    private void createPlayPreference(Skin skin, Table table) {

        Table innerTable = new Table(skin);

        final Drawable blackIcon = new TextureRegionDrawable(new TextureRegion(
                game.graphicsService.getSprite(
                        Assets.Graphics.ATLAS_PIECES, Assets.Graphics.BLACK_ICON)));

        final Drawable whiteIcon = new TextureRegionDrawable(new TextureRegion(
                game.graphicsService.getSprite(
                        Assets.Graphics.ATLAS_PIECES, Assets.Graphics.WHITE_ICON)));

        final Image computerIcon = new Image(game.preferenceService.getComputerStarts() ? blackIcon : whiteIcon);
        final Image humanIcon = new Image(game.preferenceService.getComputerStarts() ? whiteIcon : blackIcon);

        TextureRegion textureRegion = new TextureRegion(
                game.graphicsService.getSprite(
                        Assets.Graphics.ATLAS_PIECES, Assets.Graphics.UNDO_ICON));
        Drawable imageUp = new TextureRegionDrawable(textureRegion);
        ImageButton swap = new ImageButton(imageUp);

        swap.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (game.preferenceService.getComputerStarts()) {
                    game.preferenceService.setComputerStarts(false);
                    computerIcon.setDrawable(whiteIcon);
                    humanIcon.setDrawable(blackIcon);
                } else {
                    game.preferenceService.setComputerStarts(true);
                    computerIcon.setDrawable(blackIcon);
                    humanIcon.setDrawable(whiteIcon);
                }

                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        String text = game.localeService.get(LocalizedStrings.Game.HUMAN_PLAYER);
        Label humanLabel = new Label(text, skin, Assets.Skin.SKIN_STYLE_MENU);

        text = game.localeService.get(LocalizedStrings.Game.COMPUTER_PLAYER);
        Label computerLabel = new Label(text, skin, Assets.Skin.SKIN_STYLE_MENU);

        innerTable.add(humanIcon);
        innerTable.add(swap).size(50, 50);
        innerTable.add(computerIcon);
        innerTable.row();
        innerTable.add(humanLabel).uniform();
        innerTable.add();
        innerTable.add(computerLabel).uniform();

        text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.SIDE_SELECTION_TITLE);
        Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        table.add(innerTable).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();
    }

    private void createAiPreference(Skin skin, Table table) {

        Table innerTable = new Table(skin);

        AiType initialType = game.preferenceService.getAiType();
        final Label difficulty = new Label(game.localeService.get(initialType), skin, Assets.Skin.SKIN_STYLE_MENU);

        final Slider slider = new Slider(0, AiType.values().length-1, 1, false, skin);
        slider.setValue(initialType.ordinal());
        slider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AiType type = AiType.values()[(int) slider.getValue()];
                game.preferenceService.setAiType(type);
                difficulty.setText(game.localeService.get(type));
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.AI_DIFFICULTY);
        Label difficultyLabel = new Label(text, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);

        innerTable.add(difficultyLabel);
        innerTable.row();
        Cell<?> sliderCell = innerTable.add(slider);
        sliderCell.size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 15);
        innerTable.row();
        innerTable.add(difficulty);
        innerTable.row();

        table.add(innerTable).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();
    }

    private void createButtons(Table table, Skin skin) {
        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.PLAY_LEVEL_BUTTON);
        TextButton button = new TextButton(text, skin, Assets.Skin.SKIN_STYLE_MENU);
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
        table.add(button).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        Button mainMenuButton = game.getMainMenuButton(this);
        table.add(mainMenuButton).size(game.deviceSettings.menuButtonWidth, game.deviceSettings.menuButtonHeight);
        table.row();
    }
}
