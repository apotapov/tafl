package com.pactstudios.games.tafl.core.es.model.board;

import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class GameBoard {
    public ModelCell[][] cells;
    public int dimensions;
    public GamePiece selectedPiece;

    public ModelCell[] cornerCells;

    public GameBoard(int dimensions) {
        this.dimensions = dimensions;
        this.cornerCells = new ModelCell[4];
    }

    public ModelCell getCell(int x, int y) {
        if(x >= 0 && x < dimensions && y >= 0 && y < dimensions) {
            return cells[x][y];
        }
        return null;
    }

    public void setCell(ModelCell cell) {
        if(cell.x >= 0 && cell.x < dimensions && cell.y >= 0 && cell.y < dimensions) {
            cells[cell.x][cell.y] = cell;
        }
    }

    public void reset() {
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                ModelCell cell = getCell(i, j);
                cell.reset();
            }
        }
    }
}
