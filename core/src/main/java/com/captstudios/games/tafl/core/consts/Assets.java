package com.captstudios.games.tafl.core.consts;

import java.util.Locale;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Logger;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.level.TaflLevelService;
import com.roundtriangles.games.zaria.services.GraphicsService;
import com.roundtriangles.games.zaria.services.SoundService;
import com.roundtriangles.games.zaria.services.resources.LocaleService;
import com.roundtriangles.games.zaria.services.utils.FontDefinition;
import com.roundtriangles.games.zaria.services.utils.GameAssetLoader;
import com.roundtriangles.games.zaria.utils.CustomSkinLoader;

public class Assets extends GameAssetLoader {

    public static final class GraphicFiles {
        private GraphicFiles() {
        }
        public static final String ATLAS_SPLASH = "image-atlases/splash.atlas";
        public static final String ATLAS_BACKGROUNDS = "image-atlases/backgrounds.atlas";
        public static final String ATLAS_PIECES = "image-atlases/pieces.atlas";
        public static final String CORNER = "image-atlases/tafl-splash-cornerpiece.png";
    }

    public static final class TextGraphics {
        private TextGraphics() {
        }

        public static final String START = "tafl-main-text-start";
        public static final String RESUME = "tafl-main-text-resume";
        public static final String HELP = "tafl-main-text-help";

        public static final String NOVICE = "tafl-menu-settings-text-novice";
        public static final String BEGINNER = "tafl-menu-settings-text-beginner";
        public static final String INTERMEDIATE = "tafl-menu-settings-text-intermediate";
        public static final String ADVANCED = "tafl-menu-settings-text-advance";

        public static final String LEVEL = "tafl-main-text-level";
        //public static final String DIFFICULTY = "tafl-menu-settings-text-difficulty";
        public static final String MUSIC = "tafl-menu-settings-text-music";

        public static final String OFF = "tafl-menu-settings-text-off";
        public static final String ON = "tafl-menu-settings-text-on";

        public static final String PLAY_AS = "tafl-menu-start-text-playas";
        public static final String PLAY_BLACK = "tafl-menu-start-text-red";
        public static final String PLAY_WHITE = "tafl-menu-start-text-white";
        public static final String SIZE = "tafl-menu-start-text-size";
        public static final String SIZE_9 = "tafl-menu-start-text-size9";
        public static final String SIZE_11 = "tafl-menu-start-text-size11";
    }

    public static final class ButtonGraphics {
        public static final String BLANK = "tafl-main-button";
        public static final String BLANK_PRESSED = "tafl-main-button-pressed";

        public static final String DIFF_BLANK = "tafl-menu-settings-button-diff";
        public static final String DIFF_PRESSED = "tafl-menu-settings-button-diff-pressed";

        public static final String ON_OFF_BLANK = "tafl-menu-settings-button-onoff";
        public static final String ON_OFF_PRESSED = "tafl-menu-settings-button-onoff-pressed";

        public static final String PLAY_AS_BLANK = "tafl-menu-start-button-playas";
        public static final String PLAY_AS_PRESSED = "tafl-menu-start-button-playas-pressed";

        public static final String SIZE_BLANK = "tafl-menu-start-button-size";
        public static final String SIZE_PRESSED = "tafl-menu-start-button-size-pressed";
    }

    public static final class GameGraphics {
        private GameGraphics() {
        }

        public static final String GRID_9 = "tafl-grid-9x9";
        public static final String GRID_11 = "tafl-grid-11x11";

        public static final String WHITE_PIECE = "tafl-piece-light";
        public static final String WHITE_PIECE_CAPTURE = "tafl-piece-light-capture";

        public static final String KING_PIECE = "tafl-piece-king";
        public static final String KING_PIECE_CAPTURE = "tafl-piece-king-capture";

        public static final String BLACK_PIECE = "tafl-piece-dark";
        public static final String BLACK_PIECE_CAPTURE = "tafl-piece-dark-capture";
    }

    public static final class Backgrounds {
        private Backgrounds() {
        }

