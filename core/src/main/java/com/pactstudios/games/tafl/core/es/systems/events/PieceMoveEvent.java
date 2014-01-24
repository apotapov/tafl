package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class PieceMoveEvent extends SystemEvent {
    public GamePiece piece;
    public ModelCell start;
    public ModelCell end;

    @Override
    protected void resetForPooling() {
        piece = null;
        start = null;
        end = null;
    }
}
