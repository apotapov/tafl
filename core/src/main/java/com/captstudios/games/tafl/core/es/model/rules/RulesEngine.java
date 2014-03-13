package com.captstudios.games.tafl.core.es.model.rules;

import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.enums.DrawReasonEnum;
import com.captstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;

public interface RulesEngine extends TaflMatchObserver, RulesChecker {

    public int getFirstTurn();

    public BitBoard getCapturedPieces(Move move);

    public int checkWinner();
    public DrawReasonEnum checkDraw(int team);

    public BitBoard getLegalMoves(int team, int source);
    public Array<Move> allLegalMoves(int team);

    public boolean isMoveLegal(int team, int source, int destination);

    public boolean isVulnerable(int team, int cellId);
    public boolean teamCanMoveToLocation(int team, int cellId);
}
