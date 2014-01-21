package com.pactstudios.games.tafl.core.es.model.board.cells;

import com.pactstudios.games.tafl.core.es.model.board.GameBoard;



public class RegularCell extends ModelCell {

    public RegularCell(int x, int y, GameBoard board) {
        super(x, y, board);
    }

    @Override
    public boolean canWalk() {
        return true;
    }
}
