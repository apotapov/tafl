package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.Team;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class PieceCountEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(TaflMatch match) {
        int value = 0;

        Team winner = match.rulesEngine.checkWinner();
        if (winner != null) {
            value = winner == match.turn ? Constants.AiConstants.WIN : Constants.AiConstants.LOSS;
        } else {
            value += match.board.whiteBitBoard().cardinality();
            value -= match.board.blackBitBoard().cardinality();

            if (match.turn == Team.BLACK) {
                value *= -1;
            }
        }

        return value;
    }

}
