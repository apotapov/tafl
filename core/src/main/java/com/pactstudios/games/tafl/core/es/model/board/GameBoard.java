package com.pactstudios.games.tafl.core.es.model.board;

import java.util.BitSet;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class GameBoard {

    public static final int NUMBER_OF_TEAMS = 2;

    public ModelCell[][] cells;

    public GamePiece selectedPiece;

    public ModelCell[] cornerCells;

    public int dimensions;
    public int pieceTypes;
    public int numberSquares;

    public BitSet[] bitBoards;

    public ZorbistHash zorbistHash;

    public GameBoard(int dimensions, int pieceTypes, ZorbistHash zorbistHash) {
        this.dimensions = dimensions;
        this.pieceTypes = pieceTypes;
        this.numberSquares = dimensions * dimensions;
        this.zorbistHash = zorbistHash;

        this.cornerCells = new ModelCell[4];

        bitBoards = new BitSet[pieceTypes + NUMBER_OF_TEAMS];
        for (int i = 0; i < pieceTypes; i++) {
            bitBoards[i] = new BitSet(numberSquares);
        }
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

    /**
     * Compute a 32-bit integer to represent the board, according to Zobrist[70]
     */
    @Override
    public int hashCode() {
        int hash = 0;
        // Look at all pieces, one at a time
        for (int currPiece = 0; currPiece < pieceTypes; currPiece++) {
            BitSet board = bitBoards[currPiece];
            // Search for all pieces on all squares. We could optimize here: not
            // looking for pawns on the back row (or the eight row), getting out
            // of the "currSqaure" loop once we found one king of one color,
            // etc.
            // But for simplicity's sake, we'll keep things generic.
            for (int currSquare = 0; currSquare < numberSquares; currSquare++) {
                // Zobrist's method: generate a bunch of random bitfields, each
                // representing a certain "piece X is on square Y" predicate;
                // XOR
                // the bitfields associated with predicates which are true.
                // Therefore, if we find a piece (in tmp) in a certain square,
                // we accumulate the related HashKeyComponent.
                if (board.get(currSquare)) {
                    hash ^= zorbistHash.hash[currPiece][currSquare];
                }
            }
        }
        return hash;
    }

    /**
     * Compute a second 32-bit hash key, using an entirely different set
     * piece/square components.
     * This is required to be able to detect hashing collisions without
     * storing an entire board in each slot of the TranspositionTable,
     * which would gobble up inordinate amounts of memory
     * @return
     */
    public int hashLock() {
        int hash = 0;
        for (int currPiece = 0; currPiece < pieceTypes; currPiece++) {
            BitSet board = bitBoards[currPiece];
            for (int currSquare = 0; currSquare < numberSquares; currSquare++) {
                if (board.get(currSquare)) {
                    hash ^= zorbistHash.hashLock[currPiece][currSquare];
                }
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof GameBoard) {
            return hashCode() == other.hashCode();
        }
        return false;
    }
}
