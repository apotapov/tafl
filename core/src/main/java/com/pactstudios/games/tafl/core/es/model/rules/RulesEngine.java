package com.pactstudios.games.tafl.core.es.model.rules;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;

public abstract class RulesEngine implements TaflMatchObserver, RulesChecker<TaflMove, TaflBoard> {

    public abstract int getFirstTurn();
    public abstract PlayerWarningEnum checkPlayerWarning(int team);

    public abstract BitBoard getCapturedPieces(TaflMove move);

    public abstract int checkWinner();
    public abstract DrawReasonEnum checkDraw(int team);

    public abstract boolean isMoveLegal(int team, int source, int destination);
    public abstract BitBoard getLegalMoves(int team, int source);
    @Override
    public abstract Array<TaflMove> allLegalMoves(int team);

    public abstract boolean isHostile(int capturingTeam, int oppositeCell);
    public abstract boolean isVulnerable(int team, int cellId);
}
