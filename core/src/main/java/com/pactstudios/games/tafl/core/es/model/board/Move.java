package com.pactstudios.games.tafl.core.es.model.board;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class Move extends com.pactstudios.games.tafl.core.es.model.ai.optimization.Move {

    public static final Pool<Move> movePool = new Pool<Move>() {
        @Override
        protected Move newObject() {
            return new Move();
        }
    };

    public GamePiece piece;
    public ModelCell start;
    public ModelCell end;

    public Array<GamePiece> captured = new Array<GamePiece>();

    public MatchLogEntry entry;

    @Override
    public Move clone() {
        Move move = movePool.obtain();
        move.piece = piece;
        move.start = start;
        move.end = end;
        move.sourceSquare = sourceSquare;
        move.destinationSquare = destinationSquare;
        move.eval = eval;
        move.evalType = evalType;
        move.searchDepth = searchDepth;
        move.captured.addAll(captured);
        move.entry = entry;
        return move;
    }

    @Override
    public void reset() {
        super.reset();
        piece = null;
        start = null;
        end = null;
        captured.clear();
        entry = null;
    }
}
