package com.pactstudios.games.tafl.core.consts;

import java.util.Locale;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.level.TaflLevelService;
import com.roundtriangles.games.zaria.services.GraphicsService;
import com.roundtriangles.games.zaria.services.SoundService;
import com.roundtriangles.games.zaria.services.resources.LocaleService;
import com.roundtriangles.games.zaria.services.utils.FontDefinition;
import com.roundtriangles.games.zaria.services.utils.GameAssetLoader;
import com.roundtriangles.games.zaria.utils.CustomSkinLoader;

public class Assets extends GameAssetLoader {

    public static final class Graphics {
        private Graphics() {
        }

        public static final String BOARD_ATLAS = "image-atlases/board.atlas";
        public static final String BOARD = "board";

        public static final String SPLASH_ATLAS = "image-atlases/splash.atlas";
        public static final String SPLASH = "tafl-splash-01";

        public static final String MENU_ATLAS = "image-atlases/menu.atlas";
        public static final String MENU = "tafl-menu-01-background";

        public static final String PIECE_ATLAS = "image-atlases/pieces.atlas";
        public static final String WHITE_PIECE = "tafl-piece-light-01-11x11";
        public static final String KING_PIECE = "tafl-piece-king-01-11x11";
        public static final String BLACK_PIECE = "tafl-piece-dark-01-11x11";

        public static final String BLACK_ICON = "tafl-piece-dark-01-icon";
        public static final String WHITE_ICON = "tafl-piece-light-01-icon";

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


        public static final String SKIN_STYLE_PHONE_DEFAULT = "default";
        public static final String SKIN_STYLE_PHONE_MENU = "menu";
        public static final String SKIN_STYLE_PHONE_DIALOG = "dialog";
        public static final String SKIN_STYLE_PHONE_PLAYER = "player-tag";

        public static final String SKIN_STYLE_TABLET_DEFAULT = "tablet-default";
        public static final String SKIN_STYLE_TABLET_MENU = "tablet-menu";
        public static final String SKIN_STYLE_TABLET_DIALOG = "tablet-dialog";
        public static final String SKIN_STYLE_TABLET_PLAYER = "tablet-player-tag";
    }

    public static final class Sounds {
        private Sounds() {
        }
        public static final String LEVEL_MUSIC = "music/tafl-music-background-TravisChow-02.mp3";
        public static final String MENU_MUSIC = "music/tafl-music-background-TravisChow-01.mp3";

        public static final String CLICK_SOUND = "sound/tafl-sound-click-01.mp3";
        public static final String CLICK_2_SOUND = "sound/tafl-sound-click-02.mp3";
        public static final String WHITE_MOVE_1_SOUND = "sound/tafl-sound-move-01a.mp3";
        public static final String WHITE_MOVE_2_SOUND = "sound/tafl-sound-move-01b.mp3";
        public static final String BLACK_MOVE_1_SOUND = "sound/tafl-sound-move-02a.mp3";
        public static final String BLACK_MOVE_2_SOUND = "sound/tafl-sound-move-02b.mp3";
        public static final String UNDO_SOUND = "sound/tafl-sound-undo-01.mp3";
        public static final String CAPTURE_1_SOUND = "sound/tafl-sound-capture-01.mp3";
        public static final String CAPTURE_2_SOUND = "sound/tafl-sound-capture-02.mp3";
        public static final String CAPTURE_3_SOUND = "sound/tafl-sound-capture-03.mp3";
        public static final String KING_WIN_SOUND = "sound/tafl-sound-king-lose-01.mp3";
        public static final String KING_LOSE_SOUND = "sound/tafl-sound-king-win-01.mp3";
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
        public static final FontDefinition GOTHAM_MEDIUM_PHONE = new FontDefinition("skin/Gotham-Medium.otf", 32);
        public static final FontDefinition GOTHAM_MEDIUM_TABLET = new FontDefinition("skin/Gotham-Medium.otf", 64);
    }

    protected SoundService soundService;
    protected LocaleService localeService;
    protected GraphicsService graphicsService;
    protected TaflLevelService levelService;

    public Assets(TaflGame taflGame,
            SoundService soundService,
            GraphicsService graphicsService,
            LocaleService localeService,
            TaflLevelService levelService) {
        super(taflGame, soundService, graphicsService, localeService, levelService);

        this.soundService = soundService;
        this.graphicsService = graphicsService;
        this.localeService = localeService;
        this.levelService = levelService;

        assetManager.setLoader(com.badlogic.gdx.scenes.scene2d.ui.Skin.class,
                new CustomSkinLoader(new InternalFileHandleResolver()));
    }

    @Override
    public void loadAssets() {
        soundService.loadMusic(Assets.Sounds.MENU_MUSIC, Assets.Sounds.LEVEL_MUSIC);
        soundService.loadSound(Assets.Sounds.CLICK_SOUND,
                Assets.Sounds.CLICK_2_SOUND,
                Assets.Sounds.WHITE_MOVE_1_SOUND,
                Assets.Sounds.WHITE_MOVE_2_SOUND,
                Assets.Sounds.BLACK_MOVE_1_SOUND,
                Assets.Sounds.BLACK_MOVE_2_SOUND,
                Assets.Sounds.UNDO_SOUND,
                Assets.Sounds.CAPTURE_1_SOUND,
                Assets.Sounds.CAPTURE_2_SOUND,
                Assets.Sounds.CAPTURE_3_SOUND,
                Assets.Sounds.KING_WIN_SOUND,
                Assets.Sounds.KING_LOSE_SOUND);

        graphicsService.loadSkins(Assets.Skin.UI_SKIN);

        graphicsService.loadTextureAtlases(
                Assets.Graphics.PIECE_ATLAS,
                Assets.Graphics.EXPLOSION_ATLAS,
                Assets.Graphics.BOARD_ATLAS,
                Assets.Graphics.SPLASH_ATLAS,
                Assets.Graphics.MENU_ATLAS);


        graphicsService.loadFonts(Assets.Fonts.GOTHAM_MEDIUM_PHONE,
                Assets.Fonts.GOTHAM_MEDIUM_TABLET);

        localeService.load(
                Assets.Locales.DEFAULT_LOCALE,
                Assets.Locales.ADDITIONAL_LOCALES);

        levelService.load();
    }
}
