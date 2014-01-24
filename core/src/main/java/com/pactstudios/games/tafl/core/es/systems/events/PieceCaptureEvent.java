package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;

public class PieceCaptureEvent extends SystemEvent {
    public Array<ModelCell> capturedPieces = new Array<ModelCell>();
    public MatchLogEntry entry;

    @Override
    protected void resetForPooling() {
        capturedPieces.clear();
        entry = null;
    }
}
