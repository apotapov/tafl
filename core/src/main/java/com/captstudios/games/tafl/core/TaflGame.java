package com.captstudios.games.tafl.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.level.TaflLevelService;
import com.captstudios.games.tafl.core.screen.AboutScreen;
import com.captstudios.games.tafl.core.screen.GamePlayScreen;
import com.captstudios.games.tafl.core.screen.InstructionScreen;
import com.captstudios.games.tafl.core.screen.LevelSelectionScreen;
import com.captstudios.games.tafl.core.screen.LoadGameScreen;
import com.captstudios.games.tafl.core.screen.MainMenuScreen;
import com.captstudios.games.tafl.core.screen.OptionsScreen;
import com.captstudios.games.tafl.core.screen.TaflCompanyScreen;
import com.captstudios.games.tafl.core.screen.TaflLoadingScreen;
import com.captstudios.games.tafl.core.utils.TaflGraphicsService;
import com.captstudios.games.tafl.core.utils.TaflPreferenceService;
import com.captstudios.games.tafl.core.utils.device.DeviceSettings;
import com.captstudios.games.tafl.core.utils.device.TaflGameConfig;
import com.roundtriangles.games.zaria.AbstractGame;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.services.GraphicsService;
import com.roundtriangles.games.zaria.services.IAssetBasedService;
import com.roundtriangles.games.zaria.services.PreferenceService.PreferenceChangeListener;
import com.roundtriangles.games.zaria.services.SoundService;
import com.roundtriangles.games.zaria.services.resources.LocaleService;

public class TaflGame extends AbstractGame<TaflGame> implements IAssetBasedService, PreferenceChangeListener {

    public AbstractScreen<?> currentScreen;

    public MainMenuScreen mainMenuScreen;
    public TaflLoadingScreen splashScreen;
    public TaflCompanyScreen companyScreen;
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

    public DeviceSettings deviceSettings;
    public TaflGameConfig config;

    private boolean disposing;

    public TaflGame(TaflGameConfig config) {
        this.config = config;
        this.deviceSettings = new DeviceSettings();
        this.deviceSettings.deviceType = config.deviceType;
    }

    @Override
    public void initialize() {
        this.levelService = new TaflLevelService(this);
        this.preferenceService = new TaflPreferenceService(getClass().toString(), soundService, this);
        this.graphicsService = new TaflGraphicsService(this);
        this.localeService = new LocaleService();

        this.assets = new Assets(soundService, graphicsService, localeService, levelService, this);

        if (Constants.GameConstants.DEBUG) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        mainMenuScreen = new MainMenuScreen(this);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(Assets.Graphics.ATLAS_BACKGROUNDS));
        splashScreen = new TaflLoadingScreen(this, atlas,  assets, mainMenuScreen);
        companyScreen = new TaflCompanyScreen(this, atlas, splashScreen);
        instructionScreen = new InstructionScreen(this, mainMenuScreen);
        optionsScreen = new OptionsScreen(this);
        aboutScreen = new AboutScreen(this, optionsScreen);
        levelSelectionScreen = new LevelSelectionScreen(this);
        loadGameScreen = new LoadGameScreen(this);
        gamePlayScreen = new GamePlayScreen(this);
    }

    @Override
    public AbstractScreen<TaflGame> getFirstScreen() {
        return companyScreen;
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

            @SuppressWarnings("unchecked")
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                ((AbstractScreen<TaflGame>)getScreen()).switchScreen(screen);
            }
        });
        return button;
    }

    public Button getMainMenuButton() {
        return createSwitchScreenButton(
                localeService.get(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON),
                mainMenuScreen);
    }

    @Override
    public void onPreferenceChange(String name, boolean value) {
        if (TaflPreferenceService.PREF_SHOW_HELP_ON_START.equals(name) && instructionScreen != null) {
            if (value) {
                instructionScreen.parentScreen = gamePlayScreen;
            } else {
                instructionScreen.parentScreen = mainMenuScreen;
            }
        }
    }

    @Override
    public void onPreferenceChange(String name, int value) {
    }

    @Override
    public void onPreferenceChange(String name, String value) {
    }
}