        public static final String BOARD = "tafl-board";
        public static final String SPLASH = "tafl-splash";
        public static final String COMPANY_LOGO = "company-logo";
        public static final String MENU = "tafl-main";

        public static final String INSTRUCTIONS_1 = "tafl-instr-screen01";
        public static final String INSTRUCTIONS_2 = "tafl-instr-screen02";
        public static final String INSTRUCTIONS_3 = "tafl-instr-screen03";
        public static final String INSTRUCTIONS_4 = "tafl-instr-screen04";
    }

    public static final class Decorations {
        private Decorations() {
        }

        public static final String BRAID = "tafl-braid-bottom";
        public static final String HALF_BRAID = "tafl-braid-half";
        //public static final String HALF_BRAID = "";
    }

    public static final class Icons {
        private Icons() {
        }

        public static final String BLACK = "tafl-piece-dark-icon";
        public static final String WHITE = "tafl-piece-light-icon";

        public static final String MENU = "tafl-menu-icon-mainmenu";
        public static final String UNDO = "tafl-menu-icon-undo";
        public static final String MUTE = "tafl-menu-icon-mute";
        public static final String UN_MUTE = "tafl-menu-icon-unmute";
        public static final String CLOSE = "tafl-menu-icon-close";
        public static final String HINT = "tafl-menu-icon-hint";
        public static final String SETTINGS = "tafl-menu-icon-settings";
        public static final String BACK = "tafl-menu-icon-back";
        public static final String PLAY = "tafl-menu-icon-play";
        public static final String ABOUT = "tafl-menu-icon-about";
    }

    public static final class Skin {
        private Skin() {
        }
        public static final String UI_SKIN = "skin/uiskin.json";

        public static final String SKIN_STYLE_MENU = "menu";
        public static final String SKIN_STYLE_SCREEN_TITLE = "screen-title";
        public static final String SKIN_STYLE_GAME = "game";
        public static final String SKIN_STYLE_DIALOG = "dialog";
        public static final String SKIN_STYLE_PLAYER_TAG = "player-tag";
        public static final String SKIN_STYLE_RULES= "rules";
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
        public static final FontDefinition FONT_DESTKOP_MENU = new FontDefinition("skin/huggles.ttf", 32);
        public static final FontDefinition FONT_DESKTOP_SCREEN_TITLE = new FontDefinition("skin/huggles.ttf", 40);
        public static final FontDefinition FONT_DESKTOP_GAME = new FontDefinition("skin/huggles.ttf", 64);
        public static final FontDefinition FONT_DESKTOP_HUD = new FontDefinition("skin/huggles.ttf", 32);
        public static final FontDefinition FONT_DESKTOP_RULES = new FontDefinition("skin/Gotham-Medium.otf", 18);
        public static final FontDefinition FONT_DESKTOP_RULES_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 28);

