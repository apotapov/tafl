package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntIntMap.Values;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class OfficialGameEndRules implements TaflMatchObserver {

    TaflMatch match;
    IntArray boardConfigHistory;
    IntIntMap configurationCounter;

    public OfficialGameEndRules(TaflMatch match) {
        this.match = match;
        boardConfigHistory = new IntArray();
        configurationCounter = new IntIntMap();
    }
    public int checkWinner() {
        int winner = Constants.BoardConstants.NO_TEAM;
        if (match.board.king == Constants.BoardConstants.ILLEGAL_CELL) {
            winner = Constants.BoardConstants.BLACK_TEAM;
        } else if (match.board.corners.get(match.board.king)) {
            winner = Constants.BoardConstants.WHITE_TEAM;
        }
        return winner;
    }

    public DrawReasonEnum checkDraw() {
        DrawReasonEnum reason = checkDrawMoves();
        if (reason == null) {
            reason = checkDrawThreePeat();
            if (reason == null) {
                reason = checkDrawKingTrapped();

            }
        }
        return reason;
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

    private DrawReasonEnum checkDrawKingTrapped() {
        // TODO Auto-generated method stub
        return null;
    }

    private DrawReasonEnum checkDrawMoves() {
        if (match.undoStack.size >= Constants.GameConstants.DRAW_MOVE_THRESHHOLD) {
            return DrawReasonEnum.DRAW_TOO_MANY_TURNS;
        } else if (match.rulesEngine.legalMoves().size == 0){
            if (match.turn == Constants.BoardConstants.WHITE_TEAM) {
                return DrawReasonEnum.DRAW_NO_MOVES_WHITE;
            } else {
                return DrawReasonEnum.DRAW_NO_MOVES_BLACK;
            }
        }
        return null;
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        boardConfigHistory.add(match.board.hashCode());
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
    public void addPiece(TaflMatch match, int team, int pieces) {
        boardConfigHistory.items[boardConfigHistory.size - 1] = match.board.hashCode();
    }
    @Override
    public void removePieces(TaflMatch match, int captor, BitSet capturedPieces) {
        boardConfigHistory.items[boardConfigHistory.size - 1] = match.board.hashCode();
    }
    @Override
    public void changeTurn(TaflMatch match) {
    }
    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
    }
}
