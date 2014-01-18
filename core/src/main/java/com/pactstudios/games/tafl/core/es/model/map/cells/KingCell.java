package com.pactstudios.games.tafl.core.es.model.map.cells;


public class KingCell extends ModelCell {

    public KingCell(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canWalk() {
        return false;
    }
}
