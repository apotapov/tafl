package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LevelSelectionScreen extends AbstractScreen<TaflGame> {

    ObjectMap<String, String> localizedMap;

    public LevelSelectionScreen(TaflGame game) {
        super(game);

        localizedMap = new ObjectMap<String, String>();
    }

    @Override
    public void initialize() {
        stage.addActor(new Image(game.graphicsService.getTexture(Assets.Graphics.BACKGROUND_IMAGE)));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.LEVEL_SELECTION_TITLE);
        table.add(text, Assets.Skin.IN_GAME_STYLE_NAME);
        table.row();

        Array<TaflLevel> levels = game.levelService.getLevels();
        createLevelList(levels, table, skin);

        createPlayPreference(skin, table);

        createAiPreference(skin, table);

        createButtons(table, skin);

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

        final List levelList = new List(levelNames, skin, Assets.Skin.IN_GAME_STYLE_NAME);
        levelList.setSelectedIndex(game.preferenceService.getLevel());
        levelList.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setLevel(levelList.getSelectedIndex());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        table.add(levelList).size(Constants.ScreenConstants.LIST_WIDTH,
                Constants.ScreenConstants.LIST_HEIGHT).spaceBottom(Constants.ScreenConstants.SPACING);
        table.row();
    }

    private void createPlayPreference(Skin skin, Table table) {
        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.PLAY_VERSUS_COMPUTER);
        final CheckBox versusComputer = new CheckBox(text, skin, Assets.Skin.IN_GAME_STYLE_NAME);
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

        text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.COMPUTER_STARTS);
        final CheckBox computerStarts = new CheckBox(text, skin, Assets.Skin.IN_GAME_STYLE_NAME);
        computerStarts.setChecked(game.preferenceService.getComputerStarts());
        computerStarts.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setComputerStarts(computerStarts.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(computerStarts).align(BaseTableLayout.LEFT).spaceBottom(Constants.ScreenConstants.SPACING);
        table.row();
    }

    private void createAiPreference(Skin skin, Table table) {
        AiType[] aiTypes = AiType.values();

        int defaultSelection = 0;
        String[] types = new String[aiTypes.length - 1];
        int i = 0;
        for (AiType type : aiTypes) {
            if (type == game.preferenceService.getAiType()) {
                defaultSelection = i;
            }
            if (type != AiType.AI_NONE) {
                String localizedString = game.localeService.get(type);
                types[i++] = localizedString;
                localizedMap.put(localizedString, type.toString());
            }
        }

        final SelectBox selectBox = new SelectBox(types, skin, Assets.Skin.IN_GAME_STYLE_NAME);
        selectBox.setSelection(defaultSelection);
        selectBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setAiType(localizedMap.get(selectBox.getSelection()));
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.AI_DIFFICULTY);
        table.add(text, Assets.Skin.IN_GAME_STYLE_NAME);
        table.row();
        table.add(selectBox).spaceBottom(Constants.ScreenConstants.SPACING);
        table.row();
    }

    private void createButtons(Table table, Skin skin) {
        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.PLAY_LEVEL_BUTTON);
        TextButton button = new TextButton(text, skin, Assets.Skin.IN_GAME_STYLE_NAME);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                game.gamePlayScreen.createNewMatch();
                game.setScreen(game.gamePlayScreen);
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(button).size(Constants.ScreenConstants.BUTTON_WIDTH,
                Constants.ScreenConstants.BUTTON_HEIGHT).spaceBottom(Constants.ScreenConstants.SPACING);
        table.row();

        Button mainMenuButton = game.getMainMenuButton();
        table.add(mainMenuButton).size(Constants.ScreenConstants.BUTTON_WIDTH, Constants.ScreenConstants.BUTTON_HEIGHT);
        table.row();
    }
}
