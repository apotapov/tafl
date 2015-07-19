package com.captstudios.games.tafl.core.es.model.rules;

import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class SkalkRulesEngine extends FetlarRulesEngine {

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

        // CAPTURE ABOVE
        int beingCaptured = capturer + board.dimensions;
        int teammate = capturer + 2 * board.dimensions;
        if (board.isValid(beingCaptured) && oppositeBoard.get(beingCaptured)) {
            if (isHostile(beingCaptured, capturingTeam, capturingBoard, teammate)) {
                tempBitBoard.set(beingCaptured);
            }
        }

        // CAPTURE BELOW
        beingCaptured = capturer - board.dimensions;
        teammate = capturer - 2 * board.dimensions;
        if (board.isValid(beingCaptured) && oppositeBoard.get(beingCaptured)) {
            if (isHostile(beingCaptured, capturingTeam, capturingBoard, teammate)) {
                tempBitBoard.set(beingCaptured);
            }
        }

        // CAPTURE LEFT
        beingCaptured = capturer - 1;
        teammate = capturer - 2;
        if (board.isValid(beingCaptured) &&
                board.inRow(capturer, beingCaptured) &&
                oppositeBoard.get(beingCaptured)) {
            if (board.isValid(teammate) && board.inRow(capturer, teammate) &&
                    isHostile(beingCaptured, capturingTeam, capturingBoard, teammate)) {
                tempBitBoard.set(beingCaptured);
            }
        }

        // CAPTURE RIGHT
        beingCaptured = move.destination + 1;
        teammate = move.destination + 2;
        if (board.isValid(beingCaptured) &&
                board.inRow(move.destination, beingCaptured) &&
                oppositeBoard.get(beingCaptured)) {
            if (board.isValid(teammate) && board.inRow(capturer, teammate) &&
                    isHostile(beingCaptured, capturingTeam, capturingBoard, teammate)) {
                tempBitBoard.set(beingCaptured);
            }
        }

        return tempBitBoard;
    }

    @Override
    protected boolean isHostile(int beingCaptured, int capturingTeam, BitBoard capturingBoard, int oppositeCell) {
        int king = board.getKing();
        return board.isValid(oppositeCell) &&
                (capturingBoard.get(oppositeCell) ||
                        (!board.canWalk(capturingTeam, oppositeCell) &&
                                king != oppositeCell &&
                                king != beingCaptured));
    }
}
