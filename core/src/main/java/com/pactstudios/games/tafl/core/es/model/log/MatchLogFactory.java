package com.pactstudios.games.tafl.core.es.model.log;

import java.util.BitSet;
import java.util.Date;

import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class MatchLogFactory implements TaflMatchObserver {

    public MatchLogEntry log(TaflMatch match, TaflMove move) {
        MatchLogEntry entry = new MatchLogEntry();
        entry.match = match;
        entry.team = move.pieceType;
        entry.source = move.source;
        entry.destination = move.destination;
        entry.updated = new Date();

        entry.capture1 = move.capturedPieces.nextSetBit(0);
        entry.capture2 = move.capturedPieces.nextSetBit(entry.capture1 + 1);
        entry.capture3 = move.capturedPieces.nextSetBit(entry.capture2 + 1);

        return entry;
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        // TODO Auto-generated method stub

    }

    @Override
    public void applyMove(TaflMatch match, TaflMove move) {
        move.entry = log(match, move);
    }

    @Override
    public void undoMove(TaflMatch match, TaflMove move) {
    }

    @Override
    public void addPiece(TaflMatch match, int team, int pieces) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePieces(TaflMatch match, int captor, BitSet capturedPieces) {
        // TODO Auto-generated method stub

    }

    @Override
    public void changeTurn(TaflMatch match) {
        // TODO Auto-generated method stub

    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
        // TODO Auto-generated method stub

    }
}
