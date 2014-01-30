package com.pactstudios.games.tafl.core.es.model.ai.optimization;

import com.badlogic.gdx.utils.Pool.Poolable;

public class Move implements Poolable {

    public enum MoveType {
        NORMAL_MOVE,
        CAPTURE_MOVE;
    }

    public int movingPiece;

    public int sourceSquare;
    public int destinationSquare;

    public int eval;
    public EvaluationType evalType;
    public int searchDepth;

    public int[] capturedPieces;
    public MoveType moveType;

    @Override
    public void reset() {
        movingPiece = 0;
        sourceSquare = 0;
        destinationSquare = 0;
        eval = 0;
        evalType = null;
        searchDepth = 0;
    }

    public void copy(Move move) {
        movingPiece = move.movingPiece;
        sourceSquare = move.sourceSquare;
        destinationSquare = move.destinationSquare;

        eval = move.eval;
        evalType = move.evalType;
        searchDepth = move.searchDepth;
    }
}
