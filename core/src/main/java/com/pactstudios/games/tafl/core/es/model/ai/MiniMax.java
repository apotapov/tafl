package com.pactstudios.games.tafl.core.es.model.ai;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class MiniMax {

    public static final int MAX_TREE_DEPTH = 3;

    TaflMatch match;
    Team computer;

    Pool<Array<Move>> legalMovesPool;

    public MiniMax(TaflMatch match, Team computer) {
        this.match = match;
        this.computer = computer;

        legalMovesPool = new Pool<Array<Move>>(){
            @Override
            protected Array<Move> newObject() {
                return new Array<Move>();
            }

            @Override
            public void free (Array<Move> object) {
                object.clear();
                super.free(object);
            }
        };
    }


    public Move search() {
        return max(null, MAX_TREE_DEPTH);
    }

    protected void evaluate(Move previousMove) {
        previousMove.value = 0;
    }

    protected Move max(Move previousMove, int depth) {
        Move maxMove = null;
        match.simulateMove(previousMove);

        if (depth > 0) {
            Array<Move> legalMoves = legalMovesPool.obtain();
            for (int i = 0; i < match.board.dimensions; i++) {
                for (int j = 0; j < match.board.dimensions; j++) {
                    ModelCell start = match.board.getCell(i, j);
                    if (start.piece != null) {
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
            for (Move move : legalMoves) {
                min(move, depth - 1);
                if (maxMove == null || maxMove.value < move.value) {
                    maxMove = move;
                } else {
                    Pools.free(move);
                }
            }
            legalMovesPool.free(legalMoves);
        } else {
            evaluate(previousMove);
            maxMove = previousMove;
        }

        match.rollBackSimulatedMove();

        return maxMove;
    }

    protected Move min(Move previousMove, int depth) {
        Move minMove = null;
        if (previousMove != null) {
            match.simulateMove(previousMove);
        }
        if (depth > 0) {
            Array<Move> legalMoves = legalMovesPool.obtain();
            for (int i = 0; i < match.board.dimensions; i++) {
                for (int j = 0; j < match.board.dimensions; j++) {
                    ModelCell start = match.board.getCell(i, j);
                    if (start.piece != null) {
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
            for (Move move : legalMoves) {
                min(move, depth - 1);
                if (minMove == null || minMove.value > move.value) {
                    minMove = move;
                } else {
                    Pools.free(move);
                }
            }
            legalMovesPool.free(legalMoves);
        } else {
            evaluate(previousMove);
            minMove = previousMove;
        }
        if (previousMove != null) {
            match.rollBackSimulatedMove();
        }
        return minMove;
    }
}
