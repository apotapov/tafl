package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.enums.AiType;
import com.captstudios.games.tafl.core.level.TaflLevel;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LevelSelectionScreen extends AbstractScreen<TaflGame> {

    ObjectMap<String, String> localizedMap;

    public LevelSelectionScreen(TaflGame game) {
        super(game);

        localizedMap = new ObjectMap<String, String>();
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

        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.LEVEL_SELECTION_TITLE);
        Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);
        table.add(label).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        Array<TaflLevel> levels = game.levelService.getLevels();

        if (Constants.GameConstants.DEBUG) {
            createLevelList(levels, table, skin);
        } else {
            game.preferenceService.setLevel(Constants.GameConstants.DEFAULT_LEVEL_INDEX);
        }

        createPlayPreference(skin, table);

        createAiPreference(skin, table);

        createButtons(table, skin);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }

    private void createLevelList(Array<TaflLevel> levels, Table table, Skin skin) {
        String[] levelNames = new String[levels.size];
        int i = 0;
        for (TaflLevel level : levels) {
            String localizedName = game.localeService.get(level.name);
            levelNames[i++] = localizedName;
            localizedMap.put(localizedName, level.name);
        }

        final List levelList = new List(levelNames, skin, Assets.Skin.SKIN_STYLE_MENU);
        levelList.setSelectedIndex(game.preferenceService.getLevel());
        levelList.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setLevel(levelList.getSelectedIndex());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        table.add(levelList).spaceBottom(game.deviceType.menuSpacing);
        table.row();
    }

    private void createPlayPreference(Skin skin, Table table) {
        if (Constants.GameConstants.DEBUG) {
            String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.PLAY_VERSUS_COMPUTER);
            final CheckBox versusComputer = new CheckBox(text, skin, Assets.Skin.SKIN_STYLE_MENU);
            versusComputer.setChecked(game.preferenceService.getVersusComputer());
            versusComputer.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.preferenceService.setVersusComputer(versusComputer.isChecked());
                    game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                }
            });
            table.add(versusComputer).align(BaseTableLayout.LEFT);
            table.row();
        } else {
            game.preferenceService.setVersusComputer(Constants.GameConstants.DEFAULT_VERSUS_COMPUTER);
        }

        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.COMPUTER_STARTS);
        final CheckBox computerStarts = new CheckBox(text, skin, Assets.Skin.SKIN_STYLE_MENU);
        computerStarts.setChecked(game.preferenceService.getComputerStarts());
        computerStarts.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setComputerStarts(computerStarts.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(computerStarts).align(BaseTableLayout.LEFT).spaceBottom(game.deviceType.menuSpacing);
        table.row();
    }

    private void createAiPreference(Skin skin, Table table) {
        AiType[] aiTypes = AiType.values();

        String[] localizedTypes = new String[aiTypes.length];
        int i = 0;
        for (AiType type : aiTypes) {
            String localizedString = game.localeService.get(type.toString());
            localizedTypes[i++] = localizedString;
            localizedMap.put(localizedString, type.toString());
        }

        final SelectBox selectBox = new SelectBox(localizedTypes, skin, Assets.Skin.SKIN_STYLE_MENU);
        selectBox.setSelection(game.preferenceService.getAiType().ordinal());
        selectBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setAiType(localizedMap.get(selectBox.getSelection()));
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.AI_DIFFICULTY);
        table.add(text, Assets.Skin.SKIN_STYLE_MENU);
        table.row();
        table.add(selectBox).spaceBottom(game.deviceType.menuSpacing);
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
                game.setScreen(game.gamePlayScreen);
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(button).size(game.deviceType.menuButtonWidth,
                game.deviceType.menuButtonHeight).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        Button mainMenuButton = game.getMainMenuButton();
        table.add(mainMenuButton).size(game.deviceType.menuButtonWidth, game.deviceType.menuButtonHeight);
        table.row();
    }
}
