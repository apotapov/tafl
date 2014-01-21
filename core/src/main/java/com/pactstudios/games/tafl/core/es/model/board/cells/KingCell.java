package com.pactstudios.games.tafl.core.es.model.board.cells;

import com.pactstudios.games.tafl.core.es.model.board.GameBoard;


public class KingCell extends ModelCell {

    public KingCell(int x, int y, GameBoard board) {
        super(x, y, board);
    }

    @Override
    public boolean canWalk() {
        return false;
    }
}
