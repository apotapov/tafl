package com.captstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.captstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.systems.events.MoveFinishedEvent;

public class PieceMoveAnimationSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> positionMapper;
    ComponentMapper<VelocityComponent> velocityMapper;

    @SuppressWarnings("unchecked")
    public PieceMoveAnimationSystem() {
        super(Filter.allComponents(PositionComponent.class, VelocityComponent.class));
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

    private void move(VelocityComponent vc, Vector2 position) {
        position.add(vc.velocity.x * world.getDelta(), vc.velocity.y * world.getDelta());
        vc.distanceRemaining -= Math.abs(vc.velocity.x + vc.velocity.y) * world.getDelta();
    }

    private void animationFinished(Entity e, Move move) {
        e.removeComponent(VelocityComponent.class);
        MoveFinishedEvent event = world.createEvent(MoveFinishedEvent.class);
        event.move = move;
        world.postEvent(this, event);
    }

}
