package com.pactstudios.games.tafl.core.es.model.rules;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntIntMap.Values;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class OfficialRulesEngine extends RulesEngine {

    private static final Array<TaflMove> NO_MOVES = new Array<TaflMove>();

    Array<TaflMove> blackLegalMoves;
    Array<TaflMove> whiteLegalMoves;

    BitBoard legalMoves;
    BitBoard allPieces;

    PlayerWarningEnum lastKingWarning;
    PlayerWarningEnum lastKingEscapeWarning;
    int kingEscapePosition;

    BitBoard tempBitBoard;

    IntArray boardConfigHistory;
    IntIntMap configurationCounter;

    TaflBoard board;

    @Override
    public boolean isGameOver(int team) {
        return checkWinner() != Constants.BoardConstants.NO_TEAM ||
                checkDraw(team) != null;
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        board = match.board;

        blackLegalMoves = new Array<TaflMove>();
        whiteLegalMoves = new Array<TaflMove>();

        legalMoves = new BitBoard(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);
        allPieces = new BitBoard(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);

        tempBitBoard = new BitBoard(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);

        boardConfigHistory = new IntArray();
        configurationCounter = new IntIntMap();

        calculateLegalMoves(match.turn);
        boardConfigHistory.add(match.board.hashCode());
    }

    @Override
    public void addPiece(TaflMatch match, int team, int pieces) {
        boardConfigHistory.items[boardConfigHistory.size - 1] = match.board.hashCode();
    }

    @Override
    public void removePieces(TaflMatch match, int team, BitBoard capturedPieces) {
        boardConfigHistory.items[boardConfigHistory.size - 1] = match.board.hashCode();
    }

    @Override
    public void applyMove(TaflMatch match, TaflMove move) {
        boardConfigHistory.add(match.board.hashCode());
    }

    @Override
    public void undoMove(TaflMatch match, TaflMove move) {
        boardConfigHistory.pop();
    }

    @Override
    public void changeTurn(TaflMatch match) {
        calculateLegalMoves(match.turn);
    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
    }

    public void calculateLegalMoves(int team) {
        Array<TaflMove> allLegalMoves =
                (team == Constants.BoardConstants.WHITE_TEAM) ? whiteLegalMoves : blackLegalMoves;

        TaflMove.movePool.freeAll(allLegalMoves);
        allLegalMoves.clear();
        allPieces.set(board.whiteBitBoard()).or(board.blackBitBoard());

        BitBoard bitBoard = board.bitBoards[team];
        for (int source = bitBoard.nextSetBit(0); source >= 0; source = bitBoard.nextSetBit(source+1)) {
            BitBoard moves = calculateMoves(source);
            for (int dest = moves.nextSetBit(0); dest >= 0; dest = moves.nextSetBit(dest+1)) {
                TaflMove move = TaflMove.movePool.obtain();
                move.pieceType = team;
                move.source = source;
                move.destination = dest;
                allLegalMoves.add(move);
            }
        }
    }


    @Override
    public DrawReasonEnum checkDraw(int team) {
        DrawReasonEnum reason = checkDrawMoves(team);
        if (reason == null) {
            reason = checkDrawThreePeat();
            if (reason == null) {
                reason = checkDrawKingTrapped();

            }
        }
        return reason;
    }

    @Override
    public PlayerWarningEnum checkPlayerWarning(int team) {
        if (team == Constants.BoardConstants.WHITE_TEAM) {
            return checkKingCaptureWarning();
        } else {
            return checkKingEscapeWarning();
        }
    }

    @Override
    public int checkWinner() {
        int winner = Constants.BoardConstants.NO_TEAM;
        if (board.king == Constants.BoardConstants.ILLEGAL_CELL) {
            winner = Constants.BoardConstants.BLACK_TEAM;
        } else if (board.corners.get(board.king)) {
            winner = Constants.BoardConstants.WHITE_TEAM;
        }
        return winner;
    }

    @Override
    public BitBoard getCapturedPieces(TaflMove move) {
        tempBitBoard.clear();

        int capturer = move.destination;
        int capturingTeam = move.pieceType;
        int oppositeTeam = (capturingTeam + 1) % 2;

        // CAPTURE ABOVE
        int beingCaptured = capturer + board.dimensions;
        int teammate = capturer + 2 * board.dimensions;
        if (board.isValid(beingCaptured) && board.bitBoards[oppositeTeam].get(beingCaptured)) {
            if (beingCaptured == board.king) {
                int teammate2 = beingCaptured - 1;
                int teammate3 = beingCaptured + 1;
                if (isKingHostileVertical(capturingTeam, teammate) &&
                        isKingHostileHorizontal(capturingTeam, board.king, teammate2) &&
                        isKingHostileHorizontal(capturingTeam, board.king, teammate3)) {
                    tempBitBoard.set(board.king);
                }
            } else {
                if (isHostile(capturingTeam, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        // CAPTURE BELOW
        beingCaptured = capturer - board.dimensions;
        teammate = capturer - 2 * board.dimensions;
        if (board.isValid(beingCaptured) && board.bitBoards[oppositeTeam].get(beingCaptured)) {
            if (beingCaptured == board.king) {
                int teammate2 = beingCaptured - 1;
                int teammate3 = beingCaptured + 1;
                if (isKingHostileVertical(capturingTeam, teammate) &&
                        isKingHostileHorizontal(capturingTeam, board.king, teammate2) &&
                        isKingHostileHorizontal(capturingTeam, board.king, teammate3)) {
                    tempBitBoard.set(board.king);
                }
            } else {
                if (isHostile(capturingTeam, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        // CAPTURE LEFT
        beingCaptured = capturer - 1;
        teammate = capturer - 2;
        if (board.isValid(beingCaptured) &&
                board.inRow(capturer, beingCaptured) &&
                board.bitBoards[oppositeTeam].get(beingCaptured)) {
            if (beingCaptured == board.king) {
                int teammate2 = beingCaptured + board.dimensions;
                int teammate3 = beingCaptured - board.dimensions;
                if (isKingHostileHorizontal(capturingTeam, board.king, teammate) &&
                        isKingHostileVertical(capturingTeam, teammate2) &&
                        isKingHostileVertical(capturingTeam, teammate3)) {
                    tempBitBoard.set(board.king);
                }
            } else {
                if (board.inRow(capturer, teammate) && isHostile(capturingTeam, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        // CAPTURE RIGHT
        beingCaptured = move.destination + 1;
        teammate = move.destination + 2;
        if (board.isValid(beingCaptured) &&
                board.inRow(move.destination, beingCaptured) &&
                board.bitBoards[oppositeTeam].get(beingCaptured)) {
            if (beingCaptured == board.king) {
                int teammate2 = beingCaptured + board.dimensions;
                int teammate3 = beingCaptured - board.dimensions;
                if (isKingHostileHorizontal(capturingTeam, board.king, teammate) &&
                        isKingHostileVertical(capturingTeam, teammate2) &&
                        isKingHostileVertical(capturingTeam, teammate3)) {
                    tempBitBoard.set(board.king);
                }
            } else {
                if (board.inRow(capturer, teammate) && isHostile(capturingTeam, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        return tempBitBoard;
    }

    private boolean isKingHostileVertical(int capturingTeam, int oppositeCell) {
        return board.isValid(oppositeCell) &&
                board.bitBoards[capturingTeam].get(oppositeCell);
    }

    private boolean isKingHostileHorizontal(int capturingTeam, int cell, int oppositeCell) {
        return board.inRow(cell, oppositeCell) &&
                board.bitBoards[capturingTeam].get(oppositeCell);
    }

    @Override
    public int getFirstTurn() {
        return Constants.BoardConstants.BLACK_TEAM;
    }

    @Override
    public boolean isHostile(int capturingTeam, int oppositeCell) {
        return board.isValid(oppositeCell) &&
                (board.bitBoards[capturingTeam].get(oppositeCell) ||
                        (!board.canWalk(capturingTeam, oppositeCell) && oppositeCell != board.king));
    }

    @Override
    public boolean isMoveLegal(int team, int source, int destination) {
        return getLegalMoves(team, source).get(destination);
    }

    @Override
    public Array<TaflMove> allLegalMoves(int team) {
        if (isGameOver(team)) {
            return NO_MOVES;
        }
        return retrieveLegalMoves(team);
    }

    private Array<TaflMove> retrieveLegalMoves(int team) {
        Array<TaflMove> allLegalMoves =
                (team == Constants.BoardConstants.WHITE_TEAM) ? whiteLegalMoves : blackLegalMoves;

        if (allLegalMoves.size == 0) {
            calculateLegalMoves(team);
        }
        return allLegalMoves;
    }

    @Override
    public BitBoard getLegalMoves(int team, int source) {
        Array<TaflMove> allLegalMoves =
                (team == Constants.BoardConstants.WHITE_TEAM) ? whiteLegalMoves : blackLegalMoves;
        legalMoves.clear();
        for (TaflMove move : allLegalMoves) {
            if (move.source == source) {
                legalMoves.set(move.destination);
            }
        }
        return legalMoves;
    }

    @Override
    public boolean isVulnerable(int team, int cellId) {
        int oppositeTeam = (team + 1) % 2;
        BitBoard oppositeBoard = board.bitBoards[oppositeTeam];

        int cellAbove = cellId + board.dimensions;
        int cellBelow = cellId - board.dimensions;
        int cellLeft = cellId - 1;
        int cellRight = cellId +1;

        // ABOVE
        if (board.isValid(cellBelow) && isHostile(oppositeTeam, cellAbove)) {
            tempBitBoard.set(board.getRow(cellBelow)).and(oppositeBoard);
            for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
                if (board.rules.isMoveLegal(oppositeTeam, i, cellId)) {
                    return true;
                }
            }
        }

        // BELOW
        if (board.isValid(cellAbove) && isHostile(oppositeTeam, cellBelow)) {
            tempBitBoard.set(board.getRow(cellAbove)).and(oppositeBoard);
            for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
                if (board.rules.isMoveLegal(oppositeTeam, i, cellId)) {
                    return true;
                }
            }
        }

        // LEFT
        if (board.isValid(cellRight) &&
                isHostile(oppositeTeam, cellLeft) &&
                board.inRow(cellId, cellRight) &&
                board.inRow(cellId, cellLeft)) {
            tempBitBoard.set(board.getColumn(cellRight)).and(oppositeBoard);
            for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
                if (board.rules.isMoveLegal(oppositeTeam, i, cellId)) {
                    return true;
                }
            }
        }

        // RIGHT
        if (board.isValid(cellLeft) &&
                isHostile(oppositeTeam, cellRight) &&
                board.inRow(cellId, cellLeft) &&
                board.inRow(cellId, cellRight)) {
            tempBitBoard.set(board.getColumn(cellLeft)).and(oppositeBoard);
            for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
                if (board.rules.isMoveLegal(oppositeTeam, i, cellId)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean teamCanMoveToLocation(int team, int cellId) {
        tempBitBoard.set(board.getRow(cellId)).or(board.getColumn(cellId)).and(board.bitBoards[team]);
        for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
            if (board.rules.isMoveLegal(team, i, cellId)) {
                return true;
            }
        }
        return false;
    }

    private BitBoard calculateMoves(int source) {
        legalMoves.clear();

        // TODO use row/column bitsets

        // LEGAL UP
        for (int i = source + board.dimensions; i < board.boardSize; i += board.dimensions) {
            if (!allPieces.get(i)) {
                if (board.canWalk(source, i)) {
                    legalMoves.set(i);
                }
            } else {
                break;
            }
        }

        // LEGAL DOWN
        for (int i = source - board.dimensions; i >= 0; i -= board.dimensions) {
            if (!allPieces.get(i)) {
                if (board.canWalk(source, i)) {
                    legalMoves.set(i);
                }
            } else {
                break;
            }
        }

        // LEGAL RIGHT
        int nextRow = ((source + board.dimensions) / board.dimensions) * board.dimensions;
        for (int i = source + 1; i < nextRow; i++) {
            if (!allPieces.get(i)) {
                if (board.canWalk(source, i)) {
                    legalMoves.set(i);
                }
            } else {
                break;
            }
        }

        // LEGAL LEFT
        int previousRow = (source / board.dimensions) * board.dimensions - 1;
        for (int i = source - 1; i > previousRow; i--) {
            if (!allPieces.get(i)) {
                if (board.canWalk(source, i)) {
                    legalMoves.set(i);
                }
            } else {
                break;
            }
        }

        return legalMoves;
    }


    private DrawReasonEnum checkDrawKingTrapped() {
        // TODO Auto-generated method stub
        return null;
    }

    private DrawReasonEnum checkDrawMoves(int team) {
        if (board.undoStack.size >= Constants.GameConstants.DRAW_MOVE_THRESHHOLD) {
            return DrawReasonEnum.DRAW_TOO_MANY_TURNS;
        } else if (retrieveLegalMoves(team).size == 0){
            if (team == Constants.BoardConstants.WHITE_TEAM) {
                return DrawReasonEnum.DRAW_NO_MOVES_WHITE;
            } else {
                return DrawReasonEnum.DRAW_NO_MOVES_BLACK;
            }
        }
        return null;
    }
    private DrawReasonEnum checkDrawThreePeat() {
        int boardsToExamine = Constants.GameConstants.DRAW_BOARD_REPETITION_THRESHHOLD *
                Constants.GameConstants.DRAW_MOVES_TO_CHECK;
        if (boardConfigHistory.size >= boardsToExamine) {
            configurationCounter.clear();
            for (int i = boardConfigHistory.size - 1; i >= boardConfigHistory.size - boardsToExamine; i--) {
                configurationCounter.getAndIncrement(boardConfigHistory.items[i], 0, 1);
            }

            Values values = configurationCounter.values();
            while (values.hasNext) {
                if (values.next() >= Constants.GameConstants.DRAW_BOARD_REPETITION_THRESHHOLD) {
                    return DrawReasonEnum.DRAW_THREE_PEAT;
                }
            }
        }
        return null;
    }
    private PlayerWarningEnum checkKingCaptureWarning() {
        int king = board.king;
        int surroundingEnemy = 0;
        BitBoard blackPositions = board.blackBitBoard();

        int kingUp = king + board.dimensions;
        int kingDown = king - board.dimensions;
        int kingLeft = king - 1;
        if (kingLeft % board.dimensions == board.dimensions - 1) {
            kingLeft = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int kingRight = king + 1;
        if (kingRight % board.dimensions == 0) {
            kingRight = Constants.BoardConstants.ILLEGAL_CELL;
        }

        if (board.isValid(kingUp) && blackPositions.get(kingUp)) {
            surroundingEnemy++;
        }
        if (board.isValid(kingDown) && blackPositions.get(kingDown)) {
            surroundingEnemy++;
        }
        if (board.isValid(kingLeft) && blackPositions.get(kingLeft)) {
            surroundingEnemy++;
        }
        if (board.isValid(kingRight) && blackPositions.get(kingRight)) {
            surroundingEnemy++;
        }

        if (surroundingEnemy >= Constants.GameConstants.KING_CAPTURE_THRESHHOLD) {
            if (lastKingWarning == null) {
                lastKingWarning = PlayerWarningEnum.WATCH_YOUR_KING;
                return lastKingWarning;
            }
        } else {
            lastKingWarning = null;
        }
        return null;
    }

    private PlayerWarningEnum checkKingEscapeWarning() {
        allPieces.set(board.whiteBitBoard()).or(board.blackBitBoard());
        BitBoard kingLegalMoves = calculateMoves(board.king);

        int escapeMoveCount = 0;
        boolean certainEscape = false;
        if (board.nearCorners.get(board.king)) {
            certainEscape = true;
        } else {
            escapeMoveCount = BitBoard.intersectionCount(board.corners, kingLegalMoves);
        }

        if (certainEscape || escapeMoveCount == Constants.GameConstants.TUICHI_THRESHHOLD) {
            if (board.king != kingEscapePosition || lastKingEscapeWarning != PlayerWarningEnum.TUICHI) {
                lastKingEscapeWarning = PlayerWarningEnum.TUICHI;
                kingEscapePosition = board.king;
                return lastKingEscapeWarning;
            }
        } else if (escapeMoveCount == Constants.GameConstants.RAICHI_THRESHHOLD) {
            if (board.king != kingEscapePosition || lastKingEscapeWarning != PlayerWarningEnum.RAICHI) {
                lastKingEscapeWarning = PlayerWarningEnum.RAICHI;
                kingEscapePosition = board.king;
                return lastKingEscapeWarning;
            }
        } else {
            lastKingEscapeWarning = null;
            kingEscapePosition = Constants.BoardConstants.ILLEGAL_CELL;
        }
        return null;
    }

    @Override
    public void freeMoves(Array<TaflMove> moves) {
        TaflMove.movePool.freeAll(moves);
    }

    @Override
    public Array<TaflMove> generateLegalMoves(int pieceType) {
        calculateLegalMoves(pieceType);
        return allLegalMoves(pieceType);
    }
}
