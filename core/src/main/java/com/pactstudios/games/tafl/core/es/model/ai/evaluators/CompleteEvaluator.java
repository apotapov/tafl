package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;

public class CompleteEvaluator implements BoardEvaluator<TaflBoard> {

    BlackCompleteEvaluator blackEval;
    WhiteCompleteEvaluator whiteEval;

    public CompleteEvaluator(int boardSize, int dimensions) {
        blackEval = new BlackCompleteEvaluator(boardSize, dimensions);
        whiteEval = new WhiteCompleteEvaluator(boardSize);
    }

    @Override
    public int evaluate(TaflBoard board, int turn) {
        board.rules.generateLegalMoves(Constants.BoardConstants.WHITE_TEAM);
        board.rules.generateLegalMoves(Constants.BoardConstants.BLACK_TEAM);
        return turn == Constants.BoardConstants.WHITE_TEAM ?
                whiteEval.evaluate(board, turn) :
                    blackEval.evaluate(board, turn);
    }
}
