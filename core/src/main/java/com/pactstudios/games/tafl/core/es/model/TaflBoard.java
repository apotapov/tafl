package com.pactstudios.games.tafl.core.es.model;

import java.util.BitSet;

import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;

public class TaflBoard extends GameBoard {

    private static final String[] HORIZONTAL_CELL_ID = new String[] {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z" };

    private static final String[] VERTICAL_CELL_ID = new String[] {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26" };

    public BitSet corners;
    public BitSet nearCorners;
    public int center;
    public BitSet nearCenter;

    private Vector2 position;

    public int selectedPiece;

    public int king;

    public TaflBoard(int dimensions, int pieceTypes, ZorbistHash zorbistHash, int king) {
        super(dimensions, pieceTypes, zorbistHash);
        this.position = new Vector2();
        this.selectedPiece = Constants.BoardConstants.ILLEGAL_CELL;
        this.king = king;
    }

    public void initialize() {
        center = (dimensions/2) * dimensions + (dimensions / 2);

        nearCenter = new BitSet(dimensions);
        nearCenter.set(center + 1);
        nearCenter.set(center - 1);
        nearCenter.set(center + dimensions);
        nearCenter.set(center - dimensions);

        corners = new BitSet(dimensions);
        corners.set(0);
        corners.set(dimensions - 1);
        corners.set(dimensions * dimensions - dimensions);
        corners.set(dimensions * dimensions - 1);

        nearCorners = new BitSet(dimensions);
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

    public BitSet blackBitBoard() {
        return bitBoards[Constants.BoardConstants.BLACK_TEAM];
    }

    public BitSet whiteBitBoard() {
        return bitBoards[Constants.BoardConstants.WHITE_TEAM];
    }

    public void applyMove(TaflMove move) {
        BitSet bitBoard = bitBoards[move.pieceType];
        bitBoard.clear(move.source);
        bitBoard.set(move.destination);

        if (move.source == king) {
            if (move.source == 25) {
                System.out.println(25);
            }
            king = move.destination;
        }
    }

    public void undoMove(TaflMove move) {
        BitSet bitBoard = bitBoards[move.pieceType];
        bitBoard.clear(move.destination);
        bitBoard.set(move.source);

        bitBoards[(move.pieceType + 1) % 2].or(move.capturedPieces);

        if (move.destination == king) {
            if (move.destination == 25) {
                System.out.println(25);
            }
            king = move.source;
        }
    }

    public void addPiece(int team, int piece) {
        bitBoards[team].set(piece);
    }

    public void removePieces(int captor, BitSet capturedPiece) {
        bitBoards[(captor + 1) % 2].andNot(capturedPiece);

        if (capturedPiece.get(king)) {
            king = Constants.BoardConstants.ILLEGAL_CELL;
        }
    }
}