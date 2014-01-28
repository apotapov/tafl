package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.systems.events.ChangeTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class PieceCaptureSystem extends EventProcessingSystem<PieceCaptureEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    TaflDatabaseService dbService;

    CellHighlightSystem highlightSystem;
    EntityFactorySystem efs;

    @SuppressWarnings("unchecked")
    public PieceCaptureSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), PieceCaptureEvent.class);
        this.dbService = dbService;
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        highlightSystem = world.getSystem(CellHighlightSystem.class);
        efs = world.getSystem(EntityFactorySystem.class);
    }

    @Override
    protected void processEvent(Entity e, PieceCaptureEvent event) {

        MatchComponent component = matchMapper.get(e);

        for (GamePiece piece : event.move.captured) {
            piece.killed = event.move.entry;
            dbService.updatePiece(piece);

            piece.entity.deleteFromWorld();
            piece.entity = null;

            ModelCell cell = component.match.board.getCell(piece.x, piece.y);
            cell.piece = null;
            highlightSystem.clearCellHighlights(cell);
            efs.createCaptureAnimation(cell);
        }
        component.match.undoStack.peek().captured.addAll(event.move.captured);
        changeTurn(component.match);
    }

    private void changeTurn(TaflMatch match) {
        ChangeTurnEvent event = world.createEvent(ChangeTurnEvent.class);
        world.postEvent(this, event);
    }
}
