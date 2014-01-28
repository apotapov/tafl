package com.pactstudios.games.tafl.core.consts;

public class LocalizedStrings {
    public enum MainMenu {
        GAME_TITLE,
        MAIN_MENU_BUTTON_START,
        MAIN_MENU_BUTTON_RESUME,
        MAIN_MENU_BUTTON_OPTIONS,
        MAIN_MENU_BUTTON
    }

    public enum OptionsMenu {
        OPTIONS_TITLE,
        OPTIONS_SOUND_EFFECTS,
        OPTIONS_MUSIC,
    }

    public enum LevelSelectionMenu {
        LEVEL_SELECTION_TITLE,
        PLAY_LEVEL_BUTTON,

        PLAY_VERSUS_COMPUTER,
        COMPUTER_STARTS,
    }

    public enum GameMenu {
        LOSS_TITLE,
        LOSS_TEXT,

        WHITE_WIN_TITLE,
        WHITE_WIN_TEXT,

        BLACK_WIN_TITLE,
        BLACK_WIN_TEXT,

        MENU_TITLE,
        MENU_TEXT,

        RESTART_BUTTON,
        MAIN_MENU_BUTTON,
        RESUME_BUTTON,
    }

    public enum Game {
        TEAM_WHITE,
        TEAM_BLACK,
    }

    public enum Hud {
        MENU_BUTTON,

        UNDO_BUTTON,

        TURN_LABEL,
        GAME_TIME_LABEL,
    }
}
