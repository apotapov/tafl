package com.pactstudios.games.tafl.core.es.model.board.cells;


public class CornerCell extends ModelCell {

    public CornerCell(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canWalk() {
        return false;
    }
}
