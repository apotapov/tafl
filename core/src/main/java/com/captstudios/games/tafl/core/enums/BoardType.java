package com.captstudios.games.tafl.core.enums;

import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;



public enum BoardType {
    BOARD_SIZE_11_11(
            Assets.Graphics.GRID_11,
            Assets.Graphics.WHITE_PIECE_11,
            Assets.Graphics.KING_PIECE_11,
            Assets.Graphics.BLACK_PIECE_11,
            Constants.PieceConstants.TILE_SIZE_11,
            Constants.PieceConstants.BOARD_FRAME_WIDTH_11,
            Constants.PieceConstants.CELL_HORIZONTAL_OFFSET_11,
            Constants.PieceConstants.CELL_VERTICAL_OFFSET_11,
            Constants.PieceConstants.CORNER_VALUE_CELLS_11,
            Constants.AiConstants.BEGINNER_TREE_DEPTH_11,
            Constants.AiConstants.INTERMEDIATE_TREE_DEPTH_11,
            Constants.AiConstants.ADVANCED_TREE_DEPTH_11),
            BOARD_SIZE_9_9(
                    Assets.Graphics.GRID_9,
                    Assets.Graphics.WHITE_PIECE_9,
                    Assets.Graphics.KING_PIECE_9,
                    Assets.Graphics.BLACK_PIECE_9,
                    Constants.PieceConstants.TILE_SIZE_9,
                    Constants.PieceConstants.BOARD_FRAME_WIDTH_9,
                    Constants.PieceConstants.CELL_HORIZONTAL_OFFSET_9,
                    Constants.PieceConstants.CELL_VERTICAL_OFFSET_9,
                    Constants.PieceConstants.CORNER_VALUE_CELLS_9,
                    Constants.AiConstants.BEGINNER_TREE_DEPTH_9,
                    Constants.AiConstants.INTERMEDIATE_TREE_DEPTH_9,
                    Constants.AiConstants.ADVANCED_TREE_DEPTH_9);

    public String grid;
    public String whitePiece;
    public String kingPiece;
    public String blackPiece;

    public float tileSize;
    public float halfTile;
    public float borderWidth;
    public float cellXOffset;
    public float cellYOffset;

    public int[] barricades;

    public int beginnerDepth;
    public int intermediateDepth;
    public int advancedDepth;

    private BoardType(
            String grid,
            String whitePiece,
            String kingPiece,
            String blackPiece,
            float tileSize,
            float borderWidth,
            float cellXOffset,
            float cellYOffset,
            int[] barricades,
            int beginnerDepth,
            int intermediateDepth,
            int advancedDepth) {
        this.grid = grid;
        this.whitePiece = whitePiece;
        this.kingPiece = kingPiece;
        this.blackPiece = blackPiece;

        this.tileSize = tileSize;
        this.halfTile = tileSize / 2;
        this.borderWidth = borderWidth;
        this.cellXOffset = cellXOffset;
        this.cellYOffset = cellYOffset;

        this.barricades = barricades;

        this.beginnerDepth = beginnerDepth;
        this.intermediateDepth = intermediateDepth;
        this.advancedDepth = advancedDepth;
    }

    public static BoardType getBoardType(int size) {
        if (size == 11) {
            return BOARD_SIZE_11_11;
        } else {
            return BOARD_SIZE_9_9;
        }
    }
}
