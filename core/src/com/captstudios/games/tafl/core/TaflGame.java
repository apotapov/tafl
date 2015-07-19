package com.captstudios.games.tafl.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.level.TaflLevelService;
import com.captstudios.games.tafl.core.screen.AboutScreen;
import com.captstudios.games.tafl.core.screen.GamePlayScreen;
import com.captstudios.games.tafl.core.screen.InstructionScreen;
import com.captstudios.games.tafl.core.screen.LevelSelectionScreen;
import com.captstudios.games.tafl.core.screen.LoadGameScreen;
import com.captstudios.games.tafl.core.screen.MainMenuScreen;
import com.captstudios.games.tafl.core.screen.SettingsScreen;
import com.captstudios.games.tafl.core.screen.TaflCompanyScreen;
import com.captstudios.games.tafl.core.screen.TaflLoadingScreen;
import com.captstudios.games.tafl.core.utils.DoubleTextureDrawable;
import com.captstudios.games.tafl.core.utils.device.DeviceSettings;
import com.captstudios.games.tafl.core.utils.device.DeviceType;
import com.captstudios.games.tafl.core.utils.device.TaflGameConfig;
import com.captstudios.games.tafl.core.utils.services.TaflGraphicsService;
import com.captstudios.games.tafl.core.utils.services.TaflPreferenceService;
import com.captstudios.games.tafl.core.utils.services.TaflSoundService;
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
    public TaflLoadingScreen loadingScreen;
    public TaflCompanyScreen companyScreen;
    public InstructionScreen instructionScreen;
    public SettingsScreen settingsScreen;
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

    private boolean disposing;

    public TaflGame(TaflGameConfig config) {
        this.deviceSettings = new DeviceSettings(config);
    }

    @Override
    public void initialize() {
        this.levelService = new TaflLevelService(this);
        this.preferenceService = new TaflPreferenceService(soundService, this);
        this.graphicsService = new TaflGraphicsService(this);
        this.localeService = new LocaleService();

        this.assets = new Assets(soundService, graphicsService, localeService, levelService, this);

        if (Constants.GameConstants.DEBUG) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        mainMenuScreen = new MainMenuScreen(this);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(Assets.GraphicFiles.ATLAS_SPLASH));
        loadingScreen = new TaflLoadingScreen(this, atlas,  assets, mainMenuScreen);
        companyScreen = new TaflCompanyScreen(this, atlas, loadingScreen);
        settingsScreen = new SettingsScreen(this);
        aboutScreen = new AboutScreen(this, settingsScreen);
        levelSelectionScreen = new LevelSelectionScreen(this);
        loadGameScreen = new LoadGameScreen(this);
        gamePlayScreen = new GamePlayScreen(this);
        if (preferenceService.getShowHelpOnStart()) {
            instructionScreen = new InstructionScreen(this, gamePlayScreen);
        } else {
            instructionScreen = new InstructionScreen(this, mainMenuScreen);
        }
    }

    @Override
    public AbstractScreen<TaflGame> getFirstScreen() {
        if (deviceSettings.config.deviceType == DeviceType.IOS) {
            loadingScreen.initialize();
            return loadingScreen;
        } else {
            companyScreen.initialize();
            loadingScreen.initialize();
            return companyScreen;
        }
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
        return new TaflSoundService(this);
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
        settingsScreen.initialize();
        aboutScreen.initialize();
        levelSelectionScreen.initialize();
        loadGameScreen.initialize();
        gamePlayScreen.initialize();

        preferenceService.initialize();
    }


    public Button createSwitchScreenButton(
            Sprite text,
            Sprite up,
            Sprite down,
            final AbstractScreen<TaflGame> parent,
            final AbstractScreen<TaflGame> screen) {

        ImageButton button = new ImageButton(new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(text)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(text)));

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                parent.switchScreen(screen);
            }
        });

        return button;
    }

    public Button createSwitchScreenButton(Sprite icon,
            final AbstractScreen<TaflGame> parent,
            final AbstractScreen<TaflGame> screen) {

        ImageButton button = new ImageButton(new TextureRegionDrawable(new TextureRegion(icon)));

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                parent.switchScreen(screen);
            }
        });
        return button;
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

    @Override
    public void onPreferenceChange(String name, float value) {
    }
}
