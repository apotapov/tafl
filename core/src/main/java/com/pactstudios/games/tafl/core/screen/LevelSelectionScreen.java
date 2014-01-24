package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.pactstudios.games.tafl.core.level.TaflLevelService;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class LevelSelectionScreen extends AbstractScreen<TaflGame> {

    ObjectMap<String, String> localizedMap;

    public LevelSelectionScreen(TaflGame game) {
        super(game);

        localizedMap = new ObjectMap<String, String>();
    }

    @Override
    public void initialize() {
        TaflLevelService levelService = game.levelService;

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        table.add(game.localeService.get(LocalizedStrings.LevelSelectionMenu.LEVEL_SELECTION_TITLE)).spaceBottom(20);
        table.row();


        final OrderedMap<String, TaflLevel> levels = levelService.getLevels();
        String[] levelNames = new String[levels.size];
        int i = 0;
        for (String name : levels.keys()) {
            String localizedName = game.localeService.get(name);
            levelNames[i++] = localizedName;
            localizedMap.put(localizedName, name);
        }

        final List list = new List(levelNames, skin, Assets.Skin.MENU_STYLE_NAME);
        list.setSelectedIndex(0);

        table.add(list).size(Constants.ScreenConstants.LIST_WIDTH, Constants.ScreenConstants.LIST_HEIGHT).uniform().spaceBottom(10);
        table.row();

        String text = game.localeService.get(LocalizedStrings.LevelSelectionMenu.PLAY_LEVEL_BUTTON);
        TextButton button = new TextButton(text, skin, Assets.Skin.MENU_STYLE_NAME);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                game.gamePlayScreen.setLevel(levels.get(localizedMap.get(list.getSelection())));
                game.gamePlayScreen.createNewMatch();
                game.setScreen(game.gamePlayScreen);
            }
        });
        table.add(button).size(Constants.ScreenConstants.BUTTON_WIDTH, Constants.ScreenConstants.BUTTON_HEIGHT).uniform();
        table.row();

        Button mainMenuButton = game.getMainMenuButton();
        table.add(mainMenuButton).uniform();
        table.row();

        stage.addActor(table);
    }
}
