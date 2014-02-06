package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class CompleteEvaluator implements BoardEvaluator<TaflBoard> {

    private static final int PIECE_VALUE = 10;
    private static final int PIECE_VULNERABILITY_VALUE = 3;
    private static final int OPPOSITE_VULNERABILITY_VALUE = 1;

    public int Size = 11;
    public int Center = 5;
    public int[][][] Field = new int[Size][Size][1];

    public BitBoard tempBitBoard;

    public CompleteEvaluator(int boardSize) {
        tempBitBoard = new BitBoard(boardSize);
    }

    @Override
    public int evaluate(TaflBoard board, int turn) {
        int value = 0;

        int winner = board.rulesEngine.checkWinner();
        if (winner != Constants.BoardConstants.NO_TEAM) {
            value = winner == turn ? Constants.AiConstants.WIN : Constants.AiConstants.LOSS;
        } else {
            value += checkKingWinPotential(board, turn);
            value += checkIfKingOnEdge(board, turn);
            value += checkKingRisk(board, turn);
            value += materialBallance(board, turn);
            value += checkRankAndFiles(board, turn);
            value += boardControl(board, turn);
            value += moveRepetition(board, turn);
        }

        return value;
    }

    private int moveRepetition(TaflBoard board, int turn) {
        // TODO Auto-generated method stub
        return 0;
    }

    private int boardControl(TaflBoard board, int turn) {
        // TODO Auto-generated method stub
        return 0;
    }

    private int materialBallance(TaflBoard board, int turn) {

        int value = 0;
        int oppositTeam = (turn + 1) % 2;

        BitBoard turnBoard = board.bitBoards[turn];
        BitBoard oppositeBoard = board.bitBoards[oppositTeam];

        board.whiteBitBoard().clear(board.king);

        for (int i = turnBoard.nextSetBit(0); i >= 0; i = turnBoard.nextSetBit(i+1)) {
            value += PIECE_VALUE - (board.rulesEngine.isVulnerable(turn, i) ? PIECE_VULNERABILITY_VALUE : 0);
        }

        for (int i = oppositeBoard.nextSetBit(0); i >= 0; i = oppositeBoard.nextSetBit(i+1)) {
            value -= PIECE_VALUE - (board.rulesEngine.isVulnerable(oppositTeam, i) ? OPPOSITE_VULNERABILITY_VALUE : 0);
        }

        board.whiteBitBoard().set(board.king);

        return value;
    }

    private int checkRankAndFiles(TaflBoard board, int turn) {
        float value = 0;

        BitBoard blackBoard = board.blackBitBoard();

        for (int i = 0; i < board.dimensions; i++) {
            int blackRowCount = BitBoard.intersectionCount(blackBoard, board.rows[i]);
            int blackColumnCount = BitBoard.intersectionCount(blackBoard, board.columns[i]);
            if (turn == Constants.BoardConstants.WHITE_TEAM) {
                if (blackRowCount == 0) {
                    value++;
                }
                if (blackColumnCount == 0) {
                    value++;
                }
            } else {
                value += 1 + blackRowCount / 4.0f;
                value += 1 + blackColumnCount / 4.0f;
            }
        }
        return (int) value;
    }

    private int checkKingRisk(TaflBoard board, int turn) {
        // TODO Auto-generated method stub
        return 0;
    }

    private int checkIfKingOnEdge(TaflBoard board, int turn) {
        // TODO Auto-generated method stub
        return 0;
    }

    private int checkKingWinPotential(TaflBoard board, int turn) {
        // TODO Auto-generated method stub
        return 0;
    }

}
