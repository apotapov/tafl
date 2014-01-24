package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class PieceCaptureSystem extends EventProcessingSystem<PieceCaptureEvent> {

    TaflDatabaseService dbService;

    EntityFactorySystem efs;

    @SuppressWarnings("unchecked")
    public PieceCaptureSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), PieceCaptureEvent.class);
        this.dbService = dbService;
    }

    @Override
    public void initialize() {
        super.initialize();
        efs = world.getSystem(EntityFactorySystem.class);
    }

    @Override
    protected void processEvent(Entity e, PieceCaptureEvent event) {
        for (ModelCell cell : event.capturedPieces) {

            cell.piece.killed = event.entry;
            dbService.updatePiece(cell.piece);

            cell.piece.entity.deleteFromWorld();
            cell.piece = null;
            efs.createCaptureAnimation(cell);
        }
    }

}
