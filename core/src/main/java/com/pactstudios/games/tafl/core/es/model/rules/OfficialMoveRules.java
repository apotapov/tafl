package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.pactstudios.games.tafl.core.enums.Team;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;

public class OfficialMoveRules {

    TaflMatch match;
    Array<Move> allLegalMoves;
    IntArray legalMoves;

    public OfficialMoveRules(TaflMatch match) {
        this.match = match;
        allLegalMoves = new Array<Move>();
        legalMoves = new IntArray();
    }

    public boolean isMoveLegal(int source, int destination) {
        IntArray legalMoves = legalMoves(source);
        for (int i = 0; i < legalMoves.size; i++) {
            if (legalMoves.items[i] == destination) {
                return true;
            }
        }
        return false;
    }

    public Array<Move> legalMoves() {
        return allLegalMoves;
    }

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
                    (match.canWalk(i) || source == match.king)) {
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
                    (match.canWalk(i) || source == match.king)) {
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
                    (match.canWalk(i) || source == match.king)) {
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
                    (match.canWalk(i) || source == match.king)) {
                legalMoves.add(i);
            } else {
                break;
            }
        }
    }
}
