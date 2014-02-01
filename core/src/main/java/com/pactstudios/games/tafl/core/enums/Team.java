package com.pactstudios.games.tafl.core.enums;

import com.pactstudios.games.tafl.core.consts.Constants;


public enum Team {
    WHITE(Constants.BoardConstants.WHITE_TEAM_BIT_BOARD_ID),
    BLACK(Constants.BoardConstants.BLACK_TEAM_BIT_BOARD_ID);

    public int bitBoardId;

    private Team(int bitBoardId) {
        this.bitBoardId = bitBoardId;
    }

    public static Team fromId(int id) {
        return id == Team.WHITE.bitBoardId ? Team.WHITE : Team.BLACK;
    }
}
