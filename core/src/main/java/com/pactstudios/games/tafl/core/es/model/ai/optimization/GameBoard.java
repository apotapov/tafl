package com.pactstudios.games.tafl.core.es.model.ai.optimization;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;

public abstract class GameBoard<T extends Move<?>> {

    public static final int NUMBER_OF_TEAMS = 2;

    public int dimensions;
    public int pieceTypes;
    public int boardSize;

    public BitBoard[] bitBoards;

    public BitBoard[] rows;
    public BitBoard[] columns;

    public ZorbistHash zorbistHash;

    Array<T> simulatedMoves;
    public Array<T> undoStack;

    public int hashCode;
    public int hashLock;

    int applyCount = 0;
    int undoCount = 0;

    public GameBoard(int dimensions, int pieceTypes, ZorbistHash zorbistHash) {
        this.dimensions = dimensions;
        this.pieceTypes = pieceTypes;
        this.boardSize = dimensions * dimensions;
        this.zorbistHash = zorbistHash;

        this.simulatedMoves = new Array<T>();
        this.undoStack = new Array<T>();

        bitBoards = new BitBoard[pieceTypes];
        for (int i = 0; i < pieceTypes; i++) {
            bitBoards[i] = new BitBoard(boardSize);
        }

        rows = new BitBoard[dimensions];
        for (int i = 0; i < dimensions; i++) {
            rows[i] = new BitBoard(boardSize);
            rows[i].set(i * dimensions, i * dimensions + dimensions);
        }
        columns = new BitBoard[dimensions];
        for (int i = 0; i < dimensions; i++) {
            columns[i] = new BitBoard(boardSize);
            for (int j = 0; j < dimensions; j++) {
                columns[i].set(j * dimensions + i);
            }
        }
    }

    public void applyMove(T move, boolean simulate) {
        BitBoard bitBoard = bitBoards[move.pieceType];
        bitBoard.clear(move.source);
        bitBoard.set(move.destination);

        hashCode ^= zorbistHash.hash[move.pieceType][move.source];
        hashCode ^= zorbistHash.hash[move.pieceType][move.destination];
        hashLock ^= zorbistHash.hashLock[move.pieceType][move.source];
        hashLock ^= zorbistHash.hashLock[move.pieceType][move.destination];

        if (!simulate) {
            @SuppressWarnings("unchecked")
            T clone = (T) move.clone();
            undoStack.add(clone);
        }
    }

    public T undoMove() {
        if (undoStack.size > 0) {
            T move = undoStack.pop();
            undoMove(move);
            return move;
        }
        return null;
    }

    private void undoMove(T move) {
        BitBoard bitBoard = bitBoards[move.pieceType];
        bitBoard.clear(move.destination);
        bitBoard.set(move.source);

        hashCode ^= zorbistHash.hash[move.pieceType][move.source];
        hashCode ^= zorbistHash.hash[move.pieceType][move.destination];
        hashLock ^= zorbistHash.hashLock[move.pieceType][move.source];
        hashLock ^= zorbistHash.hashLock[move.pieceType][move.destination];

        addPieces((move.pieceType + 1) % 2, move.capturedPieces);
    }

    public void addPiece(int team, int piece) {
        bitBoards[team].set(piece);

        hashCode ^= zorbistHash.hash[team][piece];
        hashLock ^= zorbistHash.hashLock[team][piece];
    }

    public void addPieces(int team, BitBoard pieces) {
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            bitBoards[team].set(i);

            hashCode ^= zorbistHash.hash[team][i];
            hashLock ^= zorbistHash.hashLock[team][i];
        }
    }

    public void removePieces(int team, BitBoard pieces) {
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            bitBoards[team].clear(i);
            hashCode ^= zorbistHash.hash[team][i];
            hashLock ^= zorbistHash.hashLock[team][i];
        }
    }

    public void simulateMove(T move) {
        applyMove(move, true);
        move.capturedPieces.set(getCapturedPieces(move));
        removePieces((move.pieceType + 1) % 2, move.capturedPieces);
        simulatedMoves.add(move);
    }

    protected abstract BitBoard getCapturedPieces(T move);

    public void undoSimulatedMove() {
        if (simulatedMoves.size > 0) {
            T move = simulatedMoves.pop();
            undoMove(move);
        }
    }

    public void reset() {
        for (BitBoard bitBoard : bitBoards) {
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
            BitBoard board = bitBoards[currPiece];
            // Search for all pieces on all cells. We could optimize here: not
            // looking for pawns on the back row (or the eight row), getting out
            // of the "currSqaure" loop once we found one king of one color,
            // etc.
            // But for simplicity's sake, we'll keep things generic.
            for (int currCell = 0; currCell < boardSize; currCell++) {
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
            BitBoard board = bitBoards[currPiece];
            for (int currCell = 0; currCell < boardSize; currCell++) {
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
        return cellId >= 0 && cellId < boardSize;
    }

    public boolean inRow(int cellId, int other) {
        return rows[cellId / dimensions].get(other);
    }

    public boolean inColumn(int cellId, int other) {
        return columns[cellId % dimensions].get(other);
    }

    public BitBoard getRow(int cellId) {
        return rows[cellId / dimensions];
    }

    public BitBoard getColumn(int cellId) {
        return columns[cellId % dimensions];
    }
}
