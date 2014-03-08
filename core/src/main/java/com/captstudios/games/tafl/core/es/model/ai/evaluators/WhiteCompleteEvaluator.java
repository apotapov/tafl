package com.captstudios.games.tafl.core.es.model.ai.evaluators;

import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.model.TaflBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class WhiteCompleteEvaluator implements BoardEvaluator<TaflBoard> {

    private static final int[] CORNER_PROTECTION_CELLS = {
        2, 12, 22,
        8, 20, 32,
        88, 100, 112,
        98, 108, 118
    };

    private static final int WIN = 100000;
    private static final int LOSS = -WIN;

    private static final int WHITE_PIECE_VALUE = 5;
    private static final int BLACK_PIECE_VALUE = 10;
    private static final int WHITE_PIECE_VULNERABILITY_VALUE = 3;
    private static final int BLACK_PIECE_VULNERABILITY_VALUE = 5;
    private static final int WHITE_OPPOSITE_PIECE_VULNERABILITY_VALUE = 4;
    private static final int BLACK_OPPOSITE_PIECE_VULNERABILITY_VALUE = 2;


    private static final int KING_MOBILITY = 10;
    private static final int KING_EMPTY_RANK_2 = 90;
    private static final int KING_EMPTY_RANK_3 = 45;
    private static final int KING_EMPTY_FILE_2 = 90;
    private static final int KING_EMPTY_FILE_3 = 70;

    private static final int CORNER_PROTECTION_BONUS = 15;
    private static final int DANGER_SQUARE_BONUS = -2;

    public BitBoard tempBitBoard;
    public BitBoard allPiecesBoard;

    public BitBoard cornerProtection;

    public WhiteCompleteEvaluator(int boardSize) {
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
        }

        return value;
    }

    private int kingMobility(TaflBoard board, int turn) {
        int value = 0;

        value += board.rules.getLegalMoves(Constants.BoardConstants.WHITE_TEAM, board.getKing()).cardinality() * KING_MOBILITY;

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

        int pieceValue = WHITE_PIECE_VALUE;
        int pieceVulnerability = WHITE_PIECE_VULNERABILITY_VALUE;
        int oppositeVulnerability = WHITE_OPPOSITE_PIECE_VULNERABILITY_VALUE;
        if (turn == Constants.BoardConstants.BLACK_TEAM) {
            pieceValue = BLACK_PIECE_VALUE;
            pieceVulnerability = BLACK_PIECE_VULNERABILITY_VALUE;
            oppositeVulnerability = BLACK_OPPOSITE_PIECE_VULNERABILITY_VALUE;
        }

        for (int i = turnBoard.nextSetBit(0); i >= 0; i = turnBoard.nextSetBit(i + 1)) {
            value += pieceValue
                    - (board.rules.isVulnerable(turn, i) ? pieceVulnerability
                            : 0);
        }

        for (int i = oppositeBoard.nextSetBit(0); i >= 0; i = oppositeBoard.nextSetBit(i + 1)) {
            value -= pieceValue
                    - (board.rules.isVulnerable(oppositTeam, i) ? oppositeVulnerability
                            : 0);
        }

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

        int king = board.getKing();

        // all the pieces that are in the same column as the king
        allPiecesBoard.set(board.whiteBitBoard()).or(board.blackBitBoard());
        tempBitBoard.set(allPiecesBoard).and(board.getColumn(king));

        int kingColumn = king % board.dimensions;
        int kingRow = king / board.dimensions;

        int piecesAbove = 0;
        for (int i = tempBitBoard.nextSetBit(king + 1); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
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
        for (int i = tempBitBoard.prevSetBit(king - 1); i >= 0; i = tempBitBoard.prevSetBit(i-1)) {
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
        tempBitBoard.set(allPiecesBoard).and(board.getRow(king));

        int piecesLeft = 0;
        for (int i = tempBitBoard.nextSetBit(king + 1); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
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
        for (int i = tempBitBoard.prevSetBit(king - 1); i >= 0; i = tempBitBoard.prevSetBit(i-1)) {
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

    //    private PlayerWarningEnum checkKingCaptureWarning() {
    //        int king = board.king;
    //        int surroundingEnemy = 0;
    //        BitBoard blackPositions = board.blackBitBoard();
    //
    //        int kingUp = king + board.dimensions;
    //        int kingDown = king - board.dimensions;
    //        int kingLeft = king - 1;
    //        if (kingLeft % board.dimensions == board.dimensions - 1) {
    //            kingLeft = Constants.BoardConstants.ILLEGAL_CELL;
    //        }
    //        int kingRight = king + 1;
    //        if (kingRight % board.dimensions == 0) {
    //            kingRight = Constants.BoardConstants.ILLEGAL_CELL;
    //        }
    //
    //        if (board.isValid(kingUp) && blackPositions.get(kingUp)) {
    //            surroundingEnemy++;
    //        }
    //        if (board.isValid(kingDown) && blackPositions.get(kingDown)) {
    //            surroundingEnemy++;
    //        }
    //        if (board.isValid(kingLeft) && blackPositions.get(kingLeft)) {
    //            surroundingEnemy++;
    //        }
    //        if (board.isValid(kingRight) && blackPositions.get(kingRight)) {
    //            surroundingEnemy++;
    //        }
    //
    //        if (surroundingEnemy >= Constants.GameConstants.KING_CAPTURE_THRESHHOLD) {
    //            if (lastKingWarning == null) {
    //                lastKingWarning = PlayerWarningEnum.WATCH_YOUR_KING;
    //                return lastKingWarning;
    //            }
    //        } else {
    //            lastKingWarning = null;
    //        }
    //        return null;
    //    }
    //
    //    private PlayerWarningEnum checkKingEscapeWarning() {
    //        allPieces.set(board.whiteBitBoard()).or(board.blackBitBoard());
    //        BitBoard kingLegalMoves = calculateMoves(board.king);
    //
    //        int escapeMoveCount = 0;
    //        boolean certainEscape = false;
    //        if (board.nearCorners.get(board.king)) {
    //            certainEscape = true;
    //        } else {
    //            escapeMoveCount = BitBoard.intersectionCount(board.corners, kingLegalMoves);
    //        }
    //
    //        if (certainEscape || escapeMoveCount == Constants.GameConstants.TUICHI_THRESHHOLD) {
    //            if (board.king != kingEscapePosition || lastKingEscapeWarning != PlayerWarningEnum.TUICHI) {
    //                lastKingEscapeWarning = PlayerWarningEnum.TUICHI;
    //                kingEscapePosition = board.king;
    //                return lastKingEscapeWarning;
    //            }
    //        } else if (escapeMoveCount == Constants.GameConstants.RAICHI_THRESHHOLD) {
    //            if (board.king != kingEscapePosition || lastKingEscapeWarning != PlayerWarningEnum.RAICHI) {
    //                lastKingEscapeWarning = PlayerWarningEnum.RAICHI;
    //                kingEscapePosition = board.king;
    //                return lastKingEscapeWarning;
    //            }
    //        } else {
    //            lastKingEscapeWarning = null;
    //            kingEscapePosition = Constants.BoardConstants.ILLEGAL_CELL;
    //        }
    //        return null;
    //    }
}
