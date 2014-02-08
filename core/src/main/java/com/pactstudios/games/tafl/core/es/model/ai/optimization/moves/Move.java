package com.pactstudios.games.tafl.core.es.model.ai.optimization.moves;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.pactstudios.games.tafl.core.enums.EvaluationType;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class Move implements Poolable {

    public static Pool<Move> movePool;

    private static final int DEFAULT_BOARD_SIZE = 64;

    public int pieceType;

    public int source;
    public int destination;

    public int eval;
    public EvaluationType evalType;
    public int searchDepth;

    public BitBoard capturedPieces;

    public Move() {
        capturedPieces = new BitBoard(DEFAULT_BOARD_SIZE);
    }

    public Move(int boardSize) {
        capturedPieces = new BitBoard(boardSize);
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
        move.pieceType = pieceType;
        move.source = source;
        move.destination = destination;
        move.eval = eval;
        move.evalType = evalType;
        move.searchDepth = searchDepth;
        return move;
    }

    @Override
    public String toString() {
        return pieceType + ": " + source + " -> " + destination;
    }
}
