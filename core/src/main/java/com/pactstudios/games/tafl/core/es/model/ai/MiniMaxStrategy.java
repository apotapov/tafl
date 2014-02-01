package com.pactstudios.games.tafl.core.es.model.ai;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.board.Move;

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
    public Move search(TaflMatch match) {
        Move move = max(match, null, this.maxDepth);
        System.out.println(move.eval);
        return move;
    }

    protected void evaluate(TaflMatch match, Move move) {
        move.eval = boardEvaluator.evaluate(match);
    }

    protected Move max(TaflMatch match, Move previousMove, int depth) {
        match.simulateMove(previousMove);
        if (depth > 0) {
            Array<Move> legalMoves = getLegalMoves(match);
            Move maxMove = chooseMax(match, depth, legalMoves);
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

    private Move chooseMax(TaflMatch match, int depth, Array<Move> legalMoves) {
        Array<Move> equalMoves = arrayPool.obtain();
        int max = Integer.MIN_VALUE;
        for (Move move : legalMoves) {
            min(match, move, depth - 1);
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

    protected void min(TaflMatch match, Move previousMove, int depth) {
        match.simulateMove(previousMove);
        if (depth > 0) {
            Array<Move> legalMoves = getLegalMoves(match);
            if (previousMove != null) {
                previousMove.eval = chooseMin(match, depth, legalMoves);
            }
        } else {
            evaluate(match, previousMove);
        }
        match.rollBackSimulatedMove();
    }

    private int chooseMin(TaflMatch match, int depth,
            Array<Move> legalMoves) {
        Array<Move> equalMoves = arrayPool.obtain();
        int min = Integer.MAX_VALUE;
        for (Move move : legalMoves) {
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
                Move.movePool.free(move);
            }
        }
        int value = equalMoves.random().eval;
        cleanUp(legalMoves, equalMoves);
        return value;
    }

    private Array<Move> getLegalMoves(TaflMatch match) {
        Array<Move> legalMoves = arrayPool.obtain();

        BitSet bitBoard = match.currentBitBoard();
        for (int source = bitBoard.nextSetBit(0); source >= 0; source = bitBoard.nextSetBit(source+1)) {
            BitSet moves = match.rulesEngine.legalMoves(source);
            for (int dest = moves.nextSetBit(0); dest >= 0; dest = moves.nextSetBit(dest+1)) {
                Move move = Move.movePool.obtain();
                move.pieceType = match.turn;
                move.source = source;
                move.destination = dest;
                legalMoves.add(move);
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
