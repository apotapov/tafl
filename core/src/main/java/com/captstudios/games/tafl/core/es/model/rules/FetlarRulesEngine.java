package com.captstudios.games.tafl.core.es.model.rules;

import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntIntMap.Values;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.DrawReasonEnum;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.model.TaflBoard;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class FetlarRulesEngine implements RulesEngine {

    protected static final Array<Move> NO_MOVES = new Array<Move>();

    protected Array<Move> blackLegalMoves;
    protected Array<Move> whiteLegalMoves;
    protected Array<Move> kingLegalMoves;

    protected BitBoard legalMoves;
    protected BitBoard allPieces;

    protected BitBoard tempBitBoard;
    protected BitBoard allWhiteBitBoard;

    protected IntArray boardConfigHistory;
    protected IntIntMap configurationCounter;

    protected TaflBoard board;

    protected Random random;
    protected Move[] shuffleArray;

    public FetlarRulesEngine() {
        blackLegalMoves = new Array<Move>();
        whiteLegalMoves = new Array<Move>();
        kingLegalMoves = new Array<Move>();

        boardConfigHistory = new IntArray();
        configurationCounter = new IntIntMap();

        random = new Random();
        shuffleArray = new Move[Constants.GameConstants.MAX_NUMBER_OF_MOVES];
    }

    @Override
    public boolean isGameOver(int team) {
        return checkWinner() != Constants.BoardConstants.NO_TEAM ||
                checkDraw(team) != null;
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        board = match.board;

        legalMoves = new BitBoard(match.board.boardSize);
        allPieces = new BitBoard(match.board.boardSize);

        tempBitBoard = new BitBoard(match.board.boardSize);
        allWhiteBitBoard = new BitBoard(match.board.boardSize);

        calculateLegalMoves(match.turn);
        boardConfigHistory.add(match.board.hashCode());
    }

    @Override
    public void removePieces(TaflMatch match, int team, BitBoard capturedPieces) {
        boardConfigHistory.items[boardConfigHistory.size - 1] = match.board.hashCode();
    }

    @Override
    public void applyMove(TaflMatch match, Move move) {
        boardConfigHistory.add(match.board.hashCode());
    }

    @Override
    public void undoMove(TaflMatch match, Move move) {
        if (boardConfigHistory.size > 0) {
            boardConfigHistory.pop();
        }
    }

    @Override
    public void changeTurn(TaflMatch match) {
        calculateLegalMoves(match.turn);
    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
    }

    public void calculateLegalMoves(int team) {
        Array<Move> allLegalMoves =
                (team == Constants.BoardConstants.WHITE_TEAM) ? whiteLegalMoves : blackLegalMoves;

        board.movePool.freeAll(allLegalMoves);
        allLegalMoves.clear();
        allPieces.set(board.whiteBitBoard()).or(board.blackBitBoard()).or(board.kingBitBoard());

        calculateMoves(team, allLegalMoves);

        shuffle(allLegalMoves);

        if (team == Constants.BoardConstants.WHITE_TEAM) {
            kingLegalMoves.clear();
            calculateMoves(Constants.BoardConstants.KING, kingLegalMoves);

            // we want king moves in the beginning
            kingLegalMoves.addAll(allLegalMoves);
            whiteLegalMoves.clear();
            whiteLegalMoves.addAll(kingLegalMoves);
        }

    }

    protected void shuffle(Array<Move> moves) {
        int size = moves.size;
        for (int i = 0; i < size; i++) {
            shuffleArray[i] = moves.get(i);
        }

        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(size);
            Move current = shuffleArray[i];
            shuffleArray[i] = shuffleArray[randomIndex];
            shuffleArray[randomIndex] = current;
        }
        moves.clear();
        moves.addAll(shuffleArray, 0, size);
    }

    protected void calculateMoves(int pieceType, Array<Move> allLegalMoves) {
        BitBoard bitBoard = board.bitBoards[pieceType];
        for (int source = bitBoard.nextSetBit(0); source >= 0; source = bitBoard.nextSetBit(source+1)) {
            BitBoard moves = calculateMoves(source);
            for (int dest = moves.nextSetBit(0); dest >= 0; dest = moves.nextSetBit(dest+1)) {
                Move move = board.movePool.obtain();
                move.pieceType = pieceType;
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
        }
        return reason;
    }

    @Override
    public int checkWinner() {
        int winner = Constants.BoardConstants.NO_TEAM;
        if (board.getKingCaptured()) {
            winner = Constants.BoardConstants.BLACK_TEAM;
        } else if (board.corners.intersects(board.kingBitBoard())) {
            winner = Constants.BoardConstants.WHITE_TEAM;
        }
        return winner;
    }

    @Override
    public BitBoard getCapturedPieces(Move move) {
        tempBitBoard.clear();

        int capturer = move.destination;
        int capturingTeam = Constants.BoardConstants.WHITE_TEAM;
        if (move.pieceType == Constants.BoardConstants.BLACK_TEAM) {
            capturingTeam = Constants.BoardConstants.BLACK_TEAM;
        }

        allWhiteBitBoard.clear();
        allWhiteBitBoard.set(board.whiteBitBoard()).or(board.kingBitBoard());

        BitBoard oppositeBoard;
        BitBoard capturingBoard;

        if (capturingTeam == Constants.BoardConstants.WHITE_TEAM) {
            capturingBoard = allWhiteBitBoard;
            oppositeBoard = board.blackBitBoard();
        } else {
            capturingBoard = board.blackBitBoard();
            oppositeBoard = allWhiteBitBoard;
        }

        int king = board.getKing();

        // CAPTURE ABOVE
        int beingCaptured = capturer + board.dimensions;
        int teammate = capturer + 2 * board.dimensions;
        if (board.isValid(beingCaptured) && oppositeBoard.get(beingCaptured)) {
            if (board.kingBitBoard().get(beingCaptured)) {
                int teammate2 = beingCaptured - 1;
                int teammate3 = beingCaptured + 1;
                if (isKingHostileVertical(capturingBoard, teammate) &&
                        isKingHostileHorizontal(capturingBoard, king, teammate2) &&
                        isKingHostileHorizontal(capturingBoard, king, teammate3)) {
                    tempBitBoard.set(king);
                }
            } else {
                if (isHostile(capturingTeam, capturingBoard, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        // CAPTURE BELOW
        beingCaptured = capturer - board.dimensions;
        teammate = capturer - 2 * board.dimensions;
        if (board.isValid(beingCaptured) && oppositeBoard.get(beingCaptured)) {
            if (board.kingBitBoard().get(beingCaptured)) {
                int teammate2 = beingCaptured - 1;
                int teammate3 = beingCaptured + 1;
                if (isKingHostileVertical(capturingBoard, teammate) &&
                        isKingHostileHorizontal(capturingBoard, king, teammate2) &&
                        isKingHostileHorizontal(capturingBoard, king, teammate3)) {
                    tempBitBoard.set(king);
                }
            } else {
                if (isHostile(capturingTeam, capturingBoard, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        // CAPTURE LEFT
        beingCaptured = capturer - 1;
        teammate = capturer - 2;
        if (board.isValid(beingCaptured) &&
                board.inRow(capturer, beingCaptured) &&
                oppositeBoard.get(beingCaptured)) {
            if (board.kingBitBoard().get(beingCaptured)) {
                int teammate2 = beingCaptured + board.dimensions;
                int teammate3 = beingCaptured - board.dimensions;
                if (isKingHostileHorizontal(capturingBoard, king, teammate) &&
                        isKingHostileVertical(capturingBoard, teammate2) &&
                        isKingHostileVertical(capturingBoard, teammate3)) {
                    tempBitBoard.set(king);
                }
            } else {
                if (board.isValid(teammate) && board.inRow(capturer, teammate) && isHostile(capturingTeam, capturingBoard, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        // CAPTURE RIGHT
        beingCaptured = move.destination + 1;
        teammate = move.destination + 2;
        if (board.isValid(beingCaptured) &&
                board.inRow(move.destination, beingCaptured) &&
                oppositeBoard.get(beingCaptured)) {
            if (board.kingBitBoard().get(beingCaptured)) {
                int teammate2 = beingCaptured + board.dimensions;
                int teammate3 = beingCaptured - board.dimensions;
                if (isKingHostileHorizontal(capturingBoard, king, teammate) &&
                        isKingHostileVertical(capturingBoard, teammate2) &&
                        isKingHostileVertical(capturingBoard, teammate3)) {
                    tempBitBoard.set(king);
                }
            } else {
                if (board.isValid(teammate) && board.inRow(capturer, teammate) && isHostile(capturingTeam, capturingBoard, teammate)) {
                    tempBitBoard.set(beingCaptured);
                }
            }
        }

        return tempBitBoard;
    }

    protected boolean isKingHostileVertical(BitBoard capturingBoard, int oppositeCell) {
        return board.isValid(oppositeCell) && capturingBoard.get(oppositeCell);
    }

    protected boolean isKingHostileHorizontal(BitBoard capturingBoard, int cell, int oppositeCell) {
        return board.inRow(cell, oppositeCell) && capturingBoard.get(oppositeCell);
    }

    @Override
    public int getFirstTurn() {
        return Constants.BoardConstants.BLACK_TEAM;
    }

    protected boolean isHostile(int capturingTeam, BitBoard capturingBoard, int oppositeCell) {
        return board.isValid(oppositeCell) &&
                (capturingBoard.get(oppositeCell) ||
                        (!board.canWalk(capturingTeam, oppositeCell) &&
                                board.getKing() != oppositeCell));
    }

    @Override
    public boolean isMoveLegal(int team, int source, int destination) {
        return getLegalMoves(team, source).get(destination);
    }

    @Override
    public Array<Move> allLegalMoves(int team) {
        if (isGameOver(team)) {
            return NO_MOVES;
        }
        return retrieveLegalMoves(team);
    }

    protected Array<Move> retrieveLegalMoves(int team) {
        Array<Move> allLegalMoves =
                (team == Constants.BoardConstants.WHITE_TEAM) ? whiteLegalMoves : blackLegalMoves;

        if (allLegalMoves.size == 0) {
            calculateLegalMoves(team);
        }
        return allLegalMoves;
    }

    @Override
    public BitBoard getLegalMoves(int team, int source) {
        Array<Move> allLegalMoves =
                (team == Constants.BoardConstants.WHITE_TEAM) ? whiteLegalMoves : blackLegalMoves;
        legalMoves.clear();
        for (Move move : allLegalMoves) {
            if (move.source == source) {
                legalMoves.set(move.destination);
            }
        }
        return legalMoves;
    }

    @Override
    public boolean isVulnerable(int team, int cellId) {
        int oppositeTeam = (team + 1) % 2;

        BitBoard oppositeBoard;

        if (team == Constants.BoardConstants.WHITE_TEAM) {
            oppositeBoard = board.blackBitBoard();
        } else {
            allWhiteBitBoard.clear();
            allWhiteBitBoard.set(board.whiteBitBoard()).or(board.kingBitBoard());
            oppositeBoard = allWhiteBitBoard;
        }

        int cellAbove = cellId + board.dimensions;
        int cellBelow = cellId - board.dimensions;
        int cellLeft = cellId - 1;
        int cellRight = cellId + 1;

        // ABOVE
        if (board.isValid(cellBelow) && isHostile(oppositeTeam, oppositeBoard, cellAbove)) {
            tempBitBoard.set(board.getRow(cellBelow)).and(oppositeBoard);
            for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
                if (board.rules.isMoveLegal(oppositeTeam, i, cellId)) {
                    return true;
                }
            }
        }

        // BELOW
        if (board.isValid(cellAbove) && isHostile(oppositeTeam, oppositeBoard, cellBelow)) {
            tempBitBoard.set(board.getRow(cellAbove)).and(oppositeBoard);
            for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
                if (board.rules.isMoveLegal(oppositeTeam, i, cellId)) {
                    return true;
                }
            }
        }

        // LEFT
        if (board.isValid(cellLeft) &&
                board.isValid(cellRight) &&
                board.inRow(cellId, cellLeft) &&
                board.inRow(cellId, cellRight) &&
                isHostile(oppositeTeam, oppositeBoard, cellLeft)) {
            tempBitBoard.set(board.getColumn(cellRight)).and(oppositeBoard);
            for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
                if (board.rules.isMoveLegal(oppositeTeam, i, cellId)) {
                    return true;
                }
            }
        }

        // RIGHT
        if (board.isValid(cellLeft) &&
                board.isValid(cellRight) &&
                board.inRow(cellId, cellLeft) &&
                board.inRow(cellId, cellRight) &&
                isHostile(oppositeTeam, oppositeBoard, cellRight)) {
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

        BitBoard teamBoard;

        if (team == Constants.BoardConstants.WHITE_TEAM) {
            allWhiteBitBoard.clear();
            allWhiteBitBoard.set(board.whiteBitBoard()).or(board.kingBitBoard());
            teamBoard = allWhiteBitBoard;
        } else {
            teamBoard = board.blackBitBoard();
        }

        tempBitBoard.set(board.getRow(cellId)).or(board.getColumn(cellId)).and(teamBoard);
        for (int i = tempBitBoard.nextSetBit(0); i >= 0; i = tempBitBoard.nextSetBit(i+1)) {
            if (board.rules.isMoveLegal(team, i, cellId)) {
                return true;
            }
        }
        return false;
    }

    protected BitBoard calculateMoves(int source) {
        legalMoves.clear();

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

    protected DrawReasonEnum checkDrawMoves(int team) {
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
    protected DrawReasonEnum checkDrawThreePeat() {
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

    @Override
    public void freeMoves(Array<Move> moves) {
        board.movePool.freeAll(moves);
    }

    @Override
    public Array<Move> generateLegalMoves(int team) {
        calculateLegalMoves(team);
        return allLegalMoves(team);
    }
}
