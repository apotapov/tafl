package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class BlackCompleteEvaluator implements BoardEvaluator<TaflBoard> {

    private static final int[] CORNER_VALUE_CELLS = {
        2, 22, 12,
        8, 32, 20,
        88, 112, 100,
        98, 118, 108,
    };

    private static final int WIN = 100000;
    private static final int LOSS = -WIN;

    private static final int WHITE_PIECE_VALUE = 5;
    private static final int BLACK_PIECE_VALUE = 10;
    private static final int WHITE_PIECE_VULNERABILITY_VALUE = 3;
    private static final int BLACK_PIECE_VULNERABILITY_VALUE = 5;
    private static final int WHITE_OPPOSITE_PIECE_VULNERABILITY_VALUE = 4;
    private static final int BLACK_OPPOSITE_PIECE_VULNERABILITY_VALUE = 2;


    private static final int KING_MOBILITY = 2;
    private static final int KING_EMPTY_RANK_2 = 90;
    private static final int KING_EMPTY_RANK_3 = 45;
    private static final int KING_EMPTY_FILE_2 = 90;
    private static final int KING_EMPTY_FILE_3 = 70;

    private static final int CORNER_PROTECTION_BONUS = 10;
    private static final int WHITE_CORNER_PROTECTION_BONUS = 30;
    private static final int DANGER_SQUARE_BONUS = -2;
    private static final int FULL_BARRICADE_BONUS = 25;

    public int[][][] Field = new int[11][11][1];

    public BitBoard tempBitBoard;
    public BitBoard allPiecesBoard;

    public BitBoard cornerProtection;

    public BitBoard north;
    public BitBoard south;
    public BitBoard east;
    public BitBoard west;

    public IntArray barricadeStack;
    public IntSet barricadeSet;

    public BlackCompleteEvaluator(int boardSize, int dimensions) {
        tempBitBoard = new BitBoard(boardSize);
        allPiecesBoard = new BitBoard(boardSize);

        cornerProtection = new BitBoard(boardSize);
        for (int element : CORNER_VALUE_CELLS) {
            cornerProtection.set(element);
        }

        int half = dimensions / 2;
        north = new BitBoard(boardSize);
        south = new BitBoard(boardSize);
        east = new BitBoard(boardSize);
        west = new BitBoard(boardSize);
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                if (i < half) {
                    south.set(i * dimensions + j);
                } else if (i > half) {
                    north.set(i * dimensions + j);
                }
                if (j < half) {
                    west.set(i * dimensions + j);
                } else if (j > half) {
                    east.set(i * dimensions + j);
                }
            }
        }

        barricadeStack = new IntArray();
        barricadeSet = new IntSet();
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

            value += detectBarricade(board, turn);
            value += kingMobility(board, turn);
            value += squareWeight(board, turn);
            value += materialBallance(board, turn);
            value += checkRankAndFiles(board, turn);
            value += checkKingRanksAndFiles(board, turn);

        }

        return value;
    }

    private int detectBarricade(TaflBoard board, int turn) {
        int value = 0;

        BitBoard blackBoard = board.blackBitBoard();
        BitBoard whiteBoard = board.whiteBitBoard();

        if (checkBarricade(board, 0, blackBoard, whiteBoard, 1, board.dimensions) > 0) {
            if (south.get(board.king)) {
                value += FULL_BARRICADE_BONUS;
            }
            if (west.get(board.king)) {
                value += FULL_BARRICADE_BONUS;
            }
        }

        if (checkBarricade(board, 10, blackBoard, whiteBoard, -1, board.dimensions) > 0) {
            if (south.get(board.king)) {
                value += FULL_BARRICADE_BONUS;
            }
            if (east.get(board.king)) {
                value += FULL_BARRICADE_BONUS;
            }
        }

        if (checkBarricade(board, 110, blackBoard, whiteBoard, 1, -board.dimensions) > 0) {
            if (north.get(board.king)) {
                value += FULL_BARRICADE_BONUS;
            }
            if (west.get(board.king)) {
                value += FULL_BARRICADE_BONUS;
            }
        }

        if (checkBarricade(board, 120, blackBoard, whiteBoard, -1, -board.dimensions) > 0) {
            if (north.get(board.king)) {
                value += FULL_BARRICADE_BONUS;
            }
            if (east.get(board.king)) {
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

        value += board.rules.getLegalMoves(Constants.BoardConstants.WHITE_TEAM, board.king).cardinality() * KING_MOBILITY;

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
        if (north.get(board.king)) {
            yHemisphere = north;
        } else if (south.get(board.king)) {
            yHemisphere = south;
        }

        BitBoard xHemisphere = null;
        if (west.get(board.king)) {
            xHemisphere = west;
        } else if (east.get(board.king)) {
            xHemisphere = east;
        }

        for (int i = blackBoard.nextSetBit(0); i >= 0; i = blackBoard.nextSetBit(i + 1)) {
            if (cornerProtection.get(i)) {
                if (board.king == board.center) {
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

        board.whiteBitBoard().clear(board.king);

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
