package com.pactstudios.games.tafl.core.es.model.map.cells;



public class RegularCell extends ModelCell {

    public RegularCell(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canWalk() {
        return true;
    }
}
