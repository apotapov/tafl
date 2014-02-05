package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;

public class CompleteEvaluator implements BoardEvaluator<TaflBoard> {

    @Override
    public int evaluate(TaflBoard board, int turn) {
        int value = 0;

        int winner = board.rulesEngine.checkWinner();
        if (winner != Constants.BoardConstants.NO_TEAM) {
            value = winner == turn ? Constants.AiConstants.WIN : Constants.AiConstants.LOSS;
        } else {
            value += board.whiteBitBoard().cardinality();
            value -= board.blackBitBoard().cardinality();

            if (turn == Constants.BoardConstants.BLACK_TEAM) {
                value *= -1;
            }
        }

        return value;
    }

}
