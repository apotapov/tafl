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
import com.pactstudios.games.tafl.core.profile.TaflProfile;
import com.pactstudios.games.tafl.core.profile.TaflProfileService;
import com.roundtriangles.games.zaria.AbstractScreen;
import com.roundtriangles.games.zaria.services.LocaleService;
import com.roundtriangles.games.zaria.services.SoundService;

public class LevelSelectionScreen extends AbstractScreen<TaflGame> {

    ObjectMap<String, String> localizedMap;

    public LevelSelectionScreen(final TaflGame game) {
        super(game);

        localizedMap = new ObjectMap<String, String>();

        TaflProfileService profileService = game.getProfileService();
        TaflProfile profile = profileService.retrieveProfile();

        TaflLevelService levelService = game.getLevelService();

        Skin skin = game.getGraphicsService().getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        LocaleService localeService = game.getLocaleService();
        final SoundService soundService = game.getSoundService();

        table.add(localeService._(LocalizedStrings.LevelSelectionMenu.LEVEL_SELECTION_TITLE)).spaceBottom(20);
        table.row();


        final OrderedMap<String, TaflLevel> levels = levelService.getLevels();
        String[] levelNames = new String[levels.size];
        int i = 0;
        for (String name : levels.keys()) {
            String localizedName = localeService._(name);
            levelNames[i++] = localizedName;
            localizedMap.put(localizedName, name);
        }

        final List list = new List(levelNames, skin, Assets.Skin.MENU_STYLE_NAME);
        if (profile.currentLevel != null) {
            //list.setSelection(localeService._(profile.currentLevel));
            list.setSelectedIndex(0);
        } else {
            profile.currentLevel = localizedMap.get(levelNames[0]);
            profileService.persist();
            list.setSelectedIndex(0);
        }

        table.add(list).size(Constants.ScreenConstants.LIST_WIDTH, Constants.ScreenConstants.LIST_HEIGHT).uniform().spaceBottom(10);
        table.row();

        String text = localeService._(LocalizedStrings.LevelSelectionMenu.PLAY_LEVEL_BUTTON);
        TextButton button = new TextButton(text, skin, Assets.Skin.MENU_STYLE_NAME);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                GamePlayScreen screen = game.getGamePlayScreen();
                screen.setLevel(levels.get(localizedMap.get(list.getSelection())));
                game.setScreen(screen);
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
