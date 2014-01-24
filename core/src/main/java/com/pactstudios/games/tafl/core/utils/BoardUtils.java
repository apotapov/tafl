package com.pactstudios.games.tafl.core.utils;

import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;

public class BoardUtils {

    private static Vector2 position = new Vector2();

    public static Vector2 getTilePosition(int x, int y) {
        position.set(x * Constants.BoardConstants.TILE_SIZE, y * Constants.BoardConstants.TILE_SIZE);
        return position;
    }

    public static Vector2 getTilePosition(ModelCell cell) {
        return getTilePosition(cell.x, cell.y);
    }

    public static Vector2 getTilePositionCenter(int x, int y) {
        position.set(x * Constants.BoardConstants.TILE_SIZE + Constants.BoardConstants.HALF_TILE_SIZE,
                y * Constants.BoardConstants.TILE_SIZE + Constants.BoardConstants.HALF_TILE_SIZE);
        return position;
    }

    public static Vector2 getTilePositionCenter(ModelCell cell) {
        return getTilePositionCenter(cell.x, cell.y);
    }

    public static Vector2 getMapPosition(Vector2 screenPosition) {
        position.set((int)(screenPosition.x / Constants.BoardConstants.TILE_SIZE),
                (int)(screenPosition.y / Constants.BoardConstants.TILE_SIZE));
        return position;
    }
}
