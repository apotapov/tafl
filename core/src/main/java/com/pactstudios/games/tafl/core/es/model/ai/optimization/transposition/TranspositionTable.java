package com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition;

import com.pactstudios.games.tafl.core.enums.EvaluationType;

public class TranspositionTable {

    int size;
    TranspositionTableEntry table[];

    public TranspositionTable(int size) {
        this.size = size;
        table = new TranspositionTableEntry[size];
        for (int i = 0; i < size; i++) {
            table[i] = new TranspositionTableEntry();
        }
    }

    /**
     * Verify whether there is a stored evaluation for a given board.
     * If so, return TRUE and copy the appropriate values into the
     * output parameter
     */
    public TranspositionTableEntry lookupBoard(int hashCode, int hashLock) {
        // Find the board's hash position in Table
        int key = Math.abs(hashCode % size);
        TranspositionTableEntry entry = table[key];

        if (entry.evalType != null && entry.hashLock == hashLock) {
            return entry;
        }

        return null;
    }

    /** Store a good evaluation found through alphabeta for a certain board
     *  position
     */
    public boolean storeBoard(int hashCode, int hashLock, int eval, EvaluationType evalType,
            int depth, int timeStamp) {
        int key = Math.abs(hashCode % size);

        // Would we erase a more useful (i.e., higher) position if we stored
        // this one? If so, don't bother!
        if (table[key].evalType == null
                || table[key].depth <= depth
                || table[key].timeStamp < timeStamp) {

            table[key].hashLock = hashCode;
            table[key].hashLock = hashLock;
            table[key].eval = eval;
            table[key].depth = depth;
            table[key].evalType = evalType;
            table[key].timeStamp = timeStamp;
        }
        return true;
    }

}
