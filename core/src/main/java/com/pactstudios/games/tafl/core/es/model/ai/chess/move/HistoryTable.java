/*************************************************************************
 * jcHistoryTable - A heuristic used to pick an order of evaluation for moves
 *
 * The history heuristic is an extension of the old "killer move" system: if
 * a move has caused a lot of cutoffs recently, it will be tried early in the
 * hope that it will do so again.
 *
 * Using the history table is a gamble.  We could do without it entirely,
 * compute "successor positions" for each possible moves, look them up in
 * the transposition table and hope to get a cutoff this way, which would
 * insure fast cutoffs whenever possible.  On the other hand, HistoryTable
 * has no knowledge of the contents of the transposition table, so it may
 * cause a deep search of several moves even though another one would result
 * in an immediate cutoff...  However, History requires far less memory
 * and computation than creating a ton of successor jcBoard objects, so we
 * hope that, on average, it will still be more efficient overall.
 *
 * History
 * 14.08.00 Creation
 ************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.chess.move;

import java.util.Collections;
import java.util.Comparator;

public class HistoryTable {
    /***********************************************************************
     * DATA MEMBERS
     **********************************************************************/

    // the table itself; a separate set of cutoff counters exists for each
    // side
    int history[][][];
    int currentHistory[][];

    // This is a singleton class; the same history can be shared by two AI's
    private static HistoryTable theInstance;

    // A comparator, used to sort the moves
    private MoveComparator moveComparator;

    /***********************************************************************
     * STATIC BLOCK
     ***********************************************************************/
    static {
        theInstance = new HistoryTable();
    }

    /***********************************************************************
     * jcMoveComparator - Inner class used in sorting moves
     **********************************************************************/
    class MoveComparator implements Comparator<Move> {
        @Override
        public int compare(Move mov1, Move mov2) {
            if (currentHistory[mov1.sourceSquare][mov1.destinationSquare] > currentHistory[mov2.sourceSquare][mov2.destinationSquare]) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /************************************************************************
     * PUBLIC METHODS
     ***********************************************************************/

    // Accessor
    public static HistoryTable getInstance() {
        return theInstance;
    }

    // Sort a list of moves, using the Java "Arrays" class as a helper
    public boolean sortMoveList(MoveListGenerator theList, int movingPlayer) {
        // Which history will we use?
        currentHistory = history[movingPlayer];

        Collections.sort(theList.getMoveList(), moveComparator);
        return true;
    }

    // History table compilation
    public boolean addCount(int whichPlayer, Move mov) {
        history[whichPlayer][mov.sourceSquare][mov.destinationSquare]++;
        return true;
    }

    // public boolean Forget
    // Once in a while, we must erase the history table to avoid ordering
    // moves according to the results of very old searches
    public boolean forget() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    history[i][j][k] = 0;
                }
            }
        }
        return true;
    }

    /************************************************************************
     * PRIVATE METHODS
     ***********************************************************************/
    private HistoryTable() {
        history = new int[2][64][64];
        moveComparator = new MoveComparator();
    }
}