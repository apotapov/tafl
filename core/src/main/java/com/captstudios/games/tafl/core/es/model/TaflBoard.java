package com.captstudios.games.tafl.core.es.model;

import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;
import com.captstudios.games.tafl.core.es.model.rules.RulesEngine;
import com.roundtriangles.games.zaria.utils.ModifiableString;

public class TaflBoard extends GameBoard {

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

    public int capturedKing;

    public RulesEngine rules;

    public ModifiableString boardString;

    public TaflBoard(int dimensions, int pieceTypes, ZorbistHash zorbistHash, RulesEngine rulesEngine) {
        super(dimensions, pieceTypes, zorbistHash);
        this.rules = rulesEngine;

        initialize();
    }

    private void initialize() {
        boardString = new ModifiableString(boardSize);

        this.position = new Vector2();
        this.selectedPiece = Constants.BoardConstants.ILLEGAL_CELL;

        this.hashCode = super.hashCode();

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
        corners.set(boardSize - dimensions);
        corners.set(boardSize - 1);

        nearCorners = new BitBoard(boardSize);
        nearCorners.set(1);
        nearCorners.set(dimensions);
        nearCorners.set(dimensions - 2);
        nearCorners.set(dimensions * 2 - 1);
        nearCorners.set(boardSize - dimensions + 1);
        nearCorners.set(boardSize - dimensions * 2);
        nearCorners.set(boardSize - 2);
        nearCorners.set(boardSize - dimensions - 1);
    }

    public boolean canWalk(int piece, int cellId) {
        return bitBoards[Constants.BoardConstants.KING].get(piece) || (center != cellId && !corners.get(cellId));
    }

    public float getDimensionWithBorders() {
        return dimensions * Constants.BoardRenderConstants.TILE_SIZE +
                Constants.BoardRenderConstants.BOARD_FRAME_WIDTH * 2;
    }

    public Vector2 getCellPosition(int cellId) {
        position.x = cellId % dimensions * Constants.BoardRenderConstants.TILE_SIZE +
                Constants.BoardRenderConstants.CELL_HORIZONTAL_OFFSET;
        position.y = cellId / dimensions * Constants.BoardRenderConstants.TILE_SIZE +
                Constants.BoardRenderConstants.CELL_VERTICAL_OFFSET;
        return position;
    }

    public Vector2 getCellPositionCenter(int cellId) {
        return getCellPosition(cellId).add(
                Constants.BoardRenderConstants.HALF_TILE_SIZE,
                Constants.BoardRenderConstants.HALF_TILE_SIZE);
    }

    public int getCellId(Vector2 screenPosition) {
        int x = (int)((screenPosition.x -
                Constants.BoardRenderConstants.CELL_HORIZONTAL_OFFSET) /
                Constants.BoardRenderConstants.TILE_SIZE);

        // to make sure we are not wrapping around to the other side
        // when we touch the edge of the board.
        x = Math.min(x, dimensions - 1);

        int y = (int)((screenPosition.y -
                Constants.BoardRenderConstants.CELL_VERTICAL_OFFSET) /
                Constants.BoardRenderConstants.TILE_SIZE);

        // leave some margin around the top as well
        if (y == dimensions) {
            y = dimensions - 1;
        }


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

    public BitBoard kingBitBoard() {
        return bitBoards[Constants.BoardConstants.KING];
    }

    public int getKing() {
        return bitBoards[Constants.BoardConstants.KING].nextSetBit(0);
    }

    public boolean getKingCaptured() {
        return bitBoards[Constants.BoardConstants.KING].isEmpty();
    }


    @Override
    public void addPiece(int team, int piece) {
        super.addPiece(team, piece);

        if (piece == capturedKing) {
            bitBoards[Constants.BoardConstants.KING].set(capturedKing);
            capturedKing = Constants.BoardConstants.ILLEGAL_CELL;
        }
    }

    @Override
    public void removePieces(int team, BitBoard pieces) {
        super.removePieces(team, pieces);

        if (bitBoards[Constants.BoardConstants.KING].intersects(pieces)) {
            capturedKing = getKing();
            bitBoards[Constants.BoardConstants.KING].clear();
        }
    }

    @Override
    protected BitBoard getCapturedPieces(Move move) {
        return rules.getCapturedPieces(move);
    }

    @Override
    public String toString() {
        for (int i = 0; i < boardSize; i++) {
            boardString.setChar(i, Constants.BoardConstants.EMPTY_CELL);
        }

        BitBoard bitBoard = bitBoards[Constants.BoardConstants.WHITE_TEAM];
        for (int i = bitBoard.nextSetBit(0); i >= 0; i = bitBoard.nextSetBit(i+1)) {
            boardString.setChar(i, Constants.BoardConstants.WHITE_PIECE);
        }

        bitBoard = bitBoards[Constants.BoardConstants.BLACK_TEAM];
        for (int i = bitBoard.nextSetBit(0); i >= 0; i = bitBoard.nextSetBit(i+1)) {
            boardString.setChar(i, Constants.BoardConstants.BLACK_PIECE);
        }

        if (!bitBoards[Constants.BoardConstants.KING].isEmpty()) {
            boardString.setChar(getKing(), Constants.BoardConstants.KING_PIECE);
        }

        return boardString.toString();
    }
}
