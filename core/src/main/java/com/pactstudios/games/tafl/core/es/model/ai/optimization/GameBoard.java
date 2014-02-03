package com.pactstudios.games.tafl.core.es.model.ai.optimization;

import java.util.BitSet;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;

public class GameBoard {

    public static final int NUMBER_OF_TEAMS = 2;

    public int dimensions;
    public int pieceTypes;
    public int numberCells;

    public BitSet[] bitBoards;

    public ZorbistHash zorbistHash;

    public GameBoard(int dimensions, int pieceTypes, ZorbistHash zorbistHash) {
        this.dimensions = dimensions;
        this.pieceTypes = pieceTypes;
        this.numberCells = dimensions * dimensions;
        this.zorbistHash = zorbistHash;

        bitBoards = new BitSet[pieceTypes];
        for (int i = 0; i < pieceTypes; i++) {
            bitBoards[i] = new BitSet(numberCells);
        }
    }

    public void reset() {
        for (BitSet bitBoard : bitBoards) {
            bitBoard.clear();
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
            // Search for all pieces on all cells. We could optimize here: not
            // looking for pawns on the back row (or the eight row), getting out
            // of the "currSqaure" loop once we found one king of one color,
            // etc.
            // But for simplicity's sake, we'll keep things generic.
            for (int currCell = 0; currCell < numberCells; currCell++) {
                // Zobrist's method: generate a bunch of random bitfields, each
                // representing a certain "piece X is on cell Y" predicate;
                // XOR
                // the bitfields associated with predicates which are true.
                // Therefore, if we find a piece (in tmp) in a certain cell,
                // we accumulate the related HashKeyComponent.
                if (board.get(currCell)) {
                    hash ^= zorbistHash.hash[currPiece][currCell];
                }
            }
        }
        return hash;
    }

    /**
     * Compute a second 32-bit hash key, using an entirely different set
     * piece/cell components.
     * This is required to be able to detect hashing collisions without
     * storing an entire board in each slot of the TranspositionTable,
     * which would gobble up inordinate amounts of memory
     * @return
     */
    public int hashLock() {
        int hash = 0;
        for (int currPiece = 0; currPiece < pieceTypes; currPiece++) {
            BitSet board = bitBoards[currPiece];
            for (int currCell = 0; currCell < numberCells; currCell++) {
                if (board.get(currCell)) {
                    hash ^= zorbistHash.hashLock[currPiece][currCell];
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

    public boolean isValid(int cellId) {
        return cellId >= 0 && cellId < numberCells;
    }
}
