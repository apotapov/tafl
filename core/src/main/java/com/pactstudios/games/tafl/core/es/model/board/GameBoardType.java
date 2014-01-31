package com.pactstudios.games.tafl.core.es.model.board;

import com.pactstudios.games.tafl.core.consts.Constants;

public enum GameBoardType {
    STANDARD(Constants.BoardConstants.STANDARD_BOARD_DIMENSION),
    SMALL(Constants.BoardConstants.SMALL_BOARD_DIMENSION);

    public int dimensions;

    private GameBoardType(int dimensions) {
        this.dimensions = dimensions;
    }
}
