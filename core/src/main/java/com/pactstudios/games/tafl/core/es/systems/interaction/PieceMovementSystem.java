package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogFactory;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
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

        move(match, event);
        clearSelection(match);
        processCapturedPieces(event, match);
    }

    private void processCapturedPieces(PieceMoveEvent event, TaflMatch match) {
        Array<GamePiece> captured =
                match.rulesEngine.getCapturedPieces(event.move.end);

        if (captured.size > 0) {
            PieceCaptureEvent captureEvent =
                    world.createEvent(PieceCaptureEvent.class);
            captureEvent.move = event.move.clone();
            captureEvent.move.captured.addAll(captured);
            world.postEvent(this, captureEvent);
        }

        checkEndGame(event, match, captured);
    }

    private void checkEndGame(PieceMoveEvent event, TaflMatch match,
            Array<GamePiece> capturedPieces) {
        Lifecycle lifecycle =
                match.rulesEngine.checkGameState(event.move.end, capturedPieces);
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

    private void move(TaflMatch match, PieceMoveEvent event) {

        Vector2 newPosition = BoardUtils.getTilePositionCenter(event.move.end);
        PositionComponent position = positionMapper.get(event.move.piece.entity);
        position.position.set(newPosition);

        event.move.entry = log(match, event.move);

        match.applyMove(event.move.clone(), true);

        dbService.updatePiece(event.move.piece);

        soundSystem.playMove();
    }

    private MatchLogEntry log(TaflMatch match, Move move) {
        MatchLogEntry entry = MatchLogFactory.log(match, move);
        dbService.createLogEntry(entry);
        return entry;
    }
}
