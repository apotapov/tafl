package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

public class AnimationRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<AnimationComponent> animationMapper;
    RegionRenderer renderer;

    @SuppressWarnings("unchecked")
    public AnimationRenderSystem() {
        super(Filter.allComponents(
                AnimationComponent.class,
                PositionComponent.class),
                MatchRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        animationMapper = world.getMapper(AnimationComponent.class);
        renderer = new RegionRenderer(world);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
        rendComponent.spriteBatch.begin();
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
        rendComponent.spriteBatch.end();
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
        AnimationComponent animationComponent = animationMapper.get(e);
        if (animationComponent.isFinished()) {
            e.deleteFromWorld();
        } else {
            animationComponent.incrementStateTime(world.getDelta());
            TextureRegion region = animationComponent.getFrame();
            renderer.drawRegion(rendComponent, e, region);
        }
    }

}
