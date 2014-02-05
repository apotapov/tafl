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

import com.badlogic.gdx.utils.Array;




/*****************************************************************************
 * PUBLIC class jcOpeningBook A hash table containing a certain number of slots
 * for well-known positions
 ****************************************************************************/

public class OpeningBook<T extends Move<?>> {
    // The hash table itself

    int tableSize;
    Array<OpeningBookEntry<T>> table;

    // Construction
    public OpeningBook(int tableSize) {
        this.tableSize = tableSize;
        table = new Array<OpeningBookEntry<T>>(tableSize);
        for (int i = 0; i < tableSize; i++) {
            table.add(new OpeningBookEntry<T>());
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
    public T query(int hashKey, int hashLock, int teamId) {
        // First, look for a match in the table
        int key = Math.abs(hashKey % tableSize);

        OpeningBookEntry<T> entry = table.get(key);

        // If the hash lock doesn't match the one for our position, get out
        if (entry.hashLock == hashLock) {
            // If there is an entry for this board in the table, verify that it
            // contains a move for the current side
            if (teamId == 0) {
                return entry.moveTeam1;
            } else {
                return entry.moveTeam2;
            }
        }
        // If we haven't found anything useful, quit
        return null;
    }

    public boolean storeMove(int hashKey, int hashLock, T moveTeam1, T moveTeam2) {
        // Where should we store this data?
        int key = Math.abs(hashKey % tableSize);

        OpeningBookEntry<T> entry = table.get(key);

        // Is there already an entry for a different board position where we
        // want to put this? If so, mark it deleted
        if (entry.hashLock != hashLock) {
            entry.moveTeam1 = moveTeam1;
            entry.moveTeam2 = moveTeam2;
        }

        return true;
    }
}