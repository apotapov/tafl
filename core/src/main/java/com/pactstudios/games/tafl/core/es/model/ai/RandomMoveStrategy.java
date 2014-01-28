package com.pactstudios.games.tafl.core.es.model.ai;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class RandomMoveStrategy implements AiStrategy {

    Array<Move> legalMoves;

    public RandomMoveStrategy() {
        legalMoves = new Array<Move>();
    }

    @Override
    public Move search(TaflMatch match, Team team) {
        Pools.freeAll(legalMoves);
        legalMoves.clear();
        for (int i = 0; i < match.board.dimensions; i++) {
            for (int j = 0; j < match.board.dimensions; j++) {
                ModelCell start = match.board.getCell(i, j);
                if (start.piece != null && start.piece.type.team == team) {
                    Array<ModelCell> moves = match.rulesEngine.legalMoves(start);
                    for (ModelCell end : moves) {
                        Move move = Pools.obtain(Move.class);
                        move.piece = start.piece;
                        move.start = start;
                        move.end = end;
                        legalMoves.add(move);
                    }
                }
            }
        }
        return legalMoves.random();
    }
}
