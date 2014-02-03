package com.pactstudios.games.tafl.core.es.model.ai.optimization.moves;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;

/*************************************************************************
 * HistoryTable - A heuristic used to pick an order of evaluation for moves
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
 * and computation than creating a ton of successor board objects, so we
 * hope that, on average, it will still be more efficient overall.
 *
 ************************************************************************/
public class HistoryTable {

    private static class MoveComparator implements Comparator<Move> {

        public int currentHistory[][];

        @Override
        public int compare(Move mov1, Move mov2) {
            if (currentHistory[mov1.source][mov1.destination] >
            currentHistory[mov2.source][mov2.destination]) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private static final int NUMBER_OF_TEAMS = 2;

    // the table itself; a separate set of cutoff counters exists for each
    // side
    int history[][][];

    int boardSize;

    // A comparator, used to sort the moves
    private MoveComparator moveComparator;

    public HistoryTable(int boardSize) {
        this.boardSize = boardSize;
        history = new int[NUMBER_OF_TEAMS][boardSize][boardSize];
        moveComparator = new MoveComparator();
    }

    public boolean sortMoveList(Array<Move> moves, int teamId) {
        // Which history will we use?
        moveComparator.currentHistory = history[teamId];
        moves.sort(moveComparator);
        return true;
    }

    // History table compilation
    public boolean addCount(Move move, int teamId) {
        history[teamId][move.source][move.destination]++;
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
}