package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

public class SpriteRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<DrawableComponent> drawableMapper;
    RegionRenderer renderer;

    @SuppressWarnings("unchecked")
    public SpriteRenderSystem() {
        super(Filter.allComponents(DrawableComponent.class, PositionComponent.class),
                MatchRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        drawableMapper = world.getMapper(DrawableComponent.class);
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
        DrawableComponent drawable = drawableMapper.get(e);
        renderer.drawRegion(rendComponent, e, drawable.sprite);
    }
}
