package com.pactstudios.games.tafl.core.es.model;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

import com.pactstudios.games.tafl.core.enums.LifeCycle;

public interface TaflMatchObserver {

    public void initializeMatch(TaflMatch match);
    public void applyMove(TaflMatch match, TaflMove move);
    public void undoMove(TaflMatch match, TaflMove move);
    public void addPiece(TaflMatch match, int team, int pieces);
    public void removePieces(TaflMatch match, int captor, BitBoard capturedPieces);
    public void changeTurn(TaflMatch match);
    public void gameOver(TaflMatch match, LifeCycle status);

}
