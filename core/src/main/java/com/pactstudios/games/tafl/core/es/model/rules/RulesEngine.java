package com.pactstudios.games.tafl.core.es.model.rules;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;

public abstract class RulesEngine {

    public TaflMatch match;
    public Team turn;

    public enum RulesEngineType {
        BASIC;
    }

    public RulesEngine(TaflMatch match, Team turn) {
        this.match = match;
        this.turn = turn;
    }

    public void changeTurn() {
        if (turn == Team.BLACK) {
            turn = Team.WHITE;
        } else {
            turn = Team.BLACK;
        }
    }

    public boolean checkTurn(GamePiece piece) {
        return piece != null && piece.type.team == turn;
    }

    public abstract Array<ModelCell> getCapturedPieces(ModelCell end);
    public abstract Lifecycle checkGameState(ModelCell end, Array<ModelCell> capturedPieces);
    public abstract boolean legalMove(GamePiece piece, ModelCell start, ModelCell end);
    public abstract Array<ModelCell> legalMoves(ModelCell start);
    public abstract void populateBoard();
}