        public static final FontDefinition FONT_LDPI_MENU = new FontDefinition("skin/huggles.ttf", 32);
        public static final FontDefinition FONT_LDPI_SCREEN_TITLE = new FontDefinition("skin/huggles.ttf", 40);
        public static final FontDefinition FONT_LDPI_GAME = new FontDefinition("skin/huggles.ttf", 64);
        public static final FontDefinition FONT_LDPI_HUD = new FontDefinition("skin/huggles.ttf", 32);
        public static final FontDefinition FONT_LDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 18);
        public static final FontDefinition FONT_LDPI_RULES_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 28);

        public static final FontDefinition FONT_MDPI_MENU = new FontDefinition("skin/huggles.ttf", 48);
        public static final FontDefinition FONT_MDPI_SCREEN_TITLE = new FontDefinition("skin/huggles.ttf", 72);
        public static final FontDefinition FONT_MDPI_GAME = new FontDefinition("skin/huggles.ttf", 64);
        public static final FontDefinition FONT_MDPI_HUD = new FontDefinition("skin/huggles.ttf", 48);
        public static final FontDefinition FONT_MDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 24);
        public static final FontDefinition FONT_MDPI_RULES_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 42);

        public static final FontDefinition FONT_HDPI_MENU = new FontDefinition("skin/huggles.ttf", 72);
        public static final FontDefinition FONT_HDPI_SCREEN_TITLE = new FontDefinition("skin/huggles.ttf", 96);
        public static final FontDefinition FONT_HDPI_GAME = new FontDefinition("skin/huggles.ttf", 64);
        public static final FontDefinition FONT_HDPI_HUD = new FontDefinition("skin/huggles.ttf", 64);
        public static final FontDefinition FONT_HDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 36);
        public static final FontDefinition FONT_HDPI_RULES_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 54);

        public static final FontDefinition FONT_XHDPI_MENU = new FontDefinition("skin/huggles.ttf", 96);
        public static final FontDefinition FONT_XHDPI_SCREEN_TITLE = new FontDefinition("skin/huggles.ttf", 128);
        public static final FontDefinition FONT_XHDPI_GAME = new FontDefinition("skin/huggles.ttf", 64);
        public static final FontDefinition FONT_XHDPI_HUD = new FontDefinition("skin/huggles.ttf", 96);
        public static final FontDefinition FONT_XHDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 48);
        public static final FontDefinition FONT_XHDPI_RULES_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 84);
    }

    protected SoundService soundService;
    protected LocaleService localeService;
    protected GraphicsService graphicsService;
    protected TaflLevelService levelService;
    protected TaflGame game;

    public Assets(SoundService soundService,
            GraphicsService graphicsService,
            LocaleService localeService,
            TaflLevelService levelService,
            TaflGame game) {
        super(soundService, graphicsService, localeService, levelService, game);

        this.soundService = soundService;
        this.graphicsService = graphicsService;
        this.localeService = localeService;
        this.levelService = levelService;
        this.game = game;

        assetManager.setLoader(com.badlogic.gdx.scenes.scene2d.ui.Skin.class,
                new CustomSkinLoader(new InternalFileHandleResolver()));

        if (Constants.GameConstants.DEBUG) {
            assetManager.getLogger().setLevel(Logger.DEBUG);
        }
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
                Assets.GraphicFiles.ATLAS_BACKGROUNDS,
                Assets.GraphicFiles.ATLAS_PIECES);

        graphicsService.loadFonts(Assets.Fonts.FONT_DESTKOP_MENU,
                Assets.Fonts.FONT_DESKTOP_SCREEN_TITLE,
                Assets.Fonts.FONT_DESKTOP_GAME,
                Assets.Fonts.FONT_DESKTOP_HUD,
                Assets.Fonts.FONT_DESKTOP_RULES,
                Assets.Fonts.FONT_DESKTOP_RULES_TITLE,
                Assets.Fonts.FONT_LDPI_MENU,
                Assets.Fonts.FONT_LDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_LDPI_GAME,
                Assets.Fonts.FONT_LDPI_HUD,
                Assets.Fonts.FONT_LDPI_RULES,
                Assets.Fonts.FONT_LDPI_RULES_TITLE,
                Assets.Fonts.FONT_MDPI_MENU,
                Assets.Fonts.FONT_MDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_MDPI_GAME,
                Assets.Fonts.FONT_MDPI_HUD,
                Assets.Fonts.FONT_MDPI_RULES,
                Assets.Fonts.FONT_MDPI_RULES_TITLE,
                Assets.Fonts.FONT_HDPI_MENU,
                Assets.Fonts.FONT_HDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_HDPI_GAME,
                Assets.Fonts.FONT_HDPI_HUD,
                Assets.Fonts.FONT_HDPI_RULES,
                Assets.Fonts.FONT_HDPI_RULES_TITLE,
                Assets.Fonts.FONT_XHDPI_MENU,
                Assets.Fonts.FONT_XHDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_XHDPI_GAME,
                Assets.Fonts.FONT_XHDPI_HUD,
                Assets.Fonts.FONT_XHDPI_RULES,
                Assets.Fonts.FONT_XHDPI_RULES_TITLE);

        localeService.load(
                Assets.Locales.DEFAULT_LOCALE,
                Assets.Locales.ADDITIONAL_LOCALES);

        levelService.load();
    }
}
