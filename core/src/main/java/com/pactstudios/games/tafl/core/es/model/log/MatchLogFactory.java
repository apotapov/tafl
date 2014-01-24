package com.pactstudios.games.tafl.core.es.model.log;

import java.util.Date;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class MatchLogFactory {

    public static MatchLogEntry log(TaflMatch match, GamePiece piece, ModelCell start, ModelCell end) {
        MatchLogEntry entry = new MatchLogEntry();
        entry.match = match;
        entry.piece = piece;
        entry.x1 = start.x;
        entry.y1 = start.y;
        entry.x2 = end.x;
        entry.y2 = end.y;
        entry.updated = new Date();
        return entry;
    }
}
