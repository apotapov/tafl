package com.pactstudios.games.tafl.core.es.model.map.cells;


public class CornerCell extends ModelCell {

    public CornerCell(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canWalk() {
        return false;
    }
}
