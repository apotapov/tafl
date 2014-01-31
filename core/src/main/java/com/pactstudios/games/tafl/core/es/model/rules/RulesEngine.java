package com.pactstudios.games.tafl.core.es.model.rules;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public abstract class RulesEngine {

    public TaflMatch match;

    public enum RulesEngineType {
        BASIC;
    }

    public RulesEngine(TaflMatch match) {
        this.match = match;
    }

    public void changeTurn() {
        if (match.turn == Team.BLACK) {
            match.turn = Team.WHITE;
        } else {
            match.turn = Team.BLACK;
        }
    }

    public abstract Team getFirstTurn();
    public abstract Team getSecondTurn();
    public abstract IntArray getCapturedPieces(int destination);
    public abstract Team checkWinner(int destination, IntArray capturedPieces);
    public abstract Team checkWinner();

    public abstract boolean isMoveLegal(int source, int destination);
    public abstract IntArray legalMoves(int source);
    public abstract Array<Move> legalMoves();
    public abstract void calculateLegalMoves();
}
