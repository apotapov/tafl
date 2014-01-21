package com.pactstudios.games.tafl.core.utils.log;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class GameLog {

    public Array<GameLogEntry> log;

    Pool<GameLogEntry> entryPool;

    public GameLog() {
        log = new Array<GameLogEntry>();
        entryPool = new Pool<GameLogEntry>() {
            @Override
            protected GameLogEntry newObject() {
                return new GameLogEntry();
            }
        };
    }


    public void reset() {
        entryPool.freeAll(log);
        log.clear();
    }

    public void log(Team team, ModelCell start, ModelCell end) {
        GameLogEntry entry = entryPool.obtain();
        entry.team = team;
        entry.start = start;
        entry.end = end;
        log.add(entry);
    }
}
