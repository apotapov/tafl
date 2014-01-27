package com.pactstudios.games.tafl.core.es.model.board;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class Move implements Poolable {
    public GamePiece piece;
    public ModelCell start;
    public ModelCell end;
    public int value;

    public Array<GamePiece> captured = new Array<GamePiece>();

    public MatchLogEntry entry;

    @Override
    public Move clone() {
        Move move = Pools.obtain(Move.class);
        move.piece = piece;
        move.start = start;
        move.end = end;
        move.value = value;
        move.captured.addAll(captured);
        move.entry = entry;
        return move;
    }

    @Override
    public void reset() {
        piece = null;
        start = null;
        end = null;
        value = 0;
        captured.clear();
        entry = null;
    }
}
