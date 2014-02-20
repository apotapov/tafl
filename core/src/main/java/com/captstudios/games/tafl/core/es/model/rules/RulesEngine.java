package com.captstudios.games.tafl.core.es.model.rules;

import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.enums.DrawReasonEnum;
import com.captstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.captstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;

public abstract class RulesEngine implements TaflMatchObserver, RulesChecker {

    public abstract int getFirstTurn();
    public abstract PlayerWarningEnum checkPlayerWarning(int team);

    public abstract BitBoard getCapturedPieces(Move move);

    public abstract int checkWinner();
    public abstract DrawReasonEnum checkDraw(int team);

    public abstract boolean isMoveLegal(int team, int source, int destination);
    public abstract BitBoard getLegalMoves(int team, int source);
    public abstract Array<Move> allLegalMoves(int team);

    public abstract boolean isHostile(int capturingTeam, int oppositeCell);
    public abstract boolean isVulnerable(int team, int cellId);
    public abstract boolean teamCanMoveToLocation(int team, int cellId);
}
