package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class OfficialCaptureRules {

    TaflMatch match;
    BitSet capturedPieces;

    public OfficialCaptureRules(TaflMatch match) {
        this.match = match;
        capturedPieces = new BitSet(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);
    }

    public BitSet getCapturedPieces(Move move) {
        capturedPieces.clear();

        checkCaptureUp(move);
        checkCaptureDown(move);
        checkCaptureRight(move);
        checkCaptureLeft(move);

        return capturedPieces;
    }

    private void checkCaptureLeft(Move move) {
        int first = move.destination - 1;
        if (first % match.board.dimensions == match.board.dimensions - 1) {
            first = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int second = move.destination - 2;
        if (second % match.board.dimensions == match.board.dimensions - 1) {
            second = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int third = first + match.board.dimensions;
        int fourth = first - match.board.dimensions;
        checkCapture(move, first, second, third, fourth);
    }

    private void checkCaptureRight(Move move) {
        int first = move.destination + 1;
        if (first % match.board.dimensions == 0) {
            first = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int second = move.destination + 2;
        if (second % match.board.dimensions == 0) {
            second = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int third = first + match.board.dimensions;
        int fourth = first - match.board.dimensions;
        checkCapture(move, first, second, third, fourth);
    }

    private void checkCaptureDown(Move move) {
        int first = move.destination - match.board.dimensions;
        int second = move.destination - 2 * match.board.dimensions;
        int third = first + 1;
        if (third % match.board.dimensions == 0) {
            third = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int fourth = first - 1;
        if (fourth % match.board.dimensions == match.board.dimensions - 1) {
            fourth = Constants.BoardConstants.ILLEGAL_CELL;
        }
        checkCapture(move, first, second, third, fourth);
    }

    private void checkCaptureUp(Move move) {
        int first = move.destination + match.board.dimensions;
        int second = move.destination + 2 * match.board.dimensions;
        int third = first + 1;
        if (third % match.board.dimensions == 0) {
            third = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int fourth = first - 1;
        if (fourth % match.board.dimensions == match.board.dimensions - 1) {
            fourth = Constants.BoardConstants.ILLEGAL_CELL;
        }
        checkCapture(move, first, second, third, fourth);
    }

    private void checkCapture(Move move, int first, int second, int third, int fourth) {
        int capturingTeam = move.pieceType;
        int oppositeTeam = (capturingTeam + 1) % 2;
        if (match.board.isValid(first) && match.board.bitBoards[oppositeTeam].get(first)) {
            if (first == match.board.king) {
                if (isKingHostile(capturingTeam, second) &&
                        isKingHostile(capturingTeam, third) &&
                        isKingHostile(capturingTeam, fourth)) {
                    capturedPieces.set(first);
                }
            } else {
                if (isHostile(capturingTeam, second)) {
                    capturedPieces.set(first);
                }
            }
        }
    }

    private boolean isHostile(int capturingTeam, int oppositeCell) {
        return match.board.isValid(oppositeCell) &&
                (match.board.bitBoards[capturingTeam].get(oppositeCell) ||
                        (!match.board.canWalk(capturingTeam, oppositeCell) && oppositeCell != match.board.king));
    }

    private boolean isKingHostile(int capturingTeam, int oppositeCell) {
        return match.board.isValid(oppositeCell) &&
                match.board.bitBoards[capturingTeam].get(oppositeCell);
    }
}
