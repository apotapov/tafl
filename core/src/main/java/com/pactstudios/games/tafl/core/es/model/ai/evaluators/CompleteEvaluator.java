package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class CompleteEvaluator implements BoardEvaluator<TaflBoard> {

    private static final int[] CORNER_PROTECTION_CELLS = {
        2, 12, 22,
        8, 20, 32,
        88, 100, 112,
        98, 108, 118
    };

    private static final int WIN = 100000;
    private static final int LOSS = -WIN;

    private static final int PIECE_VALUE = 10;
    private static final int PIECE_VULNERABILITY_VALUE = 3;
    private static final int OPPOSITE_VULNERABILITY_VALUE = 1;

    private static final int KING_MOBILITY = 5;
    private static final int KING_EMPTY_RANK_2 = 90;
    private static final int KING_EMPTY_RANK_3 = 45;
    private static final int KING_EMPTY_FILE_2 = 90;
    private static final int KING_EMPTY_FILE_3 = 45;

    private static final int CORNER_PROTECTION_BONUS = 5;
    private static final int DANGER_SQUARE_BONUS = -1;

    public int[][][] Field = new int[11][11][1];

    public BitBoard tempBitBoard;
    public BitBoard allPiecesBoard;

    public BitBoard cornerProtection;

    public CompleteEvaluator(int boardSize) {
        tempBitBoard = new BitBoard(boardSize);
        allPiecesBoard = new BitBoard(boardSize);

        cornerProtection = new BitBoard(boardSize);
        for (int element : CORNER_PROTECTION_CELLS) {
            cornerProtection.set(element);
        }
    }

    @Override
    public int evaluate(TaflBoard board, int turn) {
        int value = 0;

        if (board.rules.isGameOver(turn)) {
            int winner = board.rules.checkWinner();
            if (winner == Constants.BoardConstants.NO_TEAM) {
                return 0;
            } else if (winner == turn) {
                return WIN;
            } else {
                return LOSS;
            }
        } else {

            value += kingMobility(board, turn);
            value += squareWeight(board, turn);
            value += materialBallance(board, turn);
            value += checkRankAndFiles(board, turn);
            value += checkKingRanksAndFiles(board, turn);

            //
            //            value += checkIfKingOnEdge(board, turn);
            //            value += checkKingRisk(board, turn);
            //
            //            if (turn == Constants.BoardConstants.BLACK_TEAM) {
            //                value *= KING_ON_EDGE_ATTACKER_ADJUSTMENT;
            //            }
            //
            //
            //            value += checkRankAndFiles(board, turn);
            //            value += moveRepetition(board, turn);
        }

        return value;
    }

    private int kingMobility(TaflBoard board, int turn) {
        int value = 0;

        value += board.rules.getLegalMoves(Constants.BoardConstants.WHITE_TEAM, board.king).cardinality() * KING_MOBILITY;

        if (turn == Constants.BoardConstants.BLACK_TEAM) {
            value *= -1;
        }

        return value;
    }

    private int squareWeight(TaflBoard board, int turn) {
        int value = 0;
        int oppositTeam = (turn + 1) % 2;

        BitBoard turnBoard = board.bitBoards[turn];
        BitBoard oppositeBoard = board.bitBoards[oppositTeam];

        for (int i = turnBoard.nextSetBit(0); i >= 0; i = turnBoard.nextSetBit(i + 1)) {
            if (cornerProtection.get(i)) {
                value += CORNER_PROTECTION_BONUS;
            } else if (board.nearCorners.get(i)) {
                value += DANGER_SQUARE_BONUS;
            } else if (board.nearCenter.get(i)) {
                value += DANGER_SQUARE_BONUS;
            }
        }

        for (int i = oppositeBoard.nextSetBit(0); i >= 0; i = oppositeBoard.nextSetBit(i + 1)) {
            if (cornerProtection.get(i)) {
                value -= CORNER_PROTECTION_BONUS;
            } else if (board.nearCorners.get(i)) {
                value -= DANGER_SQUARE_BONUS;
            } else if (board.nearCenter.get(i)) {
                value -= DANGER_SQUARE_BONUS;
            }
        }


        // TODO Auto-generated method stub
        return value;
    }

    private int materialBallance(TaflBoard board, int turn) {
        int value = 0;
        int oppositTeam = (turn + 1) % 2;

        BitBoard turnBoard = board.bitBoards[turn];
        BitBoard oppositeBoard = board.bitBoards[oppositTeam];

        board.whiteBitBoard().clear(board.king);

        for (int i = turnBoard.nextSetBit(0); i >= 0; i = turnBoard.nextSetBit(i + 1)) {
            value += PIECE_VALUE
                    - (board.rules.isVulnerable(turn, i) ? PIECE_VULNERABILITY_VALUE
                            : 0);
        }

        for (int i = oppositeBoard.nextSetBit(0); i >= 0; i = oppositeBoard.nextSetBit(i + 1)) {
            value -= PIECE_VALUE
                    - (board.rules.isVulnerable(oppositTeam, i) ? OPPOSITE_VULNERABILITY_VALUE
                            : 0);
        }

        board.whiteBitBoard().set(board.king);

        return value;
    }

    private int checkRankAndFiles(TaflBoard board, int turn) {
        float value = 0;

        BitBoard blackBoard = board.blackBitBoard();

        for (int i = 0; i < board.dimensions; i++) {
            int blackRowCount = BitBoard.intersectionCount(blackBoard,
                    board.rows[i]);
            int blackColumnCount = BitBoard.intersectionCount(blackBoard,
                    board.columns[i]);
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

    private int checkKingRanksAndFiles(TaflBoard board, int turn) {
        int value = 0;

        // all the pieces that are in the same column as the king
        allPiecesBoard.set(board.whiteBitBoard()).or(board.blackBitBoard());
        tempBitBoard.set(allPiecesBoard).and(board.getColumn(board.king));

        int kingColumn = board.king % board.dimensions;
        int kingRow = board.king / board.dimensions;

        int piecesAbove = 0;
        for (int i = tempBitBoard.nextSetBit(board.king + 1); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
            piecesAbove++;
        }

        if (piecesAbove == 0) {
            if (kingColumn == 1 || kingColumn == board.dimensions - 2) {
                value += KING_EMPTY_RANK_2;
            } else if (kingColumn == 2 || kingColumn == board.dimensions - 3) {
                value += KING_EMPTY_RANK_3;
            }
        }

        int piecesBelow = 0;
        for (int i = tempBitBoard.prevSetBit(board.king - 1); i >= 0; i = tempBitBoard.prevSetBit(i-1)) {
            piecesBelow++;
        }

        if (piecesBelow == 0) {
            if (kingColumn == 1 || kingColumn == board.dimensions - 2) {
                value += KING_EMPTY_RANK_2;
            } else if (kingColumn == 2 || kingColumn == board.dimensions - 3) {
                value += KING_EMPTY_RANK_3;
            }
        }

        // all the pieces that are in the same row as the king
        tempBitBoard.set(allPiecesBoard).and(board.getRow(board.king));

        int piecesLeft = 0;
        for (int i = tempBitBoard.nextSetBit(board.king + 1); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
            piecesAbove++;
        }

        if (piecesLeft == 0) {
            if (kingRow == 1 || kingRow == board.dimensions - 2) {
                value += KING_EMPTY_FILE_2;
            } else if (kingRow == 2 || kingRow == board.dimensions - 3) {
                value += KING_EMPTY_FILE_3;
            }
        }

        int piecesRight = 0;
        for (int i = tempBitBoard.prevSetBit(board.king - 1); i >= 0; i = tempBitBoard.prevSetBit(i-1)) {
            piecesBelow++;
        }

        if (piecesRight == 0) {
            if (kingRow == 1 || kingRow == board.dimensions - 2) {
                value += KING_EMPTY_FILE_2;
            } else if (kingRow == 2 || kingRow == board.dimensions - 3) {
                value += KING_EMPTY_FILE_3;
            }
        }

        if (turn == Constants.BoardConstants.BLACK_TEAM) {
            value *= -1;
        }

        return value;
    }
}