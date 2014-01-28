package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class PieceCountEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(TaflMatch match, Team team) {
        int value = 0;

        Team winner = match.rulesEngine.checkWinner();
        if (winner != null) {
            value = winner == team ? Constants.AiConstants.WIN : Constants.AiConstants.LOSS;
        } else {
            for (int i = 0; i < match.board.dimensions; i++) {
                for (int j = 0; j < match.board.dimensions; j++) {
                    ModelCell cell = match.board.getCell(i, j);
                    if (cell.piece != null) {
                        if (cell.piece.type.team == team) {
                            value++;
                        } else {
                            value--;
                        }
                    }
                }
            }
        }

        return value;
    }

}
