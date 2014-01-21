package com.pactstudios.games.tafl.core.es.model.board;

import com.pactstudios.games.tafl.core.es.model.board.cells.CornerCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.KingCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.RegularCell;

public class GameBoard {
    public ModelCell[][] cells;
    public int width;
    public int height;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new ModelCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ModelCell cell = createCell(i, j);
                cells[i][j] = cell;
            }
        }
    }

    public ModelCell getCell(int x, int y) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            return cells[x][y];
        }
        return null;
    }

    public void setCell(ModelCell cell) {
        if(cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height) {
            cells[cell.x][cell.y] = cell;
        }
    }

    public void reset() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ModelCell cell = getCell(i, j);
                cell.reset();
            }
        }
    }

    protected ModelCell createCell(int x, int y) {
        if ((x == 0 && y == 0) ||
                (x == width - 1 && y == 0) ||
                (x == 0 && y == height - 1) ||
                (x == width -1 && y == height -1)) {
            return new CornerCell(x, y, this);
        } else if (x == width / 2 && y == height / 2) {
            return new KingCell(x, y, this);
        } else {
            return new RegularCell(x, y, this);
        }
    }


}
