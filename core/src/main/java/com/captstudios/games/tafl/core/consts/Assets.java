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

    public static final class Graphics {
        private Graphics() {
        }

        public static final String ATLAS_BACKGROUNDS = "image-atlases/backgrounds.atlas";
        public static final String ATLAS_IOS = "image-atlases/ios.atlas";
        public static final String ATLAS_PIECES = "image-atlases/pieces.atlas";

        public static final String BOARD = "tafl-board";
        public static final String SPLASH = "tafl-splash";
        public static final String COMPANY_LOGO = "captstudios_logo-800x800";
        public static final String MENU = "tafl-menu-background";

        public static final String INSTRUCTIONS_1 = "tafl-instr-screen01";
        public static final String INSTRUCTIONS_2 = "tafl-instr-screen02";
        public static final String INSTRUCTIONS_3 = "tafl-instr-screen03";
        public static final String INSTRUCTIONS_4 = "tafl-instr-screen04";
        public static final String INSTRUCTIONS_5 = "tafl-instr-screen05";
        public static final String INSTRUCTIONS_6 = "tafl-instr-screen06";

        public static final String GRID = "tafl-grid-11x11";

        public static final String WHITE_PIECE = "tafl-piece-light";
        public static final String KING_PIECE = "tafl-piece-king";
        public static final String BLACK_PIECE = "tafl-piece-dark";

        public static final String BLACK_ICON = "tafl-piece-dark-icon";
        public static final String WHITE_ICON = "tafl-piece-light-icon";

        public static final String MENU_ICON = "tafl-menu-icon-mainmenu";
        public static final String UNDO_ICON = "tafl-menu-icon-undo";
        public static final String CLOSE_ICON = "tafl-menu-icon-close";

        public static final String EXPLOSION_ATLAS = "image-atlases/explosion.atlas";
        public static final String EXPLOSION = "explosion";
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
        public static final FontDefinition FONT_DESTKOP_MENU = new FontDefinition("skin/Gotham-Medium.otf", 32);
        public static final FontDefinition FONT_DESTKOP_SCREEN_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 40);
        public static final FontDefinition FONT_DESTKOP_GAME = new FontDefinition("skin/Gotham-Medium.otf", 24);
        public static final FontDefinition FONT_DESTKOP_DIALOG = new FontDefinition("skin/Gotham-Medium.otf", 32);
        public static final FontDefinition FONT_DESTKOP_RULES = new FontDefinition("skin/Gotham-Medium.otf", 16);

        public static final FontDefinition FONT_LDPI_MENU = new FontDefinition("skin/Gotham-Medium.otf", 32);
        public static final FontDefinition FONT_LDPI_SCREEN_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 40);
        public static final FontDefinition FONT_LDPI_GAME = new FontDefinition("skin/Gotham-Medium.otf", 24);
        public static final FontDefinition FONT_LDPI_DIALOG = new FontDefinition("skin/Gotham-Medium.otf", 32);
        public static final FontDefinition FONT_LDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 16);

        public static final FontDefinition FONT_MDPI_MENU = new FontDefinition("skin/Gotham-Medium.otf", 48);
        public static final FontDefinition FONT_MDPI_SCREEN_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 72);
        public static final FontDefinition FONT_MDPI_GAME = new FontDefinition("skin/Gotham-Medium.otf", 32);
        public static final FontDefinition FONT_MDPI_DIALOG = new FontDefinition("skin/Gotham-Medium.otf", 40);
        public static final FontDefinition FONT_MDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 24);

        public static final FontDefinition FONT_HDPI_MENU = new FontDefinition("skin/Gotham-Medium.otf", 72);
        public static final FontDefinition FONT_HDPI_SCREEN_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 96);
        public static final FontDefinition FONT_HDPI_GAME = new FontDefinition("skin/Gotham-Medium.otf", 48);
        public static final FontDefinition FONT_HDPI_DIALOG = new FontDefinition("skin/Gotham-Medium.otf", 64);
        public static final FontDefinition FONT_HDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 36);

        public static final FontDefinition FONT_XHDPI_MENU = new FontDefinition("skin/Gotham-Medium.otf", 96);
        public static final FontDefinition FONT_XHDPI_SCREEN_TITLE = new FontDefinition("skin/Gotham-Medium.otf", 128);
        public static final FontDefinition FONT_XHDPI_GAME = new FontDefinition("skin/Gotham-Medium.otf", 64);
        public static final FontDefinition FONT_XHDPI_DIALOG = new FontDefinition("skin/Gotham-Medium.otf", 80);
        public static final FontDefinition FONT_XHDPI_RULES = new FontDefinition("skin/Gotham-Medium.otf", 48);
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
                game.deviceSettings.backgroundAtlas,
                Assets.Graphics.ATLAS_PIECES,
                Assets.Graphics.EXPLOSION_ATLAS);

        graphicsService.loadFonts(Assets.Fonts.FONT_DESTKOP_MENU,
                Assets.Fonts.FONT_DESTKOP_SCREEN_TITLE,
                Assets.Fonts.FONT_DESTKOP_GAME,
                Assets.Fonts.FONT_DESTKOP_DIALOG,
                Assets.Fonts.FONT_DESTKOP_RULES,
                Assets.Fonts.FONT_LDPI_MENU,
                Assets.Fonts.FONT_LDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_LDPI_GAME,
                Assets.Fonts.FONT_LDPI_DIALOG,
                Assets.Fonts.FONT_LDPI_RULES,
                Assets.Fonts.FONT_MDPI_MENU,
                Assets.Fonts.FONT_MDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_MDPI_GAME,
                Assets.Fonts.FONT_MDPI_DIALOG,
                Assets.Fonts.FONT_MDPI_RULES,
                Assets.Fonts.FONT_HDPI_MENU,
                Assets.Fonts.FONT_HDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_HDPI_GAME,
                Assets.Fonts.FONT_HDPI_DIALOG,
                Assets.Fonts.FONT_HDPI_RULES,
                Assets.Fonts.FONT_XHDPI_MENU,
                Assets.Fonts.FONT_XHDPI_SCREEN_TITLE,
                Assets.Fonts.FONT_XHDPI_GAME,
                Assets.Fonts.FONT_XHDPI_DIALOG,
                Assets.Fonts.FONT_XHDPI_RULES);

        localeService.load(
                Assets.Locales.DEFAULT_LOCALE,
                Assets.Locales.ADDITIONAL_LOCALES);

        levelService.load();
    }
}
