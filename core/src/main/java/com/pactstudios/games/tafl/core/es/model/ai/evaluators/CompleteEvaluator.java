package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class CompleteEvaluator implements BoardEvaluator<TaflBoard> {

    private static final int WIN = 100000;
    private static final int LOSS = -WIN;

    private static final int PIECE_VALUE = 10;
    private static final int PIECE_VULNERABILITY_VALUE = 3;
    private static final int OPPOSITE_VULNERABILITY_VALUE = 1;
    private static final int KING_EDGE_PROXIMITY_BONUS = 40;
    private static final float KING_ON_EDGE_ATTACKER_ADJUSTMENT = -0.5f;

    public int[][][] Field = new int[11][11][1];

    public BitBoard tempBitBoard;
    public BitBoard allPiecesBoard;

    public CompleteEvaluator(int boardSize) {
        tempBitBoard = new BitBoard(boardSize);
        allPiecesBoard = new BitBoard(boardSize);
    }

    @Override
    public int evaluate(TaflBoard board, int turn) {
        int value = 0;

        int winner = board.rules.checkWinner();
        if (winner != Constants.BoardConstants.NO_TEAM) {
            value = winner == turn ? WIN : LOSS;
        } else {
            value += checkKingRanksAndFiles(board, turn);
            value += checkIfKingOnEdge(board, turn);
            value += checkKingRisk(board, turn);

            if (turn == Constants.BoardConstants.BLACK_TEAM) {
                value *= KING_ON_EDGE_ATTACKER_ADJUSTMENT;
            }

            value += materialBallance(board, turn);
            value += checkRankAndFiles(board, turn);
            value += moveRepetition(board, turn);
        }

        return value;
    }

    private int checkKingRanksAndFiles(TaflBoard board, int turn) {
        int value = 0;
        int depth = 0;

        int oppColor = (turn + 1) % 2;

        // ----------------------------------------------------------
        // Section 2.1: Examine pieces BELOW the King
        // -----------------------------------------------------------

        // First, we look at the column where the King is
        int kingColumn = board.king % board.dimensions;
        int kingRow = board.king / board.dimensions;


        // all the pieces that are in the same column as the king
        allPiecesBoard.set(board.whiteBitBoard()).or(board.blackBitBoard());
        tempBitBoard.set(allPiecesBoard).and(board.getColumn(board.king));

        int piecesAbove = 0;
        for (int i = tempBitBoard.nextSetBit(board.king + 1); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
            piecesAbove++;
        }

        int piecesBelow = 0;
        for (int i = tempBitBoard.prevSetBit(board.king - 1); i >= 0; i = tempBitBoard.prevSetBit(i-1)) {
            piecesBelow++;
        }

        if (piecesBelow == 0) {
            // Field[2][0] == 22
            if ((kingColumn == 1) && allPiecesBoard.get(22)) {
                if (turn == Constants.BoardConstants.WHITE_TEAM) {
                    value += 90;
                } else {
                    if (board.whiteBitBoard().get(22)) {
                        value += 5;
                    } else {
                        // the peice at 22 is black
                        value += 90;
                    }
                }
            }

            if ((kingColumn == board.dimensions - 2) && allPiecesBoard.get(88)) {
                if (turn == Constants.BoardConstants.WHITE_TEAM) {
                    value += 90;
                } else {
                    if (board.whiteBitBoard().get(88)) {
                        value += 5;
                    } else {
                        // the peice at 22 is black
                        value += 90;
                    }
                }
            }


            if ((kingColumn != 1 ||
                    !board.rules.teamCanMoveToLocation(Constants.BoardConstants.BLACK_TEAM, 22)) &&
                    (kingColumn != board.dimensions - 2 ||
                    !board.rules.teamCanMoveToLocation(Constants.BoardConstants.BLACK_TEAM, 88))) {

                if (board.rules.isMoveLegal(turn, 0, 88)
                        || board.rules.isMoveLegal(oppColor, 0, 88)) {

                    if (turn == Constants.BoardConstants.WHITE_TEAM) {
                        value += 90;
                    } else {
                        value += 5;
                    }
                }
            }

        }



        //
        //            // if the King is not on the 2nd rank column and there's a black
        //            // (Attacker) piece that can get to the strategic
        //            // square at (2,0) or (board.dimensions-3,0) to stop the king...
        //            if (((i != 1) || (!CanMoveToColor(2, 0, 0, depth)))
        //                    && ((i != board.dimensions - 2) || (!CanMoveToColor(board.dimensions - 3, 0, 0,
        //                            depth))))
        //            {
        //                // ...additionally, if the 1st rank remains clear of pieces, then
        //                // it's significant for
        //                // for the Attacker (value += 90) and not as important to the
        //                // Defender (value += 5).
        //                if (CanMoveFromTo(0, 0, board.dimensions - 2, 0, depth)) {
        //                    value += 90 - turn * 85;
        //                }
        //
        //            }
        //
        //        }
        //
        //        // ----------------------------------------------------------
        //        // Section 2.2: Examine pieces ABOVE the King
        //        // -----------------------------------------------------------
        //        // In this next section, we do the same thing as the previous section,
        //        // except
        //        // we are looking ABOVE the King. We count the pieces of the King, and
        //        // we examine the ranks and pieces near the TOP of the board, instead of
        //        // the bottom.
        //
        //        int nn = 0;
        //
        //        for (j = kingRow + 1; j < board.dimensions; j++)
        //
        //        {
        //            if (Field[i][j][depth] >= 0) {
        //                nn++;
        //            }
        //        }
        //
        //        if (nn >= 1) {
        //            nn = 1;
        //        } else
        //
        //        {
        //            if ((i == 1) && (Field[2][board.dimensions - 1][depth] >= 0)) {
        //                value += 90 + turn * 85 * (Field[2][board.dimensions - 1][depth] - 1);
        //            }
        //
        //            if ((i == board.dimensions - 2) && (Field[board.dimensions - 3][board.dimensions - 1][depth] >= 0)) {
        //                value += 90 + turn * 85
        //                        * (Field[board.dimensions - 3][board.dimensions - 1][depth] - 1);
        //            }
        //
        //            if (((i != 1) || (!CanMoveToColor(2, board.dimensions - 1, 0, depth)))
        //                    && ((i != board.dimensions - 2) || (!CanMoveToColor(board.dimensions - 3, board.dimensions - 1,
        //                            0, depth))))
        //
        //            {
        //                if (CanMoveFromTo(0, board.dimensions - 1, board.dimensions - 2, board.dimensions - 1, depth)) {
        //                    value += 90 - turn * 85;
        //                }
        //
        //            }
        //
        //        }
        //
        //        // ----------------------------------------------------------
        //        // Section 2.1 & 2.2: Adjusting value based on how king is surrounded
        //        // -----------------------------------------------------------
        //        // Now we add up the number of pieces ABOVE or BELOW the King, but it's
        //        // not a real count
        //        // 2: there are pieces above and below the King
        //        // 1: there are pieces either above or below the King
        //        // 0: there are no pieces above or below the King
        //        kk += nn;
        //
        //        // if the King is on either the LEFT or RIGHT edge, ...
        //        if ((i == 0) || (i == board.dimensions - 1)) {
        //            // give it a small bonus of +1
        //            value++;
        //
        //            // if there are no pieces above or below the King, +100 to the
        //            // value.
        //            // The King is on the edge, and it's a big deal, so +100.
        //            if (kk == 0) {
        //                value += 100;
        //            } if (kk == 1) {
        //
        //                // if the King is on the edge,...
        //                // AND there are pieces either above or below the King, ...
        //                // if you're Black (oppColor=1), then +100
        //                // if you're White (oppColor=0), then +1
        //                if (oppColor == Constants.BoardConstants.BLACK_TEAM) {
        //                    value += 100;
        //                } else {
        //                    value++;
        //                }
        //            }
        //        } else {
        //            value -= kk * 2;
        //        }

        return value;
    }

    private boolean CanMoveFromTo(int i, int j, int k, int l, int depth) {
        // TODO Auto-generated method stub
        return false;
    }

    private int checkIfKingOnEdge(TaflBoard board, int turn) {
        int value = 0;

        if (board.rows[0].get(board.king)
                || board.rows[board.dimensions - 1].get(board.king)
                || board.columns[0].get(board.king)
                || board.columns[board.dimensions - 1].get(board.king)) {
            value += KING_EDGE_PROXIMITY_BONUS;
        }

        if (board.rows[1].get(board.king)
                || board.rows[board.dimensions - 2].get(board.king)
                || board.columns[1].get(board.king)
                || board.columns[board.dimensions - 2].get(board.king)) {
            value += KING_EDGE_PROXIMITY_BONUS;
        }
        return value;
    }

    private int checkKingRisk(TaflBoard board, int turn) {
        int value = 0;
        //        int kk = 0;
        //        int i = 0;
        //        int j = 0;
        //        int depth = 0;
        //        int nn = 0;
        //        int curColor = turn;
        //
        //        // ------------------------------------
        //        // Section 4.1: If the King is adjacent to or on the center square...
        //        // -------------------------------------
        //        // I'm still confused by what is going on. Will need to draw this out
        //        // to understand.
        //        //
        //        if (Math.abs(i - Center) + Math.abs(j - Center) <= 1) {
        //            if ((Field[i + 1][j][depth] == 0) || (i + 1 == Center)) {
        //                kk++;
        //            } else {
        //                nn = 0;
        //            }
        //
        //            if ((Field[i - 1][j][depth] == 0) || (i - 1 == Center)) {
        //                kk++;
        //            } else {
        //                nn = 1;
        //            }
        //
        //            if ((Field[i][j + 1][depth] == 0) || (j + 1 == Center)) {
        //                kk++;
        //            } else {
        //                nn = 2;
        //            }
        //
        //            if ((Field[i][j - 1][depth] == 0) || (j - 1 == Center)) {
        //                kk++;
        //            } else {
        //                nn = 3;
        //            }
        //
        //            if (kk == 3) {
        //                if ((nn == 0) && (Field[i + 1][j][depth] == -1)) {
        //                    value -= 100 * (CanMoveToColor(i + 1, j, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //
        //                if ((nn == 1) && (Field[i - 1][j][depth] == -1)) {
        //                    value -= 100 * (CanMoveToColor(i - 1, j, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //
        //                if ((nn == 2) && (Field[i][j + 1][depth] == -1)) {
        //                    value -= 100 * (CanMoveToColor(i, j + 1, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //
        //                if ((nn == 3) && (Field[i][j - 1][depth] == -1)) {
        //                    value -= 100 * (CanMoveToColor(i, j - 1, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //            }
        //        } else {
        //
        //            // ------------------------------------
        //            // Section 4.2: If the King is not touching the center
        //            // -------------------------------------
        //            // Again, confused by what is going on. Will need to draw this out
        //            // to understand.
        //            //
        //            //
        //            // Look at the square LEFT and RIGHT of the King
        //            //
        //
        //            if ((GetField(i + 1, j, depth) == 0) || IsHostile(i + 1, j)) {
        //                kk++;
        //            } else {
        //                nn = 0;
        //            }
        //
        //            if ((GetField(i - 1, j, depth) == 0) || IsHostile(i - 1, j)) {
        //                kk++;
        //            } else {
        //                nn = 1;
        //            }
        //
        //            if (kk == 1) {
        //                if ((nn == 0) && (GetField(i + 1, j, depth) == -1)) {
        //                    value -= 100 * (CanMoveToColor(i + 1, j, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //
        //                if ((nn == 1) && (GetField(i - 1, j, depth) == -1)) {
        //                    value -= 100 * (CanMoveToColor(i - 1, j, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //
        //            } else if (kk == 2) {
        //                if ((IsHostile(i + 1, j)) || (IsHostile(i - 1, j))) {
        //                    value += 200;
        //                }
        //            }
        //
        //            kk = 0;
        //
        //            //
        //            // Look at the square ABOVE and BELOW the King
        //            //
        //            if ((GetField(i, j + 1, depth) == 0) || IsHostile(i, j + 1)) {
        //                kk++;
        //            } else {
        //                nn = 0;
        //            }
        //
        //            if ((GetField(i, j - 1, depth) == 0) || IsHostile(i, j - 1)) {
        //                kk++;
        //            } else {
        //                nn = 1;
        //            }
        //
        //            if (kk == 1) {
        //                if ((nn == 0) && (GetField(i, j + 1, depth) == -1)) {
        //                    value -= 100 * (CanMoveToColor(i, j + 1, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //
        //                if ((nn == 1) && (GetField(i, j - 1, depth) == -1)) {
        //                    value -= 100 * (CanMoveToColor(i, j - 1, 0, depth) ? 1 : 0)
        //                            * (curColor + 0.05);
        //                }
        //
        //            } else if (kk == 2) {
        //                if ((IsHostile(i, j + 1)) || (IsHostile(i, j - 1))) {
        //                    value += 200;
        //                }
        //            }
        //        }

        return value;
    }

    private int materialBallance(TaflBoard board, int turn) {

        int value = 0;
        int oppositTeam = (turn + 1) % 2;

        BitBoard turnBoard = board.bitBoards[turn];
        BitBoard oppositeBoard = board.bitBoards[oppositTeam];

        board.whiteBitBoard().clear(board.king);

        for (int i = turnBoard.nextSetBit(0); i >= 0; i = turnBoard
                .nextSetBit(i + 1)) {
            value += PIECE_VALUE
                    - (board.rules.isVulnerable(turn, i) ? PIECE_VULNERABILITY_VALUE
                            : 0);
        }

        for (int i = oppositeBoard.nextSetBit(0); i >= 0; i = oppositeBoard
                .nextSetBit(i + 1)) {
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

    private int moveRepetition(TaflBoard board, int turn) {
        // TODO Auto-generated method stub
        return 0;
    }

    private boolean CanMoveToColor(int i, int j, int k, int depth) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean IsHostile(int i, int j) {
        // TODO Auto-generated method stub
        return false;
    }

    private int GetField(int i, int j, int depth) {
        // TODO Auto-generated method stub
        return 0;
    }
}
