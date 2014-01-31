package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import java.util.BitSet;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class PieceCountEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(TaflMatch match, Team team) {
        int value = 0;

        //        Team winner = match.rulesEngine.checkWinner();
        //        if (winner != null) {
        //            value = winner == team ? Constants.AiConstants.WIN : Constants.AiConstants.LOSS;
        //        } else {
        BitSet pieces = match.board.bitBoards[Team.WHITE.bitBoardId()];
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            value++;
        }
        pieces = match.board.bitBoards[Team.BLACK.bitBoardId()];
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            value--;
        }
        if (team == Team.BLACK) {
            value *= -1;
        }
        //        }

        return value;
    }

}
