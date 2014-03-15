package com.captstudios.games.tafl.core.consts;

import com.badlogic.gdx.graphics.Color;
import com.captstudios.games.tafl.core.enums.AiType;


public final class Constants {

    private Constants() {
    }

    public static final class GameConstants {
        private GameConstants() {
        }
        public static final boolean DEBUG = false;
        public static final int BATCH_SIZE = 100;
        public static final int GAME_WIDTH = 1080;
        public static final int GAME_HALF_WIDTH = GAME_WIDTH / 2;
        public static final int GAME_HEIGHT = 1920;
        public static final int GAME_HALF_HEIGHT = GAME_HEIGHT / 2;

        public static final int DEFAULT_LEVEL_INDEX = 0;
        public static final boolean DEFAULT_COMPUTER_STARTS = true;
        public static final boolean DEFAULT_SHOW_HELP_ON_START = true;
        public static final float DEFAULT_VOLUME = 0.5f;

        public static final int DRAW_MOVE_THRESHHOLD = 300;
        public static final int DRAW_BOARD_REPETITION_THRESHHOLD = 3;
        public static final int DRAW_MOVES_TO_CHECK = 4;

        public static final int KING_CAPTURE_THRESHHOLD = 3;
        public static final int RAICHI_THRESHHOLD = 1;
        public static final int TUICHI_THRESHHOLD = 2;

        public static final int GAME_DESKTOP_HEIGHT = 800;
        public static final int GAME_DESKTOP_WIDTH = 500; //GAME_DESKTOP_HEIGHT * GAME_WIDTH / Constants.GameConstants.GAME_HEIGHT;

        public static final int GAME_HTML_HEIGHT = 400;
        public static final int GAME_HTML_WIDTH = 250; //GAME_DESKTOP_HEIGHT * GAME_WIDTH / Constants.GameConstants.GAME_HEIGHT;

        public static final int CAPTURE_VIBRATION_LENGTH = 500;
        public static final int GAME_OVER_VIBRATION_LENGTH = 1000;

        public static final float DRAG_THRESHOLD = 0; //0.1f;
        public static final int MAX_NUMBER_OF_MOVES = 11 * 11 * 20;
    }

    public static final class AiConstants {
        public static final int BEGINNER_TREE_DEPTH_11 = 1;
        public static final int INTERMEDIATE_TREE_DEPTH_11 = 2;
        public static final int ADVANCED_TREE_DEPTH_11 = 3;

        public static final int BEGINNER_TREE_DEPTH_9 = 1;
        public static final int INTERMEDIATE_TREE_DEPTH_9 = 2;
        public static final int ADVANCED_TREE_DEPTH_9 = 3;

        public static final Color LOADING_PROMP_COLOR = new Color(0f, 0f, 0f, 0.4f);

        public static final int TABLE_SIZE = 2097152; //4194304; //2097152; //1048576; // 1m
        public static final AiType DEFAULT_AI_TYPE = AiType.AI_BEGINNER;

        public static final int TRANSPOSITION_TABLE_SIZE = 1048576;
        public static final int OPENING_BOOK_SIZE = 1024;

        public static final int WIN = 100000;
        public static final int LOSS = -100000;
        public static final float AI_THINKING_ANIMATION = 0.5f;
    }

    public static final class ScreenConstants {
        private ScreenConstants() {
        }

        public static final float FADE_TIME = 0.5f;
        public static final float LOADING_SCREEN_DISPLAY_TIME = 1.75f;
        public static final float COMPANY_SCREEN_DISPLAY_TIME = 1f;

        public static final Color ABOUT_COLOR = new Color(1, 1, 1, 0.2f);
        public static final Color TEXT_COLOR = new Color(0.9453125f, 0.9453125f, 0.9453125f, 1);

        public static final int DESKTOP_LABEL_HEIGHT = 40;
        public static final int DESKTOP_BUTTON_WIDTH = 250;
        public static final int DESKTOP_BUTTON_HEIGHT = 70;
        public static final int DESKTOP_SPACING = 30;
        public static final int DESKTOP_DIALOG_BUTTON_WIDTH = 200;
        public static final int DESKTOP_DIALOG_BUTTON_HEIGHT = 70;
        public static final int DESKTOP_DIALOG_BUTTON_SPACING = 20;
        public static final int DESKTOP_HUD_BUTTON_WIDTH = 45;
        public static final int DESKTOP_HUD_BUTTON_HEIGHT = 45;
        public static final int DESKTOP_HUD_TABLE_PADDING = 10;
        public static final int DESKTOP_PLAYER_LABEL_PAD_TOP = 40;

        public static final int LDPI_MIN_WIDTH = 0;
        public static final int LDPI_LABEL_HEIGHT = 40;
        public static final int LDPI_BUTTON_WIDTH = 250;
        public static final int LDPI_BUTTON_HEIGHT = 70;
        public static final int LDPI_SPACING = 30;
        public static final int LDPI_DIALOG_BUTTON_WIDTH = 200;
        public static final int LDPI_DIALOG_BUTTON_HEIGHT = 70;
        public static final int LDPI_DIALOG_BUTTON_SPACING = 20;
        public static final int LDPI_HUD_BUTTON_WIDTH = 45;
        public static final int LDPI_HUD_BUTTON_HEIGHT = 45;
        public static final int LDPI_HUD_TABLE_PADDING = 10;
        public static final int LDPI_PLAYER_LABEL_PAD_TOP = 40;

