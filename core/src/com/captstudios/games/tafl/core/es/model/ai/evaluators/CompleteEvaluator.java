package com.captstudios.games.tafl.core.es.model.ai.evaluators;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.model.TaflBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class CompleteEvaluator implements BoardEvaluator<TaflBoard> {

    private static final int WIN = 100000;
    private static final int LOSS = -WIN;
    private static final int NEAR_WIN = WIN / 4;

    private static final int WHITE_PIECE_VALUE = 5;
    private static final int BLACK_PIECE_VALUE = 10;
    private static final int WHITE_PIECE_VULNERABILITY_VALUE = 3;
    private static final int BLACK_PIECE_VULNERABILITY_VALUE = 5;
    private static final int WHITE_OPPOSITE_PIECE_VULNERABILITY_VALUE = 4;
    private static final int BLACK_OPPOSITE_PIECE_VULNERABILITY_VALUE = 2;

    private static final int KING_MOBILITY = 5;
    private static final int KING_EMPTY_RANK_2 = 90;
    private static final int KING_EMPTY_RANK_3 = 45;
    private static final int KING_EMPTY_FILE_2 = 90;
    private static final int KING_EMPTY_FILE_3 = 70;

    private static final int CORNER_PROTECTION_BONUS = 10;
    private static final int WHITE_CORNER_PROTECTION_BONUS = 30;
    private static final int DANGER_SQUARE_BONUS = -2;
    private static final int FULL_BARRICADE_BONUS = 25;

    private static final int REPEAT_MOVE_PENALTY = -5;

    public BitBoard tempBitBoard;
    public BitBoard allPiecesBoard;

    public BitBoard cornerProtection;

    public BitBoard north;
    public BitBoard south;
    public BitBoard east;
    public BitBoard west;

    public IntArray barricadeStack;
    public IntSet barricadeSet;

    public Array<Move> moves;

    public CompleteEvaluator(TaflBoard board) {
        tempBitBoard = new BitBoard(board.boardSize);
        allPiecesBoard = new BitBoard(board.boardSize);

        cornerProtection = new BitBoard(board.boardSize);
        for (int element : board.boardType.barricades) {
            cornerProtection.set(element);
        }

        int half = board.dimensions / 2;
        north = new BitBoard(board.boardSize);
        south = new BitBoard(board.boardSize);
        east = new BitBoard(board.boardSize);
        west = new BitBoard(board.boardSize);
        for (int i = 0; i < board.dimensions; i++) {
            for (int j = 0; j < board.dimensions; j++) {
                if (i < half) {
                    south.set(i * board.dimensions + j);
                } else if (i > half) {
                    north.set(i * board.dimensions + j);
                }
                if (j < half) {
                    west.set(i * board.dimensions + j);
                } else if (j > half) {
                    east.set(i * board.dimensions + j);
                }
            }
        }

        barricadeStack = new IntArray();
        barricadeSet = new IntSet();

        moves = new Array<Move>(Constants.GameConstants.DRAW_MOVE_THRESHHOLD);
    }

    @Override
    public int evaluate(TaflBoard board, int turn) {
        board.rules.generateLegalMoves(Constants.BoardConstants.WHITE_TEAM);
        board.rules.generateLegalMoves(Constants.BoardConstants.BLACK_TEAM);

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
            value += detectBarricade(board, turn);
            value += kingMobility(board, turn);
            value += squareWeight(board, turn);
            value += materialBallance(board, turn);
            value += checkRankAndFiles(board, turn);
            value += checkKingRanksAndFiles(board, turn);
            value += checkRepeatMoves(board, turn);
        }

        return value;
    }

    private int detectBarricade(TaflBoard board, int turn) {
        int value = 0;

        BitBoard blackBoard = board.blackBitBoard();
        BitBoard whiteBoard = board.whiteBitBoard();

        if (checkBarricade(board, 0, blackBoard, whiteBoard, 1, board.dimensions) > 0) {
            if (south.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
            if (west.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
        }

        if (checkBarricade(board, 10, blackBoard, whiteBoard, -1, board.dimensions) > 0) {
            if (south.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
            if (east.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
        }

        if (checkBarricade(board, 110, blackBoard, whiteBoard, 1, -board.dimensions) > 0) {
            if (north.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
            if (west.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
        }

        if (checkBarricade(board, 120, blackBoard, whiteBoard, -1, -board.dimensions) > 0) {
            if (north.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
            if (east.intersects(board.kingBitBoard())) {
                value += FULL_BARRICADE_BONUS;
            }
        }

        return value;
    }

    private int checkBarricade(TaflBoard board, int start,
            BitBoard blackBoard, BitBoard whiteBoard,
            int xDirection, int yDirection) {

        int baseCornerCellx = start + xDirection * 2;
        int baseCornerCelly = start + yDirection * 2;
        int baseCornerCellxy = start + xDirection + yDirection;

        int dangerCornerCellx = start + xDirection;
        int dangerCornerCelly = start + yDirection;

        if (blackBoard.get(baseCornerCellx) &&
                blackBoard.get(baseCornerCelly) &&
                blackBoard.get(baseCornerCellxy) &&
                !whiteBoard.get(dangerCornerCellx) &&
                !whiteBoard.get(dangerCornerCelly)) {
            return 3;
        } else if (whiteBoard.get(dangerCornerCellx) ||
                whiteBoard.get(dangerCornerCelly)) {
            return -1;
        } else {

            barricadeSet.clear();
            barricadeStack.clear();

            barricadeSet.add(baseCornerCellx);
            barricadeSet.add(baseCornerCelly);
            barricadeSet.add(baseCornerCellxy);
            barricadeStack.add(baseCornerCellx);
            barricadeStack.add(baseCornerCelly);
            barricadeStack.add(baseCornerCellxy);

            int barricadeCount = 2;
            while (barricadeStack.size > 0) {
                int current = barricadeStack.pop();
                if (whiteBoard.get(current)) {
                    return -1;
                }

                if (blackBoard.get(current)) {
                    barricadeCount++;
                } else {
                    int nextX = current + xDirection;
                    if (board.isValid(nextX) && board.inRow(current, nextX) && !barricadeSet.contains(nextX)) {
                        barricadeStack.add(nextX);
                        barricadeSet.add(nextX);
                    }

                    int nextY = current + yDirection;
                    if (board.isValid(nextY) && !barricadeSet.contains(nextY)) {
                        barricadeStack.add(nextY);
                        barricadeSet.add(nextY);
                    }
                }
            }
            return barricadeCount;
        }
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

        BitBoard whiteBoard = board.whiteBitBoard();
        BitBoard blackBoard = board.blackBitBoard();

        BitBoard yHemisphere = null;
        if (north.intersects(board.kingBitBoard())) {
            yHemisphere = north;
        } else if (south.intersects(board.kingBitBoard())) {
            yHemisphere = south;
        }

        BitBoard xHemisphere = null;
        if (west.intersects(board.kingBitBoard())) {
            xHemisphere = west;
        } else if (east.intersects(board.kingBitBoard())) {
            xHemisphere = east;
        }

        for (int i = blackBoard.nextSetBit(0); i >= 0; i = blackBoard.nextSetBit(i + 1)) {
            if (cornerProtection.get(i)) {
                if (board.kingBitBoard().get(board.center)) {
                    value += CORNER_PROTECTION_BONUS;
                } else {
                    if (yHemisphere != null && yHemisphere.get(i)) {
                        value += CORNER_PROTECTION_BONUS * 2;
                    }
                    if (xHemisphere != null && xHemisphere.get(i)) {
                        value += CORNER_PROTECTION_BONUS * 2;
                    }
                }
            } else if (board.nearCorners.get(i)) {
                value += DANGER_SQUARE_BONUS;
            } else if (board.nearCenter.get(i)) {
                value += DANGER_SQUARE_BONUS;
            }
        }

        for (int i = whiteBoard.nextSetBit(0); i >= 0; i = whiteBoard.nextSetBit(i + 1)) {
            if (cornerProtection.get(i)) {
                value -= WHITE_CORNER_PROTECTION_BONUS;
            } else if (board.nearCorners.get(i)) {
                value -= DANGER_SQUARE_BONUS;
            } else if (board.nearCenter.get(i)) {
                value -= DANGER_SQUARE_BONUS;
            }
        }

        if (turn == Constants.BoardConstants.WHITE_TEAM) {
            value *= -1;
        }

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

        if (board.nearCorners.get(king)) {
            return NEAR_WIN;
        }

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

    private int checkRepeatMoves(TaflBoard board, int turn) {
        int value = 0;

        moves.clear();

        if (board.undoStack.size + board.simulatedStack.size > 4) {

            moves.addAll(board.undoStack);
            moves.addAll(board.simulatedStack);

            Move previousMove = moves.get(moves.size - 2);
            for (int i = moves.size - 4; i >= 0 && i >= moves.size - 13; i-=2) {
                Move currentMove = moves.get(i);
                if (previousMove.source == currentMove.destination && previousMove.destination == currentMove.source) {
                    value += REPEAT_MOVE_PENALTY;
                }
                previousMove = currentMove;
            }
        }

        return value;
    }

}
