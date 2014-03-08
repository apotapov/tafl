package com.captstudios.games.tafl.core.es.model.ai.optimization;

import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;

public abstract class GameBoard {

    public int dimensions;
    public int pieceTypes;
    public int boardSize;

    public BitBoard[] bitBoards;

    public BitBoard[] rows;
    public BitBoard[] columns;

    public ZorbistHash zorbistHash;

    public Array<Move> undoStack;
    public Array<Move> simulatedStack;

    public int hashCode;

    int applyCount = 0;
    int undoCount = 0;

    public GameBoard(int dimensions, int pieceTypes, ZorbistHash zorbistHash) {
        this.dimensions = dimensions;
        this.pieceTypes = pieceTypes;
        this.boardSize = dimensions * dimensions;
        this.zorbistHash = zorbistHash;

        this.undoStack = new Array<Move>();
        this.simulatedStack = new Array<Move>();

        bitBoards = new BitBoard[pieceTypes];
        for (int i = 0; i < pieceTypes; i++) {
            bitBoards[i] = new BitBoard(boardSize);
        }

        this.hashCode = calculateHashCode();

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

    public void applyMove(Move move, boolean simulate) {
        BitBoard bitBoard = bitBoards[move.pieceType];
        bitBoard.clear(move.source);
        bitBoard.set(move.destination);

        hashCode ^= zorbistHash.hash[move.pieceType][move.source];
        hashCode ^= zorbistHash.hash[move.pieceType][move.destination];

        if (!simulate) {
            Move clone = move.clone();
            undoStack.add(clone);
        } else {
            simulatedStack.add(move);
        }
    }

    public Move undoMove() {
        if (undoStack.size > 0) {
            Move move = undoStack.pop();
            undoMove(move);
            return move;
        }
        return null;
    }

    protected void undoMove(Move move) {
        BitBoard bitBoard = bitBoards[move.pieceType];
        bitBoard.clear(move.destination);
        bitBoard.set(move.source);

        hashCode ^= zorbistHash.hash[move.pieceType][move.source];
        hashCode ^= zorbistHash.hash[move.pieceType][move.destination];

        addPieces((move.pieceType + 1) % 2, move.capturedPieces);
    }

    public void addPiece(int pieceType, int piece) {
        bitBoards[pieceType].set(piece);

        hashCode ^= zorbistHash.hash[pieceType][piece];
    }

    public void addPieces(int pieceType, BitBoard pieces) {
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            addPiece(pieceType, i);
        }
    }

    public void removePieces(int pieceType, BitBoard pieces) {
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            bitBoards[pieceType].clear(i);
            hashCode ^= zorbistHash.hash[pieceType][i];
        }
    }

    public void simulateMove(Move move) {
        applyMove(move, true);
        move.capturedPieces.set(getCapturedPieces(move));
        removePieces((move.pieceType + 1) % 2, move.capturedPieces);
    }

    protected abstract BitBoard getCapturedPieces(Move move);

    public void undoSimulatedMove(Move move) {
        undoMove(move);
        simulatedStack.pop();
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
        return hashCode;
    }

    private int calculateHashCode() {
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
