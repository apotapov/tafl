package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.systems.events.ChangeTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem2;
import com.pactstudios.games.tafl.core.es.systems.events.MoveFinishedEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.SoundSystem;

public class PieceMovementSystem extends EventProcessingSystem2<PieceMoveEvent, MoveFinishedEvent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    SoundSystem soundSystem;
    EntityFactorySystem efs;
    CellHighlightSystem highlightSystem;

    Vector2 velocity;

    @SuppressWarnings("unchecked")
    public PieceMovementSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class), PieceMoveEvent.class, MoveFinishedEvent.class);
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
        matchComponent.match.board.selectedPiece = Constants.BoardConstants.ILLEGAL_CELL;
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
        Vector2 position = match.board.getCellPosition(event.move.source);
        float x = position.x;
        float y = position.y;
        position = match.board.getCellPosition(event.move.destination);
        return position.dst(x, y);
    }

    private void processCapturedPieces(TaflMatch match, MoveFinishedEvent event) {
        BitBoard captured = match.board.rules.getCapturedPieces(event.move);

        if (captured.cardinality() > 0) {
            PieceCaptureEvent captureEvent =
                    world.createEvent(PieceCaptureEvent.class);
            captureEvent.move = event.move.clone();
            captureEvent.move.capturedPieces.set(captured);
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
        match.applyMove(event.move, false);
    }

    @Override
    protected void processEvent2(Entity e, MoveFinishedEvent event) {
        MatchComponent matchComponent = matchMapper.get(e);
        TaflMatch match = matchComponent.match;

        move(match, event);
        processCapturedPieces(match, event);
    }
}
