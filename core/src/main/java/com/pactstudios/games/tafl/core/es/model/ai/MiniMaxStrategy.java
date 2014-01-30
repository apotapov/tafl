package com.pactstudios.games.tafl.core.es.model.ai;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class MiniMaxStrategy implements AiStrategy {

    BoardEvaluator boardEvaluator;
    int maxDepth;


    Pool<Array<Move>> arrayPool;

    public MiniMaxStrategy(BoardEvaluator boardEvaluator, int maxDepth) {
        this.boardEvaluator = boardEvaluator;
        this.maxDepth = maxDepth;
        arrayPool = new Pool<Array<Move>>(){
            @Override
            protected Array<Move> newObject() {
                return new Array<Move>();
            }

            @Override
            public void free (Array<Move> object) {
                object.size = 0;
                super.free(object);
            }
        };
    }


    @Override
    public Move search(TaflMatch match, Team team) {
        Move move = max(match, team, null, this.maxDepth);
        System.out.println(move.eval);
        return move;
    }

    protected void evaluate(TaflMatch match, Team team, Move move) {
        move.eval = boardEvaluator.evaluate(match, team);
    }

    protected Move max(TaflMatch match, Team team, Move previousMove, int depth) {
        match.simulateMove(previousMove);
        if (depth > 0) {
            Array<Move> legalMoves = getLegalMoves(match, team);
            Move maxMove = chooseMax(match, team, depth, legalMoves);
            if (previousMove != null) {
                previousMove.eval = maxMove.eval;
            } else {
                previousMove = maxMove;
            }
        } else {
            evaluate(match, team, previousMove);
        }
        match.rollBackSimulatedMove();

        return previousMove;
    }

    private Move chooseMax(TaflMatch match, Team team, int depth, Array<Move> legalMoves) {
        Array<Move> equalMoves = arrayPool.obtain();
        int max = Integer.MIN_VALUE;
        for (Move move : legalMoves) {
            min(match, team, move, depth - 1);
            if (move.eval == Constants.AiConstants.WIN) {
                Move maxMove = move.clone();
                cleanUp(legalMoves, equalMoves);
                return maxMove;
            } else if (max < move.eval) {
                max = move.eval;
                equalMoves.clear();
                equalMoves.add(move);
            } else if (max == move.eval) {
                equalMoves.add(move);
            } else {
                Move.movePool.free(move);
            }
        }
        Move maxMove = equalMoves.random().clone();
        cleanUp(legalMoves, equalMoves);
        return maxMove;
    }

    protected void min(TaflMatch match, Team team, Move previousMove, int depth) {
        match.simulateMove(previousMove);
        if (depth > 0) {
            Array<Move> legalMoves = getLegalMoves(match, team);
            if (previousMove != null) {
                previousMove.eval = chooseMin(match, team, depth, legalMoves);
            }
        } else {
            evaluate(match, team, previousMove);
        }
        match.rollBackSimulatedMove();
    }

    private int chooseMin(TaflMatch match, Team team, int depth,
            Array<Move> legalMoves) {
        Array<Move> equalMoves = arrayPool.obtain();
        int min = Integer.MAX_VALUE;
        for (Move move : legalMoves) {
            max(match, team, move, depth - 1);
            if (move.eval == Constants.AiConstants.LOSS) {
                cleanUp(legalMoves, equalMoves);
                return Constants.AiConstants.LOSS;
            } else if (min > move.eval) {
                min = move.eval;
                equalMoves.clear();
                equalMoves.add(move);
            } else if (min == move.eval) {
                equalMoves.add(move);
            } else {
                Move.movePool.free(move);
            }
        }
        int value = equalMoves.random().eval;
        cleanUp(legalMoves, equalMoves);
        return value;
    }

    private Array<Move> getLegalMoves(TaflMatch match, Team team) {
        Array<Move> legalMoves = arrayPool.obtain();
        for (int i = 0; i < match.board.dimensions; i++) {
            for (int j = 0; j < match.board.dimensions; j++) {
                ModelCell start = match.board.getCell(i, j);
                if (start.piece != null && start.piece.type.team == team) {
                    Array<ModelCell> moves = match.rulesEngine.legalMoves(start);
                    for (ModelCell end : moves) {
                        Move move = Move.movePool.obtain();
                        move.piece = start.piece;
                        move.start = start;
                        move.end = end;
                        legalMoves.add(move);
                    }
                }
            }
        }
        return legalMoves;
    }

    private void cleanUp(Array<Move> legalMoves, Array<Move> equalMoves) {
        Move.movePool.freeAll(equalMoves);
        arrayPool.free(equalMoves);
        arrayPool.free(legalMoves);
    }
}
