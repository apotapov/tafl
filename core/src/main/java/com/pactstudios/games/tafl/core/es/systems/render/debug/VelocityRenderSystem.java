package com.pactstudios.games.tafl.core.es.systems.render.debug;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.render.RenderingSystem;

public class VelocityRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<PositionComponent> pm;

    @SuppressWarnings("unchecked")
    public VelocityRenderSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, VelocityComponent.class),
                MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        vm = world.getMapper(VelocityComponent.class);
        pm = world.getMapper(PositionComponent.class);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.begin(ShapeType.Line);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);
        rendComponent.shapeRenderer.setColor(1f, 0f, 0f, 1f);
    }

    @Override
    protected void end(MapRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.end();
    }

    @Override
    protected void process(Entity e, MapRenderingComponent rendComponent) {
        VelocityComponent vc = vm.get(e);
        PositionComponent pc = pm.get(e);

        rendComponent.shapeRenderer.line(pc.position.x, pc.position.y, pc.position.x + vc.velocity.x * 2, pc.position.y + vc.velocity.y * 2);
    }

}
