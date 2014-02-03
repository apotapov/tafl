package com.pactstudios.games.tafl.core.es.model.ai;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;

public class MiniMaxStrategy implements AiStrategy {

    BoardEvaluator boardEvaluator;
    int maxDepth;


    Pool<Array<TaflMove>> arrayPool;

    public MiniMaxStrategy(BoardEvaluator boardEvaluator, int maxDepth) {
        this.boardEvaluator = boardEvaluator;
        this.maxDepth = maxDepth;
        arrayPool = new Pool<Array<TaflMove>>(){
            @Override
            protected Array<TaflMove> newObject() {
                return new Array<TaflMove>();
            }

            @Override
            public void free (Array<TaflMove> object) {
                object.size = 0;
                super.free(object);
            }
        };
    }


    @Override
    public TaflMove search(TaflMatch match) {
        TaflMove move = max(match, null, this.maxDepth);
        System.out.println(move.eval);
        return move;
    }

    protected void evaluate(TaflMatch match, TaflMove move) {
        move.eval = boardEvaluator.evaluate(match);
    }

    protected TaflMove max(TaflMatch match, TaflMove previousMove, int depth) {
        match.simulateMove(previousMove);
        if (depth > 0) {
            Array<TaflMove> legalMoves = getLegalMoves(match);
            TaflMove maxMove = chooseMax(match, depth, legalMoves);
            if (previousMove != null) {
                previousMove.eval = maxMove.eval;
            } else {
                previousMove = maxMove;
            }
        } else {
            evaluate(match, previousMove);
        }
        match.rollBackSimulatedMove();

        return previousMove;
    }

    private TaflMove chooseMax(TaflMatch match, int depth, Array<TaflMove> legalMoves) {
        Array<TaflMove> equalMoves = arrayPool.obtain();
        int max = Integer.MIN_VALUE;
        for (TaflMove move : legalMoves) {
            min(match, move, depth - 1);
            if (move.eval == Constants.AiConstants.WIN) {
                TaflMove maxMove = move.clone();
                cleanUp(legalMoves, equalMoves);
                return maxMove;
            } else if (max < move.eval) {
                max = move.eval;
                equalMoves.clear();
                equalMoves.add(move);
            } else if (max == move.eval) {
                equalMoves.add(move);
            } else {
                TaflMove.movePool.free(move);
            }
        }
        TaflMove maxMove = equalMoves.random().clone();
        cleanUp(legalMoves, equalMoves);
        return maxMove;
    }

    protected void min(TaflMatch match, TaflMove previousMove, int depth) {
        match.simulateMove(previousMove);
        if (depth > 0) {
            Array<TaflMove> legalMoves = getLegalMoves(match);
            if (previousMove != null) {
                previousMove.eval = chooseMin(match, depth, legalMoves);
            }
        } else {
            evaluate(match, previousMove);
        }
        match.rollBackSimulatedMove();
    }

    private int chooseMin(TaflMatch match, int depth,
            Array<TaflMove> legalMoves) {
        Array<TaflMove> equalMoves = arrayPool.obtain();
        int min = Integer.MAX_VALUE;
        for (TaflMove move : legalMoves) {
            max(match, move, depth - 1);
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
                TaflMove.movePool.free(move);
            }
        }
        int value = equalMoves.random().eval;
        cleanUp(legalMoves, equalMoves);
        return value;
    }

    private Array<TaflMove> getLegalMoves(TaflMatch match) {
        Array<TaflMove> legalMoves = arrayPool.obtain();

        BitSet bitBoard = match.currentBitBoard();
        for (int source = bitBoard.nextSetBit(0); source >= 0; source = bitBoard.nextSetBit(source+1)) {
            BitSet moves = match.rulesEngine.legalMoves(source);
            for (int dest = moves.nextSetBit(0); dest >= 0; dest = moves.nextSetBit(dest+1)) {
                TaflMove move = TaflMove.movePool.obtain();
                move.pieceType = match.turn;
                move.source = source;
                move.destination = dest;
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    private void cleanUp(Array<TaflMove> legalMoves, Array<TaflMove> equalMoves) {
        TaflMove.movePool.freeAll(equalMoves);
        arrayPool.free(equalMoves);
        arrayPool.free(legalMoves);
    }
}
