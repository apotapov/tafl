package com.pactstudios.games.tafl.core.consts;

import com.badlogic.gdx.graphics.Color;
import com.pactstudios.games.tafl.core.enums.AiType;


public final class Constants {

    private Constants() {
    }

    public static final class GameConstants {
        private GameConstants() {
        }
        public static final boolean DEBUG = false;
        public static final int BATCH_SIZE = 100;
        public static final int GAME_WIDTH = 600;
        public static final int GAME_HALF_WIDTH = GAME_WIDTH / 2;
        public static final int GAME_HEIGHT = 1000;
        public static final int GAME_HALF_HEIGHT = GAME_HEIGHT / 2;
        public static final boolean USE_GL20 = false;

        public static final int DEFAULT_LEVEL_INDEX = 0;
        public static final boolean DEFAULT_VERSUS_COMPUTER = true;
        public static final boolean DEFAULT_COMPUTER_STARTS = true;

        public static final int DRAW_MOVE_THRESHHOLD = 100;
        public static final int DRAW_BOARD_REPETITION_THRESHHOLD = 3;
        public static final int DRAW_MOVES_TO_CHECK = 4;

        public static final int KING_CAPTURE_THRESHHOLD = 3;
        public static final int RAICHI_THRESHHOLD = 1;
        public static final int TUICHI_THRESHHOLD = 2;
    }

    public static final class AiConstants {
        public static final int MAX_TREE_DEPTH = 4;
        public static final int WIN = 200;
        public static final int LOSS = -WIN;

        public static final Color LOADING_PROMP_COLOR = new Color(0f, 0f, 0f, 0.4f);
        public static final float LOADING_PROMPT_WIDTH = 200;
        public static final float LOADING_PROMPT_HEIGHT = 60;

        public static final int TABLE_SIZE = 1048576; // 1m
        public static final AiType DEFAULT_AI_TYPE = AiType.AI_RANDOM;
    }

    public static final class ScreenConstants {
        private ScreenConstants() {
        }

        public static final int BUTTON_WIDTH = 250;
        public static final int BUTTON_HEIGHT = 70;

        public static final int LIST_WIDTH = 270;
        public static final int LIST_HEIGHT = 170;

        public static final float FADE_TIME = 1f;
        public static final float DISPLAY_TIME = 1.75f;

        public static final int SPACING = 30;
    }

    public static final class GroupConstants {
        private GroupConstants(){
        }

        public static final String HIGHLIGHTED_CELLS = "hcells";

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

        public static final float SLOW_DOWN_DISTANCE = BoardRenderConstants.TILE_SIZE;
        public static final int PIECE_SPEED = 200;

        public static final Color PATH_COLOR = new Color(0.4f, 0.25f, .25f, 1f);
        public static final Color END_COLOR = new Color(0.25f, 0.25f, .25f, 1f);
    }

    public static final class BoardConstants {
        private BoardConstants() {
        }

        public static final int STANDARD_BOARD_DIMENSION = 11;
        public static final int STANDARD_BOARD_NUMBER_CELLS =
                STANDARD_BOARD_DIMENSION * STANDARD_BOARD_DIMENSION;

        public static final int SMALL_BOARD_DIMENSION = 9;
        public static final int SMALL_BOARD_NUMBER_CELLS =
                SMALL_BOARD_DIMENSION * SMALL_BOARD_DIMENSION;

        public static final int BIGGEST_BOARD_DIMENSION = STANDARD_BOARD_DIMENSION;
        public static final int BIGGEST_BOARD_NUMBER_CELLS =
                BIGGEST_BOARD_DIMENSION * BIGGEST_BOARD_DIMENSION;

        public static final int ILLEGAL_CELL = -1;

        public static final int WHITE_TEAM = 0;
        public static final int BLACK_TEAM = 1;
        public static final int NO_TEAM = -1;
    }

    public static final class BoardRenderConstants {
        private BoardRenderConstants() {
        }
        public static final int TILE_SIZE = 51;
        public static final int HALF_TILE_SIZE = TILE_SIZE / 2;
        public static final int QUARTER_TILE_SIZE = HALF_TILE_SIZE / 2;

        public static final int BOARD_FRAME_WIDTH = 20;

        public static final Color BACKGROUND_COLOR = new Color(0.5f, .25f, .25f, 1f);
        public static final Color HIGHLIGHT_COLOR = new Color(1f, 1f, 1f, 0.25f);
        public static final Color LINE_COLOR = new Color(0, 0, 0, 1f);

        public static final Color BORDER_COLOR = new Color(0.5f, 0f, 0f, 1f);
        public static final Color CASTLE_COLOR = new Color(0.4f, 0f, 0f, 1f);
        public static final Color CORNER_COLOR = new Color(0.4f, 0f, 0f, 1f);

        public static final float FONT_WEIRDNESS_OFFSET = 2;

        public static final float HORIZONTAL_CELL_ID_HORIZONTAL_OFFSET = HALF_TILE_SIZE;
        public static final float HORIZONTAL_CELL_ID_BOTTOM_OFFSET =
                -BOARD_FRAME_WIDTH / 2 + FONT_WEIRDNESS_OFFSET;
        public static final float HORIZONTAL_CELL_ID_TOP_OFFSET =
                HALF_TILE_SIZE * 3f - BOARD_FRAME_WIDTH / 2 + FONT_WEIRDNESS_OFFSET;

        public static final float VERTICAL_CELL_ID_VERTICAL_OFFSET =
                HALF_TILE_SIZE + FONT_WEIRDNESS_OFFSET;
        public static final float VERTICAL_CELL_ID_LEFT_OFFSET =
                -QUARTER_TILE_SIZE;
        public static final float VERTICAL_CELL_ID_RIGHT_OFFSET =
                TILE_SIZE + QUARTER_TILE_SIZE;

        public static final int BOARD_HORIZONTAL_OFFSET = BOARD_FRAME_WIDTH -
                GameConstants.GAME_HALF_WIDTH;
        public static final int BOARD_VERTICAL_OFFSET = BOARD_FRAME_WIDTH -
                GameConstants.GAME_HALF_HEIGHT + 201;

        public static final Color TURN_HIGHLIGHT_COLOR = new Color(0, 0, 0, 0.1f);
        public static final int TURN_HIGHLIGHT_WIDTH = 170;
        public static final int TURN_HIGHLIGHT_HEIGHT = 50;
        public static final int TURN_HIGHLIGHT_BLACK_X = - GameConstants.GAME_WIDTH / 2 + 10;
        public static final int TURN_HIGHLIGHT_WHITE_X = GameConstants.GAME_WIDTH / 2 - TURN_HIGHLIGHT_WIDTH - 10;
        public static final int TURN_HIGHLIGHT_Y = 345;
    }

    public static final class HudConstants {
        private HudConstants() {
        }
        public static final int HUD_HEIGHT = 100;
        public static final Color HUD_BACKGROUND_COLOR = new Color(0, 0, 0, 0.5f);

        public static final int HUD_BUTTON_WIDTH = 30;
        public static final int HUD_BUTTON_HEIGHT = 30;

        public static final float HUD_TABLE_PADDING_TOP = 10f;
        public static final float HUD_TABLE_PADDING_SIDES = 15f;

        public static final float PLAYER_LABEL_PAD_TOP = 50f;
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

        public static final int CACHE_SIZE = 1000;
    }
}
