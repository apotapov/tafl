package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;

public class AnimationRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<AnimationComponent> animationMapper;
    RegionRenderer renderer;

    @SuppressWarnings("unchecked")
    public AnimationRenderSystem() {
        super(Aspect.getAspectForAll(
                AnimationComponent.class,
                PositionComponent.class),
                MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        animationMapper = world.getMapper(AnimationComponent.class);
        renderer = new RegionRenderer(world);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {
        rendComponent.spriteBatch.begin();
    }

    @Override
    protected void end(MapRenderingComponent rendComponent) {
        rendComponent.spriteBatch.end();
    }

    @Override
    protected void process(Entity e, MapRenderingComponent rendComponent) {
        AnimationComponent animationComponent = animationMapper.get(e);
        animationComponent.incrementStateTime(world.getDelta());
        TextureRegion region = animationComponent.getFrame();
        renderer.drawRegion(rendComponent, e, region);
    }

}
