package com.captstudios.games.tafl.core.es.model.ai.optimization.moves;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.captstudios.games.tafl.core.enums.EvaluationType;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class Move implements Poolable {

    public Pool<Move> movePool;

    public int pieceType;

    public int source;
    public int destination;

    public int eval;
    public EvaluationType evalType;
    public int searchDepth;

    public BitBoard capturedPieces;

    /**
     * For serialization only.
     */
    @Deprecated
    public Move() {
    }

    public Move(Pool<Move> movePool, int boardSize) {
        this.movePool = movePool;
        this.capturedPieces = new BitBoard(boardSize);
    }

    @Override
    public void reset() {
        pieceType = 0;
        source = 0;
        destination = 0;
        eval = 0;
        evalType = null;
        searchDepth = 0;
        capturedPieces.clear();
    }

    @Override
    public Move clone() {
        Move move = movePool.obtain();
        move.pieceType = this.pieceType;
        move.source = this.source;
        move.destination = this.destination;
        move.eval = this.eval;
        move.evalType = this.evalType;
        move.searchDepth = this.searchDepth;
        move.capturedPieces.set(this.capturedPieces);
        return move;
    }

    @Override
    public String toString() {
        return pieceType + ": " + source + " -> " + destination;
    }

    public void free() {
        movePool.free(this);
    }

    public Move populate(Move move) {
        this.pieceType = move.pieceType;
        this.source = move.source;
        this.destination = move.destination;
        this.eval = move.eval;
        this.evalType = move.evalType;
        this.searchDepth = move.searchDepth;
        this.capturedPieces.set(move.capturedPieces);
        return this;
    }
}
