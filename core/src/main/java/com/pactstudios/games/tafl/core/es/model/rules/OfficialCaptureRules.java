package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class OfficialCaptureRules {

    TaflMatch match;
    BitSet capturedPieces;

    public OfficialCaptureRules(TaflMatch match) {
        this.match = match;
        capturedPieces = new BitSet(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);
    }

    public BitSet getCapturedPieces(int destination) {
        capturedPieces.clear();

        checkCaptureUp(destination);
        checkCaptureDown(destination);
        checkCaptureRight(destination);
        checkCaptureLeft(destination);

        return capturedPieces;
    }

    private void checkCaptureLeft(int destination) {
        int first = destination - 1;
        if (first % match.board.dimensions == match.board.dimensions - 1) {
            first = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int second = destination - 2;
        if (second % match.board.dimensions == match.board.dimensions - 1) {
            second = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int third = first + match.board.dimensions;
        int fourth = first - match.board.dimensions;
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCaptureRight(int destination) {
        int first = destination + 1;
        if (first % match.board.dimensions == 0) {
            first = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int second = destination + 2;
        if (second % match.board.dimensions == 0) {
            second = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int third = first + match.board.dimensions;
        int fourth = first - match.board.dimensions;
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCaptureDown(int destination) {
        int first = destination - match.board.dimensions;
        int second = destination - 2 * match.board.dimensions;
        int third = first + 1;
        if (third % match.board.dimensions == 0) {
            third = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int fourth = first - 1;
        if (fourth % match.board.dimensions == match.board.dimensions - 1) {
            fourth = Constants.BoardConstants.ILLEGAL_CELL;
        }
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCaptureUp(int destination) {
        int first = destination + match.board.dimensions;
        int second = destination + 2 * match.board.dimensions;
        int third = first + 1;
        if (third % match.board.dimensions == 0) {
            third = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int fourth = first - 1;
        if (fourth % match.board.dimensions == match.board.dimensions - 1) {
            fourth = Constants.BoardConstants.ILLEGAL_CELL;
        }
        checkCapture(destination, first, second, third, fourth);
    }

    private void checkCapture(int destination, int first, int second, int third, int fourth) {
        int capturingTeam = match.board.getTeam(destination).bitBoardId();
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
                        (!match.board.canWalk(oppositeCell) && oppositeCell != match.board.king));
    }

    private boolean isKingHostile(int capturingTeam, int oppositeCell) {
        return match.board.isValid(oppositeCell) &&
                match.board.bitBoards[capturingTeam].get(oppositeCell);
    }
}
