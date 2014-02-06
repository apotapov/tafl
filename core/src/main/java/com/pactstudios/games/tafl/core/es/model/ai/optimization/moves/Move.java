package com.pactstudios.games.tafl.core.es.model.ai.optimization.moves;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.pactstudios.games.tafl.core.enums.EvaluationType;

public abstract class Move<T extends Move<?>> implements Poolable {

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
    public abstract T clone();
}
