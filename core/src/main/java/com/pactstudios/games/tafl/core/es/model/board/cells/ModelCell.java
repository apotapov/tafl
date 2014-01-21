package com.pactstudios.games.tafl.core.es.model.board.cells;

import com.pactstudios.games.tafl.core.es.model.objects.Piece;


public abstract class ModelCell {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public int x;
    public int y;
    public Piece piece;
    public String name;

    public ModelCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.name = "" + LETTERS.charAt(x) + y;
    }

    public abstract boolean canWalk();

    @Override
    public String toString() {
        return name;
    }

    public void reset() {
        piece = null;
    }
}