        public static final int MDPI_MIN_WIDTH = 500;
        public static final int MDPI_LABEL_HEIGHT = 60;
        public static final int MDPI_BUTTON_WIDTH = 375;
        public static final int MDPI_BUTTON_HEIGHT = 105;
        public static final int MDPI_SPACING = 45;
        public static final int MDPI_DIALOG_BUTTON_WIDTH = 300;
        public static final int MDPI_DIALOG_BUTTON_HEIGHT = 105;
        public static final int MDPI_DIALOG_BUTTON_SPACING = 30;
        public static final int MDPI_HUD_BUTTON_WIDTH = 90;
        public static final int MDPI_HUD_BUTTON_HEIGHT = 90;
        public static final int MDPI_HUD_TABLE_PADDING = 15;
        public static final int MDPI_PLAYER_LABEL_PAD_TOP = 60;

        public static final int HDPI_MIN_WIDTH = 900;
        public static final int HDPI_LABEL_HEIGHT = 80;
        public static final int HDPI_BUTTON_WIDTH = 500;
        public static final int HDPI_BUTTON_HEIGHT = 140;
        public static final int HDPI_SPACING = 60;
        public static final int HDPI_DIALOG_BUTTON_WIDTH = 400;
        public static final int HDPI_DIALOG_BUTTON_HEIGHT = 140;
        public static final int HDPI_DIALOG_BUTTON_SPACING = 40;
        public static final int HDPI_HUD_BUTTON_WIDTH = 135;
        public static final int HDPI_HUD_BUTTON_HEIGHT = 135;
        public static final int HDPI_HUD_TABLE_PADDING = 20;
        public static final int HDPI_PLAYER_LABEL_PAD_TOP = 80;

        public static final int XHDPI_MIN_WIDTH = 1200;
        public static final int XHDPI_LABEL_HEIGHT = 120;
        public static final int XHDPI_BUTTON_WIDTH = 750;
        public static final int XHDPI_BUTTON_HEIGHT = 210;
        public static final int XHDPI_SPACING = 90;
        public static final int XHDPI_DIALOG_BUTTON_WIDTH = 600;
        public static final int XHDPI_DIALOG_BUTTON_HEIGHT = 210;
        public static final int XHDPI_DIALOG_BUTTON_SPACING = 40;
        public static final int XHDPI_HUD_BUTTON_WIDTH = 180;
        public static final int XHDPI_HUD_BUTTON_HEIGHT = 180;
        public static final int XHDPI_HUD_TABLE_PADDING = 30;
        public static final int XHDPI_PLAYER_LABEL_PAD_TOP = 120;


        public static final float INSTRUCTION_CLOSE_BUTTON_X_OFFSET = 180;
        public static final float INSTRUCTION_CLOSE_BUTTON_Y_OFFSET = 180;
        public static final float INSTRUCTION_SLIDE_DURATION = .75f;
        public static final float INSTRUCTION_SLIDE_BACK_DURATION = INSTRUCTION_SLIDE_DURATION / 2;

    }

    public static final class PieceConstants {
        private PieceConstants() {
        }

        public static final float CAPTURE_FRAME_DURATION = .1f;
        public static final int PIECE_SPEED = 400;

        public static final float TILE_SIZE_11 = 95.8f;
        public static final float BOARD_FRAME_WIDTH_11 = 15;

        public static final float CELL_HORIZONTAL_OFFSET_11 = BOARD_FRAME_WIDTH_11 -
                Constants.GameConstants.GAME_HALF_WIDTH;
        public static final float CELL_VERTICAL_OFFSET_11 = 434 -
                Constants.GameConstants.GAME_HALF_HEIGHT ;

        public static final int[] CORNER_VALUE_CELLS_11 = {
            2, 12, 22,
            6, 18, 32,
            88, 100, 112,
            98, 108, 118
        };

        public static final float TILE_SIZE_9 = 117f;
        public static final float BOARD_FRAME_WIDTH_9 = 15;

        public static final float CELL_HORIZONTAL_OFFSET_9 = BOARD_FRAME_WIDTH_9 -
                Constants.GameConstants.GAME_HALF_WIDTH;
        public static final float CELL_VERTICAL_OFFSET_9 = 434 -
                Constants.GameConstants.GAME_HALF_HEIGHT;

        public static final int[] CORNER_VALUE_CELLS_9 = {
            2, 12, 22,
            6, 16, 26,
            54, 64, 74,
            78, 70, 62
        };
        public static final float SCALING_11 = TILE_SIZE_11 / TILE_SIZE_9;
    }

    public static final class BoardConstants {
        private BoardConstants() {
        }

        public static final int ILLEGAL_CELL = -1;

        public static final int PIECE_TYPES = 3;
        public static final int WHITE_TEAM = 0;
        public static final int BLACK_TEAM = 1;
        public static final int KING = 2;
        public static final int NO_TEAM = -1;

        public static final char EMPTY_CELL = '.';
        public static final char WHITE_PIECE = 'W';
        public static final char BLACK_PIECE = 'B';
        public static final char KING_PIECE = 'K';
    }

    public static final class BoardRenderConstants {
        private BoardRenderConstants() {
        }

        public static final Color HIGHLIGHT_COLOR = new Color(0.58203125f, 0.3671875f, 0.10546875f, 0.6f);
        public static final Color SPECIAL_HIGHLIGHT_COLOR = new Color(1f, 0f, 0f, 0.4f);

        public static final Color PATH_COLOR = HIGHLIGHT_COLOR;
        public static final Color END_COLOR = new Color(0.9453125f, 0.9453125f, 0.9453125f, 0.6f);

        public static final float GRID_RENDER_POSITION_X = -Constants.GameConstants.GAME_WIDTH / 2;
        public static final float GRID_RENDER_POSITION_Y = -Constants.GameConstants.GAME_HEIGHT / 2;
        public static final float BRAID_OFFSET_BOTTOM = 70;
        public static final float BRAID_OFFSET_TOP = 20;
    }

    public static final class HudConstants {
        private HudConstants() {
        }
    }
}
