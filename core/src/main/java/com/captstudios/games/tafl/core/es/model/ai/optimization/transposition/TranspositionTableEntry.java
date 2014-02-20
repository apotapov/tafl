package com.captstudios.games.tafl.core.es.model.ai.optimization.transposition;

import com.captstudios.games.tafl.core.enums.EvaluationType;

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

    public int hash;

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof TranspositionTableEntry && ((TranspositionTableEntry)o).hash == hash;
    }
}
