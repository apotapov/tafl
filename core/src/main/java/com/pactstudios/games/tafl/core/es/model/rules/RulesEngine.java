package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;

public abstract class RulesEngine {

    public TaflMatch match;

    public RulesEngine(TaflMatch match) {
        this.match = match;
    }

    public void changeTurn() {
        match.turn = (match.turn + 1) % 2;
    }

    public abstract int getFirstTurn();
    public abstract int getSecondTurn();
    public abstract PlayerWarningEnum checkPlayerWarning();

    public abstract BitSet getCapturedPieces(int destination);

    public abstract int checkWinner();
    public abstract DrawReasonEnum checkDraw();
    public abstract void recordBoardConfiguration(int boardHash);
    public abstract void undoBoardConfiguration();

    public abstract boolean isMoveLegal(int source, int destination);
    public abstract BitSet legalMoves(int source);
    public abstract Array<Move> legalMoves();
    public abstract void calculateLegalMoves();
}
