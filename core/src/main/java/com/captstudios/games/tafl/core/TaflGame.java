package com.captstudios.games.tafl.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.DeviceType;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.level.TaflLevelService;
import com.captstudios.games.tafl.core.screen.AboutScreen;
import com.captstudios.games.tafl.core.screen.GamePlayScreen;
import com.captstudios.games.tafl.core.screen.InstructionScreen;
import com.captstudios.games.tafl.core.screen.LevelSelectionScreen;
import com.captstudios.games.tafl.core.screen.LoadGameScreen;
import com.captstudios.games.tafl.core.screen.MainMenuScreen;
import com.captstudios.games.tafl.core.screen.OptionsScreen;
import com.captstudios.games.tafl.core.utils.TaflGameConfig;
import com.captstudios.games.tafl.core.utils.TaflGraphicsService;
import com.captstudios.games.tafl.core.utils.TaflPreferenceService;
import com.roundtriangles.games.zaria.AbstractGame;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.screen.LoadingScreen;
import com.roundtriangles.games.zaria.services.GraphicsService;
import com.roundtriangles.games.zaria.services.IAssetBasedService;
import com.roundtriangles.games.zaria.services.SoundService;
import com.roundtriangles.games.zaria.services.resources.LocaleService;

public class TaflGame extends AbstractGame<TaflGame> implements IAssetBasedService {

    public AbstractScreen<?> currentScreen;

    public LoadingScreen<TaflGame> splashScreen;
    public MainMenuScreen mainMenuScreen;
    public InstructionScreen instructionScreen;
    public OptionsScreen optionsScreen;
    public AboutScreen aboutScreen;
    public LevelSelectionScreen levelSelectionScreen;
    public LoadGameScreen loadGameScreen;
    public GamePlayScreen gamePlayScreen;

    public Assets assets;
    public TaflLevelService levelService;
    public TaflPreferenceService preferenceService;
    public GraphicsService graphicsService;
    public LocaleService localeService;

    public DeviceType deviceType;

    private boolean disposing;

    public TaflGame(TaflGameConfig config) {
        this.deviceType = config.deviceType;
    }

    @Override
    public void initialize() {
        this.levelService = new TaflLevelService(this);
        this.preferenceService = new TaflPreferenceService(getClass().getSimpleName(), soundService);
        this.graphicsService = new TaflGraphicsService(this);
        this.localeService = new LocaleService();

        this.assets = new Assets(soundService, graphicsService, localeService, levelService, this);
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
    public FPSLogger createFPSLogger() {
        if (Constants.GameConstants.DEBUG) {
            return new FPSLogger();
        }
        return null;
    }

    @Override
    public SoundService createSoundService() {
        return new SoundService();
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
        instructionScreen = new InstructionScreen(this, mainMenuScreen);
        optionsScreen = new OptionsScreen(this);
        aboutScreen = new AboutScreen(this, optionsScreen);
        levelSelectionScreen = new LevelSelectionScreen(this);
        loadGameScreen = new LoadGameScreen(this);
        gamePlayScreen = new GamePlayScreen(this);

        mainMenuScreen.initialize();
        instructionScreen.initialize();
        optionsScreen.initialize();
        aboutScreen.initialize();
        levelSelectionScreen.initialize();
        loadGameScreen.initialize();
        gamePlayScreen.initialize();
    }

    public TextButton createSwitchScreenButton(String text, final Screen screen) {

        Skin skin = graphicsService.getSkin(Assets.Skin.UI_SKIN);
        TextButton button = new TextButton(text, skin, Assets.Skin.SKIN_STYLE_MENU);

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
