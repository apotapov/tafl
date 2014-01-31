package com.pactstudios.games.tafl.core.es.model.board;

import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;

public class Move extends com.pactstudios.games.tafl.core.es.model.ai.optimization.Move {

    public static final Pool<Move> movePool = new Pool<Move>() {
        @Override
        protected Move newObject() {
            return new Move();
        }
    };

    public MatchLogEntry entry;

    @Override
    public Move clone() {
        Move move = movePool.obtain();
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
}
