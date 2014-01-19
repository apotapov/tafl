package com.pactstudios.games.tafl.core.consts;

import com.badlogic.gdx.graphics.Color;


public final class Constants {

    private Constants() {
    }

    public static final class Screens {
        private Screens() {
        }

        public static final int BUTTON_WIDTH = 300;
        public static final int BUTTON_HEIGHT = 60;

        public static final int LIST_WIDTH = 200;
        public static final int LIST_HEIGHT = 200;
    }

    public static final class Groups {
        private Groups(){
        }

        public static final String HIGHLIGHTED_CELLS = "hcells";
        public static final String WHITE = "white";
        public static final String BLACK = "black";
        public static final String KING = "king";
        public static final String SELECTED_PIECE = "selected";

        public static final String MAP = "map";
        public static final String RENDERER = "renderer";
        public static final String HUD = "hud";
    }

    public static final class Piece {
        private Piece() {
        }
        public static final int HUMAN_SIZE = 32;

        public static final int REGULAR_SPEED = 100;
        public static final int REGULAR_WEIGHT = 1;

        public static final int FAST_SPEED = 200;
        public static final int FAST_WEIGHT = 1;

        public static final int SLOW_SPEED = 50;
        public static final int SLOW_WEIGHT = 2;

        public static final int BASE_MONEY = 20;
        public static final int BASE_SCORE = 100;

        public static final float FRAME_DURATION = 0.2f;

        public static final int VELOCITY_FLIP_MARGIN = 15;

        public static final float SCALING = 2f;

        public static final float MAX_ANGLE_CHANGE = 5;
    }



    public static final class GameObjects {
        private GameObjects() {
        }

        public static final int TRAP_BOUNDS = 32;
        public static final int TRIGGER_BOUNDS = 32;
        public static final int DOOR_BOUNDS = 32;
        public static final float SCALING = 2f;
    }

    public static final class Map {
        private Map() {
        }
        public static final int TILE_SIZE = 64;
        public static final int HALF_TILE_SIZE = TILE_SIZE / 2;

        public static final int MAP_DIM = 13;
        public static final int MAP_SIZE = MAP_DIM * TILE_SIZE;


        public static final Color BACKGROUND_COLOR = new Color(0.5f, .25f, .25f, 1f);
        public static final Color HIGHLIGHT_COLOR = new Color(1f, 1f, 1f, 0.25f);

    }

    public static final class Hud {
        private Hud() {
        }
        public static final int HUD_HEIGHT = 100;
        public static final Color HUD_BACKGROUND_COLOR = new Color(0, 0, 0, 0.5f);

        public static final int BUTTON_WIDTH = 100;
        public static final int BUTTON_HEIGHT = 100;
    }

    public static final class Game {
        private Game() {
        }
        public static final boolean DEBUG = true;
        public static final int BATCH_SIZE = 100;
        public static final int GAME_WIDTH = 600;
        public static final int GAME_HEIGHT = 800;
        public static final boolean USE_GL20 = false;
    }
}
