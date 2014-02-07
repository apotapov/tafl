package com.pactstudios.games.tafl.core.enums;

import java.util.EnumSet;

public enum LifeCycle {
    PLAY,
    MENU,
    QUIT,
    RESTART,
    WIN,
    LOSS,
    DRAW,
    SURRENDER;

    public static EnumSet<LifeCycle> GAME_OVER = EnumSet.of(WIN, LOSS, DRAW, SURRENDER);

}
