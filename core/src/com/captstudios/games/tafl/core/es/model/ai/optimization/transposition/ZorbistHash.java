package com.captstudios.games.tafl.core.es.model.ai.optimization.transposition;

import java.util.Random;

import com.badlogic.gdx.utils.ObjectSet;

public class ZorbistHash {

    public int pieceTypes;
    public int boardSize;
    public int[][] hash;
    public int[][] hashLock;
    public ObjectSet<ZorbistHashEntry> entries;

    Random random;

    public ZorbistHash(int pieceTypes, int boardSize) {
        this.pieceTypes = pieceTypes;
        this.boardSize = boardSize;
        this.hash = new int[pieceTypes][boardSize];
        this.hashLock = new int[pieceTypes][boardSize];
        this.entries = new ObjectSet<ZorbistHashEntry>();

        this.random = new Random();
    }

    public void addEntry(ZorbistHashEntry entry) {
        this.entries.add(entry);
        this.hash[entry.pieceType][entry.cellId] = entry.hash;
        this.hashLock[entry.pieceType][entry.cellId] = entry.hashLock;
    }

    public void generate() {
        this.entries.clear();
        for (int i = 0; i < pieceTypes; i++) {
            for (int j = 0; j < boardSize; j++) {

                hash[i][j] = random.nextInt();
                hashLock[i][j] = random.nextInt();
            }
        }
    }
}
