package com.pactstudios.games.tafl.core.es.model.ai.optimization;

import java.util.BitSet;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.EvaluationType;

public class Move implements Poolable {
    public int pieceType;

    public int source;
    public int destination;

    public int eval;
    public EvaluationType evalType;
    public int searchDepth;

    public BitSet capturedPieces = new BitSet(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);

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

    public void copy(Move move) {
        pieceType = move.pieceType;
        source = move.source;
        destination = move.destination;

        eval = move.eval;
        evalType = move.evalType;
        searchDepth = move.searchDepth;
    }
}
