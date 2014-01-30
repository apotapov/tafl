package com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition;


public class ZorbistHashEntry {
    public int boardSize;
    public int pieceType;
    public int cellId;
    public int hash;
    public int hashLock;

    public ZorbistHashEntry(int boardSize, int pieceType, int cellId, int hash, int hashLock) {
        this.boardSize = boardSize;
        this.pieceType = pieceType;
        this.cellId = cellId;
        this.hash = hash;
        this.hashLock = hashLock;
    }

    @Override
    public int hashCode() {
        return hash ^ hashLock;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ZorbistHashEntry) {
            return hashCode() == o.hashCode();
        }
        return false;
    }
}
