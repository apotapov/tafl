package com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pactstudios.games.tafl.core.enums.EvaluationType;

public class TranspositionTable {

    private class LruCache extends LinkedHashMap<TranspositionTableEntry, TranspositionTableEntry> {

        private static final long serialVersionUID = -7590996424683869190L;

        private final int maxEntries;

        TranspositionTableEntry evictedKey;
        TranspositionTableEntry evictedValue;

        public LruCache(final int maxEntries) {
            super(maxEntries + 1, 1.0f, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(final Map.Entry<TranspositionTableEntry, TranspositionTableEntry> eldest) {
            if (super.size() > maxEntries) {
                evictedKey = eldest.getKey();
                evictedValue = eldest.getValue();
                return true;
            }
            return false;
        }
    }

    LruCache lruCache;
    TranspositionTableEntry lookUp;
    TranspositionTableEntry recycledKey;
    TranspositionTableEntry recycledValue;

    public TranspositionTable(int size) {
        lruCache = new LruCache(size);
        lookUp = new TranspositionTableEntry();
    }

    /**
     * Verify whether there is a stored evaluation for a given board.
     * If so, return TRUE and copy the appropriate values into the
     * output parameter
     */
    public TranspositionTableEntry lookupBoard(int hash) {
        lookUp.hash = hash;
        return lruCache.get(lookUp);
    }

    /** Store a good evaluation found through alphabeta for a certain board
     *  position
     */
    public boolean storeBoard(int hash, int eval, EvaluationType evalType) {

        TranspositionTableEntry key = recycledKey;
        TranspositionTableEntry value = recycledValue;
        if (key == null) {
            key = new TranspositionTableEntry();
            value = new TranspositionTableEntry();
        }
        key.hash = hash;
        value.hash = hash;
        value.eval = eval;
        value.evalType = evalType;

        lruCache.put(key, value);

        if (lruCache.evictedKey != null) {
            recycledKey = lruCache.evictedKey;
            recycledValue = lruCache.evictedValue;
            lruCache.evictedKey = null;
            lruCache.evictedValue = null;
        }
        return true;
    }

}
