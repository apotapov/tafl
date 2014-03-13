package com.captstudios.games.tafl.core.es.systems.interaction;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.systems.event.EventProcessingSystem;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.systems.events.ChangeTurnEvent;
import com.captstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.captstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.captstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;

public class PieceCaptureSystem extends EventProcessingSystem<PieceCaptureEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    CellHighlightSystem highlightSystem;
    EntityFactorySystem efs;

    @SuppressWarnings("unchecked")
    public PieceCaptureSystem() {
        super(Filter.allComponents(MatchComponent.class), PieceCaptureEvent.class);
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

        if (event.move.pieceType == Constants.BoardConstants.BLACK_TEAM) {
            component.match.removePieces(Constants.BoardConstants.WHITE_TEAM, event.move.capturedPieces);
            component.match.removePieces(Constants.BoardConstants.KING, event.move.capturedPieces);
        } else {
            component.match.removePieces(Constants.BoardConstants.BLACK_TEAM, event.move.capturedPieces);
        }

        for (int i = event.move.capturedPieces.nextSetBit(0); i >= 0; i = event.move.capturedPieces.nextSetBit(i+1)) {
            highlightSystem.clearCellHighlights(i);
            efs.createCaptureAnimation(component.match, i);
        }
        component.match.board.undoStack.peek().capturedPieces.set(event.move.capturedPieces);
        changeTurn(component.match);
    }

    private void changeTurn(TaflMatch match) {
        ChangeTurnEvent event = world.createEvent(ChangeTurnEvent.class);
        world.postEvent(this, event);
    }
}
