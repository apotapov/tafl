package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.pactstudios.games.tafl.core.es.components.render.DirectionComponent;

public class DirectionSystem extends EntityProcessingSystem {

    ComponentMapper<VelocityComponent> velocityMapper;
    ComponentMapper<DirectionComponent> directionMapper;

    @SuppressWarnings("unchecked")
    public DirectionSystem() {
        super(Aspect.getAspectForAll(
                DirectionComponent.class,
                VelocityComponent.class));
    }

    @Override
    public void initialize() {
        super.initialize();
        velocityMapper = world.getMapper(VelocityComponent.class);
        directionMapper = world.getMapper(DirectionComponent.class);
    }

    @Override
    protected void process(Entity e) {
        DirectionComponent transform = directionMapper.get(e);
        VelocityComponent velocity = velocityMapper.get(e);
        int sigX = (int)(Math.signum(velocity.velocity.x));
        if (sigX != 0 && sigX != transform.flipHorizontal &&
                Math.abs(velocity.velocity.x) > Constants.Piece.VELOCITY_FLIP_MARGIN) {
            transform.flipHorizontal *= -1;
        }
    }

}
