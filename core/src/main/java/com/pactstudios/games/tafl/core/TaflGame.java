package com.pactstudios.games.tafl.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.DeviceType;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.level.TaflLevelService;
import com.pactstudios.games.tafl.core.screen.GamePlayScreen;
import com.pactstudios.games.tafl.core.screen.LevelSelectionScreen;
import com.pactstudios.games.tafl.core.screen.LoadGameScreen;
import com.pactstudios.games.tafl.core.screen.MainMenuScreen;
import com.pactstudios.games.tafl.core.screen.OptionsScreen;
import com.pactstudios.games.tafl.core.utils.TaflGameConfig;
import com.pactstudios.games.tafl.core.utils.TaflPreferenceService;
import com.roundtriangles.games.zaria.AbstractGame;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.screen.LoadingScreen;
import com.roundtriangles.games.zaria.services.IAssetBasedService;

public class TaflGame extends AbstractGame<TaflGame> implements IAssetBasedService {

    public AbstractScreen<?> currentScreen;

    public LoadingScreen<TaflGame> splashScreen;
    public MainMenuScreen mainMenuScreen;
    public OptionsScreen optionsScreen;
    public LevelSelectionScreen levelSelectionScreen;
    public LoadGameScreen loadGameScreen;
    public GamePlayScreen gamePlayScreen;

    public Assets assets;
    public TaflLevelService levelService;
    public TaflPreferenceService preferenceService;

    public DeviceType deviceType;

    private boolean disposing;

    public TaflGame(TaflGameConfig config) {
        this.deviceType = config.deviceType;
    }

    @Override
    public void create() {
        super.create();
        this.fpsLogger = null;
    }

    @Override
    public void initialize() {
        levelService = new TaflLevelService(this);
        preferenceService = new TaflPreferenceService(getClass().getSimpleName(), soundService);

        assets = new Assets(this, soundService, graphicsService, localeService, levelService);
    }

    @Override
    public LoadingScreen<TaflGame> getLoadingScreen() {
        if (splashScreen == null) {
            splashScreen = new LoadingScreen<TaflGame>(this,
                    assets,
                    Assets.Graphics.SPLASH_ATLAS,
                    Assets.Graphics.SPLASH,
                    Constants.ScreenConstants.DISPLAY_TIME,
                    Constants.ScreenConstants.FADE_TIME) {

                @Override
                public void resize(int width, int height) {
                    super.resize(width, height);

                    if (deviceType != DeviceType.DESKTOP) {
                        deviceType = DeviceType.getDeviceType(width, height);
                    }
                }
            };
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

    @Override
    public void setAssetManager(AssetManager assetManager) {
        // do nothing
    }

    @Override
    public void onFinishLoading() {
        mainMenuScreen = new MainMenuScreen(this);
        optionsScreen = new OptionsScreen(this);
        levelSelectionScreen = new LevelSelectionScreen(this);
        loadGameScreen = new LoadGameScreen(this);
        gamePlayScreen = new GamePlayScreen(this);

        mainMenuScreen.initialize();
        optionsScreen.initialize();
        levelSelectionScreen.initialize();
        loadGameScreen.initialize();
        gamePlayScreen.initialize();
    }

    public TextButton createSwitchScreenButton(String text, final Screen screen) {
        TextButton button = new TextButton(text,
                graphicsService.getSkin(Assets.Skin.UI_SKIN),
                deviceType.menuStyle);

        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                setScreen(screen);
            }
        });
        return button;
    }

    public Button getMainMenuButton() {
        return createSwitchScreenButton(
                localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON),
                mainMenuScreen);
    }
}
