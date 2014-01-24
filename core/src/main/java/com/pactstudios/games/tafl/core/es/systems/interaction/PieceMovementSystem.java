package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogFactory;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.SoundSystem;
import com.pactstudios.games.tafl.core.utils.BoardUtils;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class PieceMovementSystem extends EventProcessingSystem<PieceMoveEvent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    CellHighlightSystem highlightSystem;
    SoundSystem soundSystem;

    TaflDatabaseService dbService;

    @SuppressWarnings("unchecked")
    public PieceMovementSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), PieceMoveEvent.class);
        this.dbService = dbService;
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        positionMapper = world.getMapper(PositionComponent.class);

        highlightSystem = world.getSystem(CellHighlightSystem.class);
        soundSystem = world.getSystem(SoundSystem.class);
    }

    @Override
    protected void processEvent(Entity e, PieceMoveEvent event) {
        MatchComponent matchComponent = matchMapper.get(e);
        TaflMatch match = matchComponent.match;

        //matchComponent.animationInProgress = true;

        MatchLogEntry entry = move(match, event);
        clearSelection(match);
        processCapturedPieces(event, match, entry);
    }

    private void processCapturedPieces(PieceMoveEvent event, TaflMatch match, MatchLogEntry entry) {
        Array<ModelCell> capturedPieces =
                match.rulesEngine.getCapturedPieces(event.end);

        if (capturedPieces.size > 0) {
            PieceCaptureEvent captureEvent =
                    world.createEvent(PieceCaptureEvent.class);
            captureEvent.capturedPieces.addAll(capturedPieces);
            captureEvent.entry = entry;
            world.postEvent(this, captureEvent);
        }

        checkEndGame(event, match, capturedPieces);
    }

    private void checkEndGame(PieceMoveEvent event, TaflMatch match,
            Array<ModelCell> capturedPieces) {
        Lifecycle lifecycle =
                match.rulesEngine.checkGameState(event.end, capturedPieces);
        if (lifecycle == Lifecycle.PLAY) {
            match.rulesEngine.changeTurn();
            dbService.updateMatch(match);
            highlightSystem.highlightTeam(match.turn);
        } else {
            LifecycleEvent lce = world.createEvent(LifecycleEvent.class);
            lce.lifecycle = lifecycle;
            world.postEvent(this, lce);
        }
    }

    private void clearSelection(TaflMatch match) {
        match.board.selectedPiece = null;
        highlightSystem.clearCellHighlights();
    }

    private MatchLogEntry move(TaflMatch match, PieceMoveEvent event) {
        event.start.piece = null;
        event.end.piece = event.piece;
        Vector2 newPosition = BoardUtils.getTilePositionCenter(event.end);
        PositionComponent position = positionMapper.get(event.piece.entity);
        position.position.set(newPosition);

        event.piece.x = event.end.x;
        event.piece.y = event.end.y;

        dbService.updatePiece(event.piece);

        soundSystem.playMove();

        return log(match, event);
    }

    private MatchLogEntry log(TaflMatch match, PieceMoveEvent event) {
        MatchLogEntry entry = MatchLogFactory.log(match, event.piece, event.start, event.end);
        dbService.createLogEntry(entry);
        return entry;
    }

}
