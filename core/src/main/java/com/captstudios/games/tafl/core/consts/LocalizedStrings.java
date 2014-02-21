package com.captstudios.games.tafl.core.consts;

public class LocalizedStrings {
    public enum MainMenu {
        GAME_TITLE,
        MAIN_MENU_BUTTON_START,
        MAIN_MENU_BUTTON_RESUME,
        MAIN_MENU_BUTTON_OPTIONS,
        MAIN_MENU_BUTTON_INSTRUCTIONS,
        MAIN_MENU_BUTTON
    }

    public enum OptionsMenu {
        OPTIONS_TITLE,
        OPTIONS_SOUND_EFFECTS,
        OPTIONS_MUSIC,
        OPTIONS_VIBRATE,
        ABOUT,
    }

    public enum LevelSelectionMenu {
        LEVEL_SELECTION_TITLE,
        PLAY_LEVEL_BUTTON,

        PLAY_VERSUS_COMPUTER,
        COMPUTER_STARTS,
        AI_DIFFICULTY,
    }

    public enum GameMenu {
        LOSS_TITLE,
        LOSS_TEXT,

        SURRENDER_TITLE,
        SURRENDER_TEXT,

        DRAW_TITLE,
        DRAW_NO_MOVES_WHITE_TEXT,
        DRAW_NO_MOVES_BLACK_TEXT,
        DRAW_KING_TRAPPED_TEXT,
        DRAW_THREE_PEAT_TEXT,
        DRAW_TOO_MANY_TURNS_TEXT,

        WIN_TITLE,
        WHITE_WIN_TEXT,
        BLACK_WIN_TEXT,

        MENU_TITLE,

        RESTART_BUTTON,
        MAIN_MENU_BUTTON,
        RESUME_BUTTON,
        OK_BUTTON,

        PLAYER_WARNING_TITLE,
        RAICHI_TEXT,
        TUICHI_TEXT,
        WATCH_YOUR_KING_TEXT,
    }

    public enum Game {
        AI_PROCESSING,

        HUMAN_PLAYER,
        COMPUTER_PLAYER,

        PLAYER_1,
        PLAYER_2,
    }

    public enum Hud {
        MENU_BUTTON,

        UNDO_BUTTON,

        WHITE_TURN_LABEL,
        BLACK_TURN_LABEL,
        COMPUTER_TURN_LABEL,
    }

    public enum About {
        ABOUT_COPYRIGHT,
        ABOUT_RIGHTS_RESERVED,
        ABOUT_ART_LABEL,
        ABOUT_ARTIST,
        ABOUT_ENGINEERING_LABEL,
        ABOUT_ENGINEER,
        ABOUT_MUSIC_LABEL,
        ABOUT_MUSICIAN
    }
}
