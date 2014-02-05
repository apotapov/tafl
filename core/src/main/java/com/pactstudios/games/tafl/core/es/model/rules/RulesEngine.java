package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;

public abstract class RulesEngine implements TaflMatchObserver, RulesChecker<TaflMove, TaflBoard> {

    public abstract int getFirstTurn();
    public abstract PlayerWarningEnum checkPlayerWarning();

    public abstract BitSet getCapturedPieces(TaflMove move);

    public abstract int checkWinner();
    public abstract DrawReasonEnum checkDraw();

    public abstract boolean isMoveLegal(int source, int destination);
    public abstract BitSet legalMoves(int source);
    public abstract Array<TaflMove> legalMoves();
}
