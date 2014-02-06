package com.pactstudios.games.tafl.core.es.model;

import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;
import com.pactstudios.games.tafl.core.es.model.rules.RulesEngine;

public class TaflBoard extends GameBoard<TaflMove> {

    private static final String[] HORIZONTAL_CELL_ID = new String[] {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z" };

    private static final String[] VERTICAL_CELL_ID = new String[] {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26" };

    public BitBoard corners;
    public BitBoard nearCorners;
    public int center;
    public BitBoard nearCenter;

    private Vector2 position;

    public int selectedPiece;

    public int king;
    public int capturedKing;

    public RulesEngine rules;

    public TaflBoard(int dimensions, int pieceTypes, ZorbistHash zorbistHash, RulesEngine rulesEngine) {
        super(dimensions, pieceTypes, zorbistHash);
        this.rules = rulesEngine;

        initialize();
    }

    private void initialize() {
        this.position = new Vector2();
        this.selectedPiece = Constants.BoardConstants.ILLEGAL_CELL;

        this.hashCode = super.hashCode();
        this.hashLock = super.hashLock();

        king = Constants.BoardConstants.ILLEGAL_CELL;
        capturedKing = Constants.BoardConstants.ILLEGAL_CELL;

        center = (dimensions/2) * dimensions + (dimensions / 2);

        nearCenter = new BitBoard(boardSize);
        nearCenter.set(center + 1);
        nearCenter.set(center - 1);
        nearCenter.set(center + dimensions);
        nearCenter.set(center - dimensions);

        corners = new BitBoard(boardSize);
        corners.set(0);
        corners.set(dimensions - 1);
        corners.set(dimensions * dimensions - dimensions);
        corners.set(dimensions * dimensions - 1);

        nearCorners = new BitBoard(boardSize);
        nearCorners.set(1);
        nearCorners.set(dimensions);
        nearCorners.set(dimensions - 2);
        nearCorners.set(dimensions * 2 - 1);
        nearCorners.set(dimensions * dimensions - dimensions + 1);
        nearCorners.set(dimensions * dimensions - dimensions * 2);
        nearCorners.set(dimensions * dimensions - 2);
        nearCorners.set(dimensions * dimensions - dimensions - 1);
    }

    public boolean canWalk(int piece, int cellId) {
        return piece == king || (center != cellId && !corners.get(cellId));
    }

    public float getDimensionWithBorders() {
        return dimensions * Constants.BoardRenderConstants.TILE_SIZE +
                Constants.BoardRenderConstants.BOARD_FRAME_WIDTH * 2;
    }

    public Vector2 getCellPosition(int cellId) {
        position.x = cellId % dimensions * Constants.BoardRenderConstants.TILE_SIZE +
                Constants.BoardRenderConstants.BOARD_HORIZONTAL_OFFSET;
        position.y = cellId / dimensions * Constants.BoardRenderConstants.TILE_SIZE +
                Constants.BoardRenderConstants.BOARD_VERTICAL_OFFSET;
        return position;
    }

    public Vector2 getCellPositionCenter(int cellId) {
        return getCellPosition(cellId).add(
                Constants.BoardRenderConstants.HALF_TILE_SIZE,
                Constants.BoardRenderConstants.HALF_TILE_SIZE);
    }

    public int getCellId(Vector2 screenPosition) {
        int x = (int)((screenPosition.x -
                Constants.BoardRenderConstants.BOARD_HORIZONTAL_OFFSET) /
                Constants.BoardRenderConstants.TILE_SIZE);
        int y = (int)((screenPosition.y -
                Constants.BoardRenderConstants.BOARD_VERTICAL_OFFSET) /
                Constants.BoardRenderConstants.TILE_SIZE);
        return y * dimensions + x;
    }

    public String getHorizontalCellId(int index) {
        return HORIZONTAL_CELL_ID[index];
    }

    public String getVerticalCellId(int index) {
        return VERTICAL_CELL_ID[index];
    }

    public String getCellName(int x, int y) {
        return HORIZONTAL_CELL_ID[x] + VERTICAL_CELL_ID[y];
    }

    public BitBoard blackBitBoard() {
        return bitBoards[Constants.BoardConstants.BLACK_TEAM];
    }

    public BitBoard whiteBitBoard() {
        return bitBoards[Constants.BoardConstants.WHITE_TEAM];
    }

    @Override
    public void applyMove(TaflMove move, boolean simulate) {
        super.applyMove(move, simulate);

        if (move.source == king) {
            king = move.destination;
        }
    }

    @Override
    public TaflMove undoMove() {
        TaflMove move = super.undoMove();
        if (move != null && move.destination == king) {
            king = move.source;
        }
        return move;
    }

    @Override
    public void addPiece(int team, int piece) {
        super.addPiece(team, piece);

        if (piece == capturedKing) {
            king = capturedKing;
            capturedKing = Constants.BoardConstants.ILLEGAL_CELL;
        }
    }

    @Override
    public void removePieces(int team, BitBoard pieces) {
        super.removePieces(team, pieces);

        if (king != Constants.BoardConstants.ILLEGAL_CELL && pieces.get(king)) {
            king = Constants.BoardConstants.ILLEGAL_CELL;
            capturedKing = king;
        }
    }

    @Override
    protected BitBoard getCapturedPieces(TaflMove move) {
        return rules.getCapturedPieces(move);
    }
}
