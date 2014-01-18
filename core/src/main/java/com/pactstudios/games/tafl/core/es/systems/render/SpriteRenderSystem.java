package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;

public class SpriteRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<DrawableComponent> drawableMapper;
    RegionRenderer renderer;

    @SuppressWarnings("unchecked")
    public SpriteRenderSystem() {
        super(Aspect.getAspectForAll(DrawableComponent.class, PositionComponent.class),
                MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        drawableMapper = world.getMapper(DrawableComponent.class);
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
        DrawableComponent drawable = drawableMapper.get(e);
        renderer.drawRegion(rendComponent, e, drawable.sprite);
    }
}
