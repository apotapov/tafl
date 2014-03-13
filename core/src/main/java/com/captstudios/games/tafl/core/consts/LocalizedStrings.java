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
    }

    public enum LevelSelectionMenu {
        BOARD_SELECTION_TITLE,
        SIDE_SELECTION_TITLE,
        PLAY_LEVEL_BUTTON,

        PLAY_VERSUS_COMPUTER,
        COMPUTER_STARTS,
        AI_DIFFICULTY,
    }

    public enum GameMenu {
        GAME_OVER_TITLE,
        LOSS_TEXT,

        SURRENDER_TEXT,

        DRAW_NO_MOVES_WHITE_TEXT,
        DRAW_NO_MOVES_BLACK_TEXT,
        DRAW_KING_TRAPPED_TEXT,
        DRAW_THREE_PEAT_TEXT,
        DRAW_TOO_MANY_TURNS_TEXT,

        WHITE_WIN_TEXT,
        BLACK_WIN_TEXT,

        MENU_TITLE,

        RESTART_BUTTON,
        MAIN_MENU_BUTTON,
        RESUME_BUTTON,
        OK_BUTTON,
    }

    public enum Game {
        AI_PROCESSING,

        HUMAN_PLAYER,
        COMPUTER_PLAYER,
    }

    public enum Hud {
        MENU_BUTTON,

        UNDO_BUTTON,
    }

    public enum AboutInfo {
        ABOUT,
        ABOUT_VERSION,
        ABOUT_COPYRIGHT,
        ABOUT_RIGHTS_RESERVED,
    }

    public enum AboutCredit {
        ABOUT_ART_LABEL,
        ABOUT_ARTIST,
        ABOUT_ENGINEERING_LABEL,
        ABOUT_ENGINEER,
        //        ABOUT_TECHNOLOGY_LABEL,
        //        ABOUT_TECHNOLOGY
    }

    public enum AboutRules {
        ABOUT_RULES_TITLE_11,
        ABOUT_RULES_11,
        ABOUT_RULES_TITLE_9,
        ABOUT_RULES_9,
        ABOUT_RULES_1,
        ABOUT_RULES_2
    }
}
