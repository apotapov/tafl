/****************************************************************************
 * jcOpeningBook - A hash table of well-known positions and moves
 *
 * Chess programs are notoriously bad at deciding what to do with complicated
 * positions, so everyone "cheats" by giving them a library of opening positions
 * taken from the ECO or something like that.  This one is very primitive and
 * contains very little, but it gets the job done.
 *
 * History:
 * 19.09.00 Creation
 *
 ****************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.optimization.moves;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.Move;



/*****************************************************************************
 * PUBLIC class jcOpeningBook A hash table containing a certain number of slots
 * for well-known positions
 ****************************************************************************/

public class OpeningBook {
    // The hash table itself

    int tableSize;
    OpeningBookEntry table[];

    // Construction
    public OpeningBook(int tableSize) {
        this.tableSize = tableSize;
        table = new OpeningBookEntry[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = new OpeningBookEntry();
        }
    }

    /**
     * Querying the table for a ready-made move to play. Return null if there
     * is none
     * 
     * @param hashKey
     * @param hashLock
     * @param teamId
     * @return
     */
    public Move query(int hashKey, int hashLock, int teamId) {
        // First, look for a match in the table
        int key = Math.abs(hashKey % tableSize);

        // If the hash lock doesn't match the one for our position, get out
        if (table[key].hashLock == hashLock) {
            // If there is an entry for this board in the table, verify that it
            // contains a move for the current side
            return table[key].moves[teamId];
        }
        // If we haven't found anything useful, quit
        return null;
    }

    public boolean storeMove(int hashKey, int hashLock, int teamId, Move move) {
        // Where should we store this data?
        int key = Math.abs(hashKey % tableSize);

        // Is there already an entry for a different board position where we
        // want to put this? If so, mark it deleted
        if (table[key].hashLock != hashLock) {
            // And store the new move
            table[key].moves[teamId].copy(move);
            table[key].moves[(teamId + 1) % 2] = null;
        }

        return true;
    }
}