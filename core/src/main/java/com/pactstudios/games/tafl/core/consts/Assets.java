package com.pactstudios.games.tafl.core.consts;

import java.util.Locale;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.pactstudios.games.tafl.core.level.TaflLevelService;
import com.roundtriangles.games.zaria.services.GraphicsService;
import com.roundtriangles.games.zaria.services.SoundService;
import com.roundtriangles.games.zaria.services.db.DatabaseService;
import com.roundtriangles.games.zaria.services.resources.LocaleService;
import com.roundtriangles.games.zaria.services.utils.FontDefinition;
import com.roundtriangles.games.zaria.services.utils.GameAssetLoader;
import com.roundtriangles.games.zaria.utils.CustomSkinLoader;

public class Assets extends GameAssetLoader<TaflLevel> {

    public static final class Graphics {
        private Graphics() {
        }

        public static final String SPLASH_IMAGE = "images/tafl-splash-01.png";
        public static final String BACKGROUND_IMAGE = "images/tafl-menu-01-background.png";
        public static final String BOARD_IMAGE = "images/tafl-board-01-02-11x11.png";

        public static final String PIECE_ATLAS = "image-atlases/pieces.atlas";
        public static final String WHITE_PIECE = "tafl-piece-light-01-11x11";
        public static final String KING_PIECE = "tafl-piece-king-01-11x11";
        public static final String BLACK_PIECE = "tafl-piece-dark-01-11x11";

        public static final String MENU_ICON = "tafl-menu-icon-mainmenu";
        public static final String SETTINGS_ICON = "tafl-menu-icon-settings";
        public static final String UNDO_ICON = "tafl-menu-icon-undo";
        public static final String BACK_ICON = "tafl-menu-icon-back";

        public static final String EXPLOSION_ATLAS = "image-atlases/explosion.atlas";
        public static final String EXPLOSION = "explosion";
    }

    public static final class Skin {
        private Skin() {
        }
        public static final String UI_SKIN = "skin/uiskin.json";
        public static final String MENU_STYLE_NAME = "menu";
    }

    public static final class Sounds {
        private Sounds() {
        }
        public static final String LEVEL_MUSIC = "music/tafl-music-background-TravisChow-02.mp3";
        public static final String MENU_MUSIC = "music/tafl-music-background-TravisChow-01.mp3";
        public static final String CLICK_SOUND = "sound/click.wav";
    }

    public static final class Locales {
        private Locales() {
        }

        public static final Locale DEFAULT_LOCALE = Locale.US;
        public static final Locale[] ADDITIONAL_LOCALES = {
            new Locale("es", "ES")
        };
    }

    public static final class Game {
        private Game() {
        }

        public static final String LEVEL_LIST = "levels/levels.json";
    }

    public static final class Fonts {
        private Fonts() {
        }
        public static final FontDefinition BLOWHOLE_FONT_GAME = new FontDefinition("skin/BlowholeBB.ttf", 32);
        public static final FontDefinition BLOWHOLE_FONT_MENU = new FontDefinition("skin/BlowholeBB.ttf", 44);
        public static final FontDefinition GOTHAM_MEDIUM_GAME = new FontDefinition("skin/Gotham-Medium.otf", 32);
    }

    protected SoundService soundService;
    protected LocaleService localeService;
    protected GraphicsService graphicsService;
    protected TaflLevelService levelService;
    protected DatabaseService databaseService;

    public Assets(TaflGame taflGame,
            SoundService soundService,
            GraphicsService graphicsService,
            LocaleService localeService,
            TaflLevelService levelService,
            DatabaseService databaseService) {
        super(taflGame, soundService, graphicsService, localeService, levelService, databaseService);

        this.soundService = soundService;
        this.graphicsService = graphicsService;
        this.localeService = localeService;
        this.levelService = levelService;
        this.databaseService = databaseService;

        assetManager.setLoader(com.badlogic.gdx.scenes.scene2d.ui.Skin.class,
                new CustomSkinLoader(new InternalFileHandleResolver()));
    }

    @Override
    public void loadAssets() {
        soundService.loadMusic(Assets.Sounds.MENU_MUSIC, Assets.Sounds.LEVEL_MUSIC);
        soundService.loadSound(Assets.Sounds.CLICK_SOUND);

        graphicsService.loadSkins(Assets.Skin.UI_SKIN);

        graphicsService.loadTextures(
                Assets.Graphics.SPLASH_IMAGE,
                Assets.Graphics.BACKGROUND_IMAGE,
                Assets.Graphics.BOARD_IMAGE);

        graphicsService.loadTextureAtlases(
                Assets.Graphics.PIECE_ATLAS,
                Assets.Graphics.EXPLOSION_ATLAS);

        graphicsService.loadFonts(Assets.Fonts.BLOWHOLE_FONT_GAME,
                Assets.Fonts.BLOWHOLE_FONT_MENU,
                Assets.Fonts.GOTHAM_MEDIUM_GAME);

        localeService.load(
                Assets.Locales.DEFAULT_LOCALE,
                Assets.Locales.ADDITIONAL_LOCALES);

        levelService.load();

        databaseService.load();
    }
}
