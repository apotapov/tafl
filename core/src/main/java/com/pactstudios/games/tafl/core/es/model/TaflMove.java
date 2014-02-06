package com.pactstudios.games.tafl.core.es.model;

import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;

public class TaflMove extends Move<TaflMove> {

    public static final Pool<TaflMove> movePool = new Pool<TaflMove>() {
        @Override
        protected TaflMove newObject() {
            return new TaflMove(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);
        }
    };

    public MatchLogEntry entry;

    public TaflMove(int boardSize) {
        super(boardSize);
    }

    @Override
    public TaflMove clone() {
        TaflMove move = movePool.obtain();
        move.pieceType = pieceType;
        move.source = source;
        move.destination = destination;
        move.eval = eval;
        move.evalType = evalType;
        move.searchDepth = searchDepth;
        move.entry = entry;
        return move;
    }

    @Override
    public void reset() {
        super.reset();
        entry = null;
    }

    @Override
    public String toString() {
        return pieceType + ": " + source + " -> " + destination;
    }
}
