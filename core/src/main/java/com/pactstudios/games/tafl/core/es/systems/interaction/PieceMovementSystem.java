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
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.pactstudios.games.tafl.core.es.systems.events.ChangeTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem2;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.es.systems.events.MoveFinishedEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.SoundSystem;
import com.pactstudios.games.tafl.core.utils.BoardUtils;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class PieceMovementSystem extends EventProcessingSystem2<PieceMoveEvent, MoveFinishedEvent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    SoundSystem soundSystem;
    EntityFactorySystem efs;
    CellHighlightSystem highlightSystem;

    TaflDatabaseService dbService;

    @SuppressWarnings("unchecked")
    public PieceMovementSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), PieceMoveEvent.class, MoveFinishedEvent.class);
        this.dbService = dbService;
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        positionMapper = world.getMapper(PositionComponent.class);

        soundSystem = world.getSystem(SoundSystem.class);
        efs = world.getSystem(EntityFactorySystem.class);
        highlightSystem = world.getSystem(CellHighlightSystem.class);
    }

    @Override
    protected void processEvent(Entity e, PieceMoveEvent event) {
        MatchComponent matchComponent = matchMapper.get(e);
        matchComponent.animationInProgress = true;
        efs.movePiece(event.move.clone());
        matchComponent.match.board.selectedPiece = null;
        highlightSystem.clearCellHighlights();
    }

    private void processCapturedPieces(TaflMatch match, MoveFinishedEvent event) {
        Array<GamePiece> captured =
                match.rulesEngine.getCapturedPieces(event.move.end);

        if (captured.size > 0) {
            PieceCaptureEvent captureEvent =
                    world.createEvent(PieceCaptureEvent.class);
            captureEvent.move = event.move.clone();
            captureEvent.move.captured.addAll(captured);
            world.postEvent(this, captureEvent);
        }

        checkEndGame(match, event, captured);
    }

    private void checkEndGame(TaflMatch match, MoveFinishedEvent event,
            Array<GamePiece> capturedPieces) {
        Team winner =
                match.rulesEngine.checkWinner(event.move.end, capturedPieces);
        if (winner != null) {
            Lifecycle lifecycle = Lifecycle.WIN;
            if (match.versusComputer && match.computerTeam == winner) {
                lifecycle = Lifecycle.LOSS;
            }
            LifecycleEvent lce = world.createEvent(LifecycleEvent.class);
            lce.lifecycle = lifecycle;
            lce.winner = winner;
            world.postEvent(this, lce);
        } else {
            if (capturedPieces.size == 0) {
                changeTurn(match);
            }
        }
    }

    private void changeTurn(TaflMatch match) {
        ChangeTurnEvent event = world.createEvent(ChangeTurnEvent.class);
        world.postEvent(this, event);
    }

    private void move(TaflMatch match, MoveFinishedEvent event) {

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

    @Override
    protected void processEvent2(Entity e, MoveFinishedEvent event) {
        MatchComponent matchComponent = matchMapper.get(e);
        TaflMatch match = matchComponent.match;

        move(match, event);
        processCapturedPieces(match, event);
    }
}
