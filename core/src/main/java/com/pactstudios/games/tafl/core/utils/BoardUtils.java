package com.pactstudios.games.tafl.core.utils;

import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;

public class BoardUtils {

    private static final String[] HORIZONTAL_CELL_ID = new String[] {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z" };

    private static final String[] VERTICAL_CELL_ID = new String[] {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26" };

    private static Vector2 position = new Vector2();

    public static Vector2 getTilePosition(int x, int y) {
        position.x = x * Constants.BoardConstants.TILE_SIZE + Constants.BoardConstants.BOARD_FRAME_WIDTH;
        position.y = y * Constants.BoardConstants.TILE_SIZE + Constants.BoardConstants.BOARD_FRAME_WIDTH;
        return position;
    }

    public static Vector2 getTilePosition(ModelCell cell) {
        return getTilePosition(cell.x, cell.y);
    }

    public static Vector2 getTilePositionCenter(int x, int y) {
        return getTilePosition(x, y).add(
                Constants.BoardConstants.HALF_TILE_SIZE,
                Constants.BoardConstants.HALF_TILE_SIZE);
    }

    public static Vector2 getTilePositionCenter(ModelCell cell) {
        return getTilePositionCenter(cell.x, cell.y);
    }

    public static Vector2 getMapPosition(Vector2 screenPosition) {
        position.x = (int)((screenPosition.x - Constants.BoardConstants.BOARD_FRAME_WIDTH) / Constants.BoardConstants.TILE_SIZE);
        position.y = (int)((screenPosition.y - Constants.BoardConstants.BOARD_FRAME_WIDTH) / Constants.BoardConstants.TILE_SIZE);
        return position;
    }

    public static String getHorizontalCellId(int index) {
        return HORIZONTAL_CELL_ID[index];
    }

    public static String getVerticalCellId(int index) {
        return VERTICAL_CELL_ID[index];
    }

    public static String getCellId(int x, int y) {
        return HORIZONTAL_CELL_ID[x] + VERTICAL_CELL_ID[y];
    }
}
