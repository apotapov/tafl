package com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition;

import com.pactstudios.games.tafl.core.enums.EvaluationType;

/***************************************************************************
 * A small internal class containing an AB value for a given position, and a
 * "hash lock" signature used to identify collisions between board positions
 * with the same basic hashing values.
 * 
 * Note that there is no need to store the actual move leading to this value,
 * for two reasons: first, by the time we check on the transposition table, the
 * move has already been applied; second, our version of alphabeta only handles
 * moves themselves at the top level of the search, so this information would be
 * passed to non one!
 ***************************************************************************/
public class TranspositionTableEntry {
    // Data fields, beginning with the actual value of the board and whether
    // this value represents an accurate evaluation or only a boundary
    public EvaluationType evalType;
    public int eval;

    // This value was obtained through a search to what depth? 0 means that
    // it was obtained during quiescence search (which is always effectively
    // of infinite depth but only within the quiescence domain; full-width
    // search of depth 1 is still more valuable than whatever Qsearch result)
    public int depth;

    public long hash;
    // Board position signature, used to detect collisions
    public long hashLock;

    // What this entry stored so long ago that it may no longer be useful?
    // Without this, the table will slowly become clogged with old, deep search
    // results for positions with no chance of happening again, and new
    // positions (specifically the 0-depth quiescence search positions) will
    // never be stored!
    public int timeStamp;
}
