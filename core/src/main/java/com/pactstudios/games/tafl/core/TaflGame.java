package com.pactstudios.games.tafl.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.level.TaflLevelService;
import com.pactstudios.games.tafl.core.screen.GamePlayScreen;
import com.pactstudios.games.tafl.core.screen.LevelSelectionScreen;
import com.pactstudios.games.tafl.core.screen.MainMenuScreen;
import com.pactstudios.games.tafl.core.screen.OptionsScreen;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;
import com.pactstudios.games.tafl.core.utils.TaflGameConfig;
import com.roundtriangles.games.zaria.AbstractGame;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.screen.LoadingScreen;
import com.roundtriangles.games.zaria.services.IAssetBasedService;

public class TaflGame extends AbstractGame<TaflGame> implements IAssetBasedService {

    public TaflGameConfig config;

    public LoadingScreen<TaflGame> splashScreen;
    public MainMenuScreen mainMenuScreen;
    public OptionsScreen optionsScreen;
    public LevelSelectionScreen levelSelectionScreen;
    public GamePlayScreen gamePlayScreen;

    public Assets assets;
    public TaflLevelService levelService;
    public TaflDatabaseService databaseService;

    private boolean disposing;

    public TaflGame(TaflGameConfig config) {
        this.config = config;
    }

    @Override
    public void initialize() {
        databaseService = new TaflDatabaseService(config.getConnectionSource());
        levelService = new TaflLevelService(databaseService);

        assets = new Assets(this, soundService, graphicsService, localeService, levelService, databaseService);
    }

    @Override
    public LoadingScreen<TaflGame> getLoadingScreen() {
        if (splashScreen == null) {
            splashScreen = new LoadingScreen<TaflGame>(this,
                    assets,
                    Assets.Graphics.SPLASH_IMAGE);
            splashScreen.initialize();
        }
        return splashScreen;
    }

    @Override
    public AbstractScreen<TaflGame> getMainMenuScreen() {
        return mainMenuScreen;
    }

    @Override
    public void dispose() {
        // breaking infinite loop
        if (!disposing) {
            disposing = true;
            super.dispose();
            assets.dispose();
            disposing = false;
        }
    }

    public Button getMainMenuButton() {
        TextButton button = new TextButton(localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON),
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

    @Override
    public void setAssetManager(AssetManager assetManager) {
        // do nothing
    }

    @Override
    public void onFinishLoading() {
        mainMenuScreen = new MainMenuScreen(this);
        optionsScreen = new OptionsScreen(this);
        levelSelectionScreen = new LevelSelectionScreen(this);
        gamePlayScreen = new GamePlayScreen(this);

        mainMenuScreen.initialize();
        optionsScreen.initialize();
        levelSelectionScreen.initialize();
        gamePlayScreen.initialize();
    }
}
