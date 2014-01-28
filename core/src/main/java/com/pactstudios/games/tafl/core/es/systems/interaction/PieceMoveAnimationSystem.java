package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.systems.events.MoveFinishedEvent;

public class PieceMoveAnimationSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> positionMapper;
    ComponentMapper<VelocityComponent> velocityMapper;

    Vector2 velocity;

    @SuppressWarnings("unchecked")
    public PieceMoveAnimationSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, VelocityComponent.class));
        velocity = new Vector2();
    }

    @Override
    public void initialize() {
        super.initialize();
        positionMapper = world.getMapper(PositionComponent.class);
        velocityMapper = world.getMapper(VelocityComponent.class);
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = positionMapper.get(e);
        VelocityComponent velocityComponent = velocityMapper.get(e);

        Move move = velocityComponent.move;

        if (velocityComponent.distanceRemaining > 0) {
            move(velocityComponent, position.position);
        } else {
            animationFinished(e, move);
        }
    }

    private void move(VelocityComponent velocityComponent, Vector2 position) {
        calculateVelocity(velocityComponent.move, position);

        velocity.scl(world.getDelta());
        position.add(velocity);
        velocityComponent.distanceRemaining -= Math.abs(velocity.x + velocity.y);
    }

    private void calculateVelocity(Move move, Vector2 position) {
        velocity.set(move.end.x - move.start.x, move.end.y - move.start.y);
        velocity.nor().scl(Constants.PieceConstants.PIECE_SPEED);
    }

    private void animationFinished(Entity e, Move move) {
        e.removeComponent(VelocityComponent.class);
        MoveFinishedEvent event = world.createEvent(MoveFinishedEvent.class);
        event.move = move;
        world.postEvent(this, event);
    }

}
