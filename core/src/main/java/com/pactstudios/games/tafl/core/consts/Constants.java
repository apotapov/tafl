package com.pactstudios.games.tafl.core.consts;

import com.badlogic.gdx.graphics.Color;


public final class Constants {

    private Constants() {
    }

    public static final class ScreenConstants {
        private ScreenConstants() {
        }

        public static final int BUTTON_WIDTH = 300;
        public static final int BUTTON_HEIGHT = 60;

        public static final int LIST_WIDTH = 200;
        public static final int LIST_HEIGHT = 200;

        public static final float FADE_TIME = 1f;
        public static final float DISPLAY_TIME = 1.75f;
    }

    public static final class GroupConstants {
        private GroupConstants(){
        }

        public static final String HIGHLIGHTED_CELLS = "hcells";
        public static final String WHITE = "white";
        public static final String BLACK = "black";

        public static final String BOARD = "board";
        public static final String RENDERER = "renderer";
        public static final String HUD = "hud";
    }

    public static final class PieceConstants {
        private PieceConstants() {
        }
        public static final float SCALING = 2f;

        public static final float FRAME_DURATION = 0.2f;
        public static final float EXPLOSION_FRAME_DURATION = 0.05f;
    }

    public static final class BoardConstants {
        private BoardConstants() {
        }
        public static final int TILE_SIZE = 64;
        public static final int HALF_TILE_SIZE = TILE_SIZE / 2;

        public static final Color BACKGROUND_COLOR = new Color(0.5f, .25f, .25f, 1f);
        public static final Color HIGHLIGHT_COLOR = new Color(1f, 1f, 1f, 0.25f);
    }

    public static final class HudConstants {
        private HudConstants() {
        }
        public static final int HUD_HEIGHT = 100;
        public static final Color HUD_BACKGROUND_COLOR = new Color(0, 0, 0, 0.5f);

        public static final int BUTTON_WIDTH = 100;
        public static final int BUTTON_HEIGHT = 100;
    }

    public static final class GameConstants {
        private GameConstants() {
        }
        public static final boolean DEBUG = false;
        public static final int BATCH_SIZE = 100;
        public static final int GAME_WIDTH = 600;
        public static final int GAME_HEIGHT = 1000;
        public static final boolean USE_GL20 = false;
    }

    public static final class DbConstants {
        private DbConstants() {
        }
        public static final String DB_URL_PREFIX = "jdbc:sqlite:";
        public static final String SQLITE_DB_NAME = "tafl";
        public static final String SQLITE_DB_FILE = SQLITE_DB_NAME + ".db";

        public static final int CURRENT_DB_VERSION = 0;

        public static final String MATCH_TABLE = "matches";
        public static final String PIECE_TABLE = "pieces";
        public static final String LOG_TABLE = "log";

        public static final int MATCH_TO_RESUME = 1;
    }
}
