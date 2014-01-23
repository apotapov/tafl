package com.pactstudios.games.tafl.core;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.level.TaflLevelService;
import com.pactstudios.games.tafl.core.profile.TaflProfileService;
import com.pactstudios.games.tafl.core.screen.GamePlayScreen;
import com.pactstudios.games.tafl.core.screen.LevelSelectionScreen;
import com.pactstudios.games.tafl.core.screen.MainMenuScreen;
import com.pactstudios.games.tafl.core.screen.OptionsScreen;
import com.roundtriangles.games.zaria.AbstractGame;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.screen.LoadingScreen;

public class TaflGame extends AbstractGame<TaflGame>  {
    private static final int LOADING_IMAGE_WIDTH = 512;
    private static final int LOADING_IMAGE_HEIGHT = 301;

    LoadingScreen<TaflGame> splashScreen;
    MainMenuScreen mainMenuScreen;
    OptionsScreen optionsScreen;
    LevelSelectionScreen levelSelectionScreen;
    GamePlayScreen gamePlayScreen;

    Assets assets;
    TaflLevelService levelService;
    TaflProfileService profileService;

    @Override
    public void initialize() {
        levelService = new TaflLevelService();
        assets = new Assets(soundService, graphicsService, localeService, levelService);
        profileService = new TaflProfileService();
        profileService.initialize(Assets.Game.PROFILE_DATA_FILE);
    }

    @Override
    public LoadingScreen<TaflGame> getLoadingScreen() {
        if (splashScreen == null) {
            splashScreen = new LoadingScreen<TaflGame>(this,
                    assets,
                    Assets.Graphics.SPLASH_IMAGE,
                    LOADING_IMAGE_WIDTH,
                    LOADING_IMAGE_HEIGHT);
        }
        return splashScreen;
    }

    @Override
    public AbstractScreen<TaflGame> getMainMenuScreen() {
        if (mainMenuScreen == null) {
            mainMenuScreen = new MainMenuScreen(this);
        }
        return mainMenuScreen;
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
    }

    public OptionsScreen getOptionsScreen() {
        if (optionsScreen == null) {
            optionsScreen = new OptionsScreen(this);
        }
        return optionsScreen;
    }

    public LevelSelectionScreen getLevelSelectionScreen() {
        if (levelSelectionScreen == null) {
            levelSelectionScreen = new LevelSelectionScreen(this);
        }
        return levelSelectionScreen;
    }

    public GamePlayScreen getGamePlayScreen() {
        if (gamePlayScreen == null) {
            gamePlayScreen = new GamePlayScreen(this);
        }
        return gamePlayScreen;
    }


    public Button getMainMenuButton() {
        TextButton button = new TextButton(localeService._(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON),
                graphicsService.getSkin(Assets.Skin.UI_SKIN), Assets.Skin.MENU_STYLE_NAME);
        button.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                setScreen(getMainMenuScreen());
            }
        });
        return button;
    }

    public TaflLevelService getLevelService() {
        return levelService;
    }

    public TaflProfileService getProfileService() {
        return profileService;
    }
}
