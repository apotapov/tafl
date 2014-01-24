package com.pactstudios.games.tafl.core.es.model.rules;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;

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

    public boolean checkTurn(GamePiece piece) {
        return piece != null && piece.type.team == match.turn;
    }

    public abstract Team getFirstTurn();
    public abstract Array<ModelCell> getCapturedPieces(ModelCell end);
    public abstract Lifecycle checkGameState(ModelCell end, Array<ModelCell> capturedPieces);
    public abstract boolean legalMove(GamePiece piece, ModelCell start, ModelCell end);
    public abstract Array<ModelCell> legalMoves(ModelCell start);
    public abstract void populateBoard();
}
