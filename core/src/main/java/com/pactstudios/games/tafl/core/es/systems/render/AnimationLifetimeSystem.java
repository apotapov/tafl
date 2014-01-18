package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;

public class AnimationLifetimeSystem extends EntityProcessingSystem {

    ComponentMapper<AnimationComponent> animationMapper;

    @SuppressWarnings("unchecked")
    public AnimationLifetimeSystem() {
        super(Aspect.getAspectForAll(AnimationComponent.class));
    }

    @Override
    public void initialize() {
        super.initialize();
        animationMapper = world.getMapper(AnimationComponent.class);
    }

    @Override
    protected void process(Entity e) {
        AnimationComponent animationComponent = animationMapper.get(e);

        if (animationComponent.isFinished()) {
            e.deleteFromWorld();
        }
    }

}
