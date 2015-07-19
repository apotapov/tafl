package com.captstudios.games.tafl.core.es.model;

import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public interface TaflMatchObserver {

    public void initializeMatch(TaflMatch match);
    public void applyMove(TaflMatch match, Move move);
    public void undoMove(TaflMatch match, Move move);
    public void removePieces(TaflMatch match, int captor, BitBoard capturedPieces);
    public void changeTurn(TaflMatch match);
    public void gameOver(TaflMatch match, LifeCycle status);

}
