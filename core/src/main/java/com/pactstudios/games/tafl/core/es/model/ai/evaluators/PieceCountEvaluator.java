package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class PieceCountEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(TaflMatch match) {
        int value = 0;

        int winner = match.rulesEngine.checkWinner();
        if (winner != Constants.BoardConstants.NO_TEAM) {
            value = winner == match.turn ? Constants.AiConstants.WIN : Constants.AiConstants.LOSS;
        } else {
            value += match.board.whiteBitBoard().cardinality();
            value -= match.board.blackBitBoard().cardinality();

            if (match.turn == Constants.BoardConstants.BLACK_TEAM) {
                value *= -1;
            }
        }

        return value;
    }

}
