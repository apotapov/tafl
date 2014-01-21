package com.pactstudios.games.tafl.core.utils.log;

import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class GameLogEntry {
    public Team team;
    public ModelCell start;
    public ModelCell end;

    @Override
    public String toString() {
        return team + ": " + start + "->" + end;
    }
}
