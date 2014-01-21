package com.pactstudios.games.tafl.core.es.model.board.cells;

import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.objects.Piece;


public abstract class ModelCell {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public GameBoard board;
    public int x;
    public int y;
    public Piece piece;
    public String name;

    public ModelCell(int x, int y, GameBoard board) {
        this.x = x;
        this.y = y;
        this.board = board;
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

    public ModelCell up() {
        return board.getCell(x, y + 1);
    }

    public ModelCell down() {
        return board.getCell(x, y - 1);
    }

    public ModelCell right() {
        return board.getCell(x + 1, y);
    }

    public ModelCell left() {
        return board.getCell(x - 1, y);
    }
}
