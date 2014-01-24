package com.pactstudios.games.tafl.core.es.model.board;

import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class GameBoard {
    public ModelCell[][] cells;
    public int dimentions;
    public GamePiece selectedPiece;

    public GameBoard(int dimensions) {
        this.dimentions = dimensions;
    }

    public ModelCell getCell(int x, int y) {
        if(x >= 0 && x < dimentions && y >= 0 && y < dimentions) {
            return cells[x][y];
        }
        return null;
    }

    public void setCell(ModelCell cell) {
        if(cell.x >= 0 && cell.x < dimentions && cell.y >= 0 && cell.y < dimentions) {
            cells[cell.x][cell.y] = cell;
        }
    }

    public void reset() {
        for (int i = 0; i < dimentions; i++) {
            for (int j = 0; j < dimentions; j++) {
                ModelCell cell = getCell(i, j);
                cell.reset();
            }
        }
    }
}
