package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogFactory;
import com.pactstudios.games.tafl.core.es.systems.events.ChangeTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem2;
import com.pactstudios.games.tafl.core.es.systems.events.MoveFinishedEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.SoundSystem;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class PieceMovementSystem extends EventProcessingSystem2<PieceMoveEvent, MoveFinishedEvent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    SoundSystem soundSystem;
    EntityFactorySystem efs;
    CellHighlightSystem highlightSystem;

    TaflDatabaseService dbService;

    Vector2 velocity;

    @SuppressWarnings("unchecked")
    public PieceMovementSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), PieceMoveEvent.class, MoveFinishedEvent.class);
        this.dbService = dbService;
        velocity = new Vector2();
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
        float distance = calculateDistance(event, matchComponent.match);

        velocity = calculateVelocity(matchComponent.match, event.move);

        efs.movePiece(matchComponent.match, event.move.clone(), velocity, distance);
        matchComponent.match.selectedPiece = Constants.BoardConstants.NO_PIECE_SELECTED;
        highlightSystem.clearCellHighlights();
    }

    private Vector2 calculateVelocity(TaflMatch match, Move move) {
        int sourceX = move.source % match.board.dimensions;
        int sourceY = move.source / match.board.dimensions;
        int destinationX = move.destination % match.board.dimensions;
        int destinationY = move.destination / match.board.dimensions;

        velocity.set(destinationX - sourceX, destinationY - sourceY);
        velocity.nor().scl(Constants.PieceConstants.PIECE_SPEED);

        return velocity;
    }

    private float calculateDistance(PieceMoveEvent event,
            TaflMatch match) {
        Vector2 position = match.getCellPosition(event.move.source);
        float x = position.x;
        float y = position.y;
        position = match.getCellPosition(event.move.destination);
        return position.dst(x, y);
    }

    private void processCapturedPieces(TaflMatch match, MoveFinishedEvent event) {
        IntArray captured =
                match.rulesEngine.getCapturedPieces(event.move.destination);

        if (captured.size > 0) {
            PieceCaptureEvent captureEvent =
                    world.createEvent(PieceCaptureEvent.class);
            captureEvent.move = event.move.clone();
            captureEvent.move.capturedPieces.addAll(captured);
            world.postEvent(this, captureEvent);
        } else {
            changeTurn(match);
        }
    }

    private void changeTurn(TaflMatch match) {
        ChangeTurnEvent event = world.createEvent(ChangeTurnEvent.class);
        world.postEvent(this, event);
    }

    private void move(TaflMatch match, MoveFinishedEvent event) {
        Entity entity = match.pieceEntities[event.move.source];
        Vector2 newPosition = match.getCellPositionCenter(event.move.destination);
        PositionComponent position = positionMapper.get(entity);
        position.position.set(newPosition);

        event.move.entry = log(match, event.move);

        match.applyMove(event.move.clone(), false);

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
