package com.pactstudios.games.tafl.core.enums;

import com.pactstudios.games.tafl.core.consts.LocalizedStrings;

public enum DrawReasonEnum {
    DRAW_NO_MOVES_WHITE(LocalizedStrings.GameMenu.DRAW_NO_MOVES_WHITE_TEXT),
    DRAW_NO_MOVES_BLACK(LocalizedStrings.GameMenu.DRAW_NO_MOVES_BLACK_TEXT),
    DRAW_NO_KING_CAPTURE(LocalizedStrings.GameMenu.DRAW_KING_TRAPPED_TEXT),
    DRAW_KING_TRAPPED(LocalizedStrings.GameMenu.DRAW_KING_TRAPPED_TEXT),
    DRAW_THREE_PEAT(LocalizedStrings.GameMenu.DRAW_THREE_PEAT_TEXT),
    DRAW_TOO_MANY_TURNS(LocalizedStrings.GameMenu.DRAW_TOO_MANY_TURNS_TEXT);

    public LocalizedStrings.GameMenu text;

    private DrawReasonEnum(LocalizedStrings.GameMenu text) {
        this.text = text;
    }
}
