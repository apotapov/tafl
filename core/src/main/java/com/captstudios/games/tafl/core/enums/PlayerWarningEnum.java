package com.captstudios.games.tafl.core.enums;

import com.captstudios.games.tafl.core.consts.LocalizedStrings;

public enum PlayerWarningEnum {
    RAICHI(LocalizedStrings.GameMenu.RAICHI_TEXT),
    TUICHI(LocalizedStrings.GameMenu.TUICHI_TEXT),
    WATCH_YOUR_KING(LocalizedStrings.GameMenu.WATCH_YOUR_KING_TEXT);

    public LocalizedStrings.GameMenu text;

    private PlayerWarningEnum(LocalizedStrings.GameMenu text) {
        this.text = text;
    }
}
