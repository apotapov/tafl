package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class OfficialRulesEngine extends RulesEngine {

    Array<Move> allLegalMoves;
    IntArray legalMoves;
    IntArray capturedPieces;

    public OfficialRulesEngine(TaflMatch match) {
        super(match);
        allLegalMoves = new Array<Move>();
        legalMoves = new IntArray();
        capturedPieces = new IntArray();
    }

    @Override
    public Team checkWinner(int destination, IntArray capturedPieces) {
        Team winner = null;
        if (checkCaptureKing(capturedPieces)) {
            winner = Team.BLACK;
        } else if (checkKingEscaped(destination)) {
            winner = Team.WHITE;
        }
        return winner;
    }

    @Override
    public Team checkWinner() {
        Team winner = null;
        if (checkCaptureKing()) {
            winner = Team.BLACK;
        } else if (checkKingEscaped()) {
            winner = Team.WHITE;
        }
        return winner;
    }

    private boolean checkCaptureKing() {
        return match.king != Constants.BoardConstants.KING_DEAD;
    }

    private boolean checkKingEscaped() {
        return match.corners[0] == match.king ||
                match.corners[1] == match.king ||
                match.corners[2] == match.king ||
                match.corners[3] == match.king;
    }



    private boolean checkCaptureKing(IntArray capturedPieces) {
        for (int i = 0; i < capturedPieces.size; i++) {
            if (capturedPieces.items[i] == match.king) {
                return true;
            }
        }
        return false;
    }

    private boolean checkKingEscaped(int cellId) {
        return match.isCornerCell(cellId) && cellId == match.king;
    }

    @Override
    public IntArray getCapturedPieces(int destination) {
        capturedPieces.clear();

        checkCaptureUp(destination);
        checkCaptureDown(destination);
        checkCaptureRight(destination);
        checkCaptureLeft(destination);

        return capturedPieces;
    }

    private void checkCaptureLeft(int destination) {
        int first = destination - 1;
        int second = destination - 2;
        int third = first + match.board.dimensions;
        int fourth = first - match.board.dimensions;
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCaptureRight(int destination) {
        int first = destination + 1;
        int second = destination + 2;
        int third = first + match.board.dimensions;
        int fourth = first - match.board.dimensions;
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCaptureDown(int destination) {
        int first = destination - match.board.dimensions;
        int second = destination - 2 * match.board.dimensions;
        int third = first + 1;
        int fourth = first - 1;
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCaptureUp(int destination) {
        int first = destination + match.board.dimensions;
        int second = destination + 2 * match.board.dimensions;
        int third = first + 1;
        int fourth = first - 1;
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCapture(int destination, int first, int second, int third, int fourth) {
        try {
            Team capturingTeam = match.getTeam(destination);
            if (match.board.isValid(first) && match.board.bitBoards[capturingTeam.getOpositeTeam().bitBoardId()].get(first)) {
                if (first == match.king) {
                    if (isKingHostile(capturingTeam, second) &&
                            isKingHostile(capturingTeam, third) &&
                            isKingHostile(capturingTeam, fourth)) {
                        capturedPieces.add(first);
                    }
                } else {
                    if (isHostile(capturingTeam, second)) {
                        capturedPieces.add(first);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isHostile(Team capturingTeam, int oppositeCell) {
        return match.board.isValid(oppositeCell) &&
                (match.board.bitBoards[capturingTeam.bitBoardId()].get(oppositeCell) ||
                        (!match.canWalk(oppositeCell) && oppositeCell != match.king));
    }

    private boolean isKingHostile(Team capturingTeam, int oppositeCell) {
        return match.board.isValid(oppositeCell) &&
                match.board.bitBoards[capturingTeam.bitBoardId()].get(oppositeCell);
    }

    @Override
    public boolean isMoveLegal(int source, int destination) {
        IntArray legalMoves = legalMoves(source);
        for (int i = 0; i < legalMoves.size; i++) {
            if (legalMoves.items[i] == destination) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Array<Move> legalMoves() {
        return allLegalMoves;
    }

    @Override
    public void calculateLegalMoves() {
        Move.movePool.freeAll(allLegalMoves);
        allLegalMoves.clear();
        BitSet bitBoard = match.board.bitBoards[match.turn.bitBoardId()];
        for (int source = bitBoard.nextSetBit(0); source >= 0; source = bitBoard.nextSetBit(source+1)) {
            IntArray moves = calculateLegalMoves(source);
            for (int i = 0; i < moves.size; i++) {
                Move move = Move.movePool.obtain();
                move.pieceType = match.turn.bitBoardId();
                move.source = source;
                move.destination = moves.items[i];
                allLegalMoves.add(move);
            }
        }
    }

    @Override
    public IntArray legalMoves(int source) {
        legalMoves.clear();
        for (Move move : allLegalMoves) {
            if (move.source == source) {
                legalMoves.add(move.destination);
            }
        }
        return legalMoves;
    }

    private IntArray calculateLegalMoves(int source) {
        legalMoves.clear();

        legalUp(source);
        legalDown(source);
        legalRight(source);
        legalLeft(source);

        return legalMoves;
    }

    private void legalUp(int source) {
        for (int i = source + match.board.dimensions; i < match.board.numberCells; i += match.board.dimensions) {
            if (!match.board.bitBoards[Team.WHITE.bitBoardId()].get(i) &&
                    !match.board.bitBoards[Team.BLACK.bitBoardId()].get(i) &&
                    (match.canWalk(i) || i == match.king)) {
                legalMoves.add(i);
            } else {
                break;
            }
        }
    }

    private void legalDown(int source) {
        for (int i = source - match.board.dimensions; i >= 0; i -= match.board.dimensions) {
            if (!match.board.bitBoards[Team.WHITE.bitBoardId()].get(i) &&
                    !match.board.bitBoards[Team.BLACK.bitBoardId()].get(i) &&
                    (match.canWalk(i) || i == match.king)) {
                legalMoves.add(i);
            } else {
                break;
            }
        }
    }

    private void legalRight(int source) {
        int nextRow = ((source + match.board.dimensions) / match.board.dimensions) * match.board.dimensions;
        for (int i = source + 1; i < nextRow; i++) {
            if (!match.board.bitBoards[Team.WHITE.bitBoardId()].get(i) &&
                    !match.board.bitBoards[Team.BLACK.bitBoardId()].get(i) &&
                    (match.canWalk(i) || i == match.king)) {
                legalMoves.add(i);
            } else {
                break;
            }
        }
    }

    private void legalLeft(int source) {
        int previousRow = (source / match.board.dimensions) * match.board.dimensions - 1;
        for (int i = source - 1; i > previousRow; i--) {
            if (!match.board.bitBoards[Team.WHITE.bitBoardId()].get(i) &&
                    !match.board.bitBoards[Team.BLACK.bitBoardId()].get(i) &&
                    (match.canWalk(i) || i == match.king)) {
                legalMoves.add(i);
            } else {
                break;
            }
        }
    }

    @Override
    public Team getFirstTurn() {
        return Team.BLACK;
    }

    @Override
    public Team getSecondTurn() {
        return Team.WHITE;
    }
}
