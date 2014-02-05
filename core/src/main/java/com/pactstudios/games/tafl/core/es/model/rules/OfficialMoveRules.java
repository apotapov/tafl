package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class OfficialMoveRules {

    TaflMatch match;
    Array<TaflMove> allLegalMoves;
    BitSet legalMoves;
    BitSet allPieces;

    PlayerWarningEnum lastKingWarning;
    PlayerWarningEnum lastKingEscapeWarning;
    int kingEscapePosition;

    public OfficialMoveRules(TaflMatch match) {
        this.match = match;
        allLegalMoves = new Array<TaflMove>();
        legalMoves = new BitSet(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);
        allPieces = new BitSet(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);
    }

    public boolean isMoveLegal(int source, int destination) {
        return legalMoves(source).get(destination);
    }

    public Array<TaflMove> legalMoves() {
        return allLegalMoves;
    }

    public void calculateLegalMoves() {
        calculateLegalMoves(match.turn);
    }

    public void calculateLegalMoves(int team) {
        TaflMove.movePool.freeAll(allLegalMoves);
        allLegalMoves.clear();
        allPieces.clear();
        allPieces.or(match.board.whiteBitBoard());
        allPieces.or(match.board.blackBitBoard());

        BitSet bitBoard = match.board.bitBoards[team];
        for (int source = bitBoard.nextSetBit(0); source >= 0; source = bitBoard.nextSetBit(source+1)) {
            BitSet moves = calculateMoves(source);
            for (int dest = moves.nextSetBit(0); dest >= 0; dest = moves.nextSetBit(dest+1)) {
                TaflMove move = TaflMove.movePool.obtain();
                move.pieceType = team;
                move.source = source;
                move.destination = dest;
                allLegalMoves.add(move);
            }
        }
    }

    public BitSet legalMoves(int source) {
        legalMoves.clear();
        for (TaflMove move : allLegalMoves) {
            if (move.source == source) {
                legalMoves.set(move.destination);
            }
        }
        return legalMoves;
    }

    private BitSet calculateMoves(int source) {
        legalMoves.clear();

        legalUp(source);
        legalDown(source);
        legalRight(source);
        legalLeft(source);

        return legalMoves;
    }

    private void legalUp(int source) {
        for (int i = source + match.board.dimensions; i < match.board.numberCells; i += match.board.dimensions) {
            if (!allPieces.get(i) && match.board.canWalk(source, i)) {
                legalMoves.set(i);
            } else {
                break;
            }
        }
    }

    private void legalDown(int source) {
        for (int i = source - match.board.dimensions; i >= 0; i -= match.board.dimensions) {
            if (!allPieces.get(i) && match.board.canWalk(source, i)) {
                legalMoves.set(i);
            } else {
                break;
            }
        }
    }

    private void legalRight(int source) {
        int nextRow = ((source + match.board.dimensions) / match.board.dimensions) * match.board.dimensions;
        for (int i = source + 1; i < nextRow; i++) {
            if (!allPieces.get(i) && match.board.canWalk(source, i)) {
                legalMoves.set(i);
            } else {
                break;
            }
        }
    }

    private void legalLeft(int source) {
        int previousRow = (source / match.board.dimensions) * match.board.dimensions - 1;
        for (int i = source - 1; i > previousRow; i--) {
            if (!allPieces.get(i) && match.board.canWalk(source, i)) {
                legalMoves.set(i);
            } else {
                break;
            }
        }
    }

    public PlayerWarningEnum checkPlayerWarning() {
        if (match.turn == Constants.BoardConstants.WHITE_TEAM) {
            return checkKingCaptureWarning();
        } else {
            return checkKingEscapeWarning();
        }
    }

    private PlayerWarningEnum checkKingEscapeWarning() {
        allPieces.clear();
        allPieces.or(match.board.whiteBitBoard());
        allPieces.or(match.board.blackBitBoard());
        BitSet kingLegalMoves = calculateMoves(match.board.king);

        int escapeMoveCount = 0;
        boolean certainEscape = false;
        if (match.board.nearCorners.get(match.board.king)) {
            certainEscape = true;
        } else {
            kingLegalMoves.and(match.board.corners);
            escapeMoveCount = kingLegalMoves.cardinality();
        }

        if (certainEscape || escapeMoveCount == Constants.GameConstants.TUICHI_THRESHHOLD) {
            if (match.board.king != kingEscapePosition || lastKingEscapeWarning != PlayerWarningEnum.TUICHI) {
                lastKingEscapeWarning = PlayerWarningEnum.TUICHI;
                kingEscapePosition = match.board.king;
                return lastKingEscapeWarning;
            }
        } else if (escapeMoveCount == Constants.GameConstants.RAICHI_THRESHHOLD) {
            if (match.board.king != kingEscapePosition || lastKingEscapeWarning != PlayerWarningEnum.RAICHI) {
                lastKingEscapeWarning = PlayerWarningEnum.RAICHI;
                kingEscapePosition = match.board.king;
                return lastKingEscapeWarning;
            }
        } else {
            lastKingEscapeWarning = null;
            kingEscapePosition = Constants.BoardConstants.ILLEGAL_CELL;
        }
        return null;
    }

    private PlayerWarningEnum checkKingCaptureWarning() {
        int king = match.board.king;
        int surroundingEnemy = 0;
        BitSet blackPositions = match.board.blackBitBoard();

        int kingUp = king + match.board.dimensions;
        int kingDown = king - match.board.dimensions;
        int kingLeft = king - 1;
        if (kingLeft % match.board.dimensions == match.board.dimensions - 1) {
            kingLeft = Constants.BoardConstants.ILLEGAL_CELL;
        }
        int kingRight = king + 1;
        if (kingRight % match.board.dimensions == 0) {
            kingRight = Constants.BoardConstants.ILLEGAL_CELL;
        }

        if (match.board.isValid(kingUp) && blackPositions.get(kingUp)) {
            surroundingEnemy++;
        }
        if (match.board.isValid(kingDown) && blackPositions.get(kingDown)) {
            surroundingEnemy++;
        }
        if (match.board.isValid(kingLeft) && blackPositions.get(kingLeft)) {
            surroundingEnemy++;
        }
        if (match.board.isValid(kingRight) && blackPositions.get(kingRight)) {
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
}
