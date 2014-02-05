package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
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

        component.match.removePieces((event.move.pieceType + 1) % 2, event.move.capturedPieces);

        for (int i = event.move.capturedPieces.nextSetBit(0); i >= 0; i = event.move.capturedPieces.nextSetBit(i+1)) {
            highlightSystem.clearCellHighlights(i);
            Vector2 position = component.match.board.getCellPositionCenter(i);
            efs.createCaptureAnimation(position);
        }
        component.match.undoStack.peek().capturedPieces.clear();
        component.match.undoStack.peek().capturedPieces.or(event.move.capturedPieces);
        changeTurn(component.match);
    }

    private void changeTurn(TaflMatch match) {
        ChangeTurnEvent event = world.createEvent(ChangeTurnEvent.class);
        world.postEvent(this, event);
    }
}
