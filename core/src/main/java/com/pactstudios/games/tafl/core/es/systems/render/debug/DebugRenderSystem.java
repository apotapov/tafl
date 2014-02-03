package com.pactstudios.games.tafl.core.es.systems.render.debug;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.render.RenderingSystem;

public class DebugRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<PositionComponent> positionMapper;

    Vector2 debugTextPosition;
    Array<Component> components;

    @SuppressWarnings("unchecked")
    public DebugRenderSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class),
                MatchRenderingComponent.class);
        this.debugTextPosition = new Vector2();
        components = new Array<Component>();
    }

    @Override
    public void initialize() {
        super.initialize();
        positionMapper = world.getMapper(PositionComponent.class);
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
        PositionComponent positionComponent = positionMapper.get(e);

        debugTextPosition.set(positionComponent.position);

        components.clear();
        e.getComponents(components);
        for (Component component : components) {
            debugTextPosition.sub(0, rendComponent.font.getLineHeight());
            rendComponent.font.draw(rendComponent.spriteBatch,
                    component.toString(), debugTextPosition.x, debugTextPosition.y);
        }
    }

}
