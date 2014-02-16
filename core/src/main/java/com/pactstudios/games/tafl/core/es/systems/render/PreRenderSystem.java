package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.RenderingComponent;

public class PreRenderSystem extends RenderingSystem<MatchRenderingComponent> {


    @SuppressWarnings("unchecked")
    public PreRenderSystem() {
        super(Aspect.getAspectForAll(RenderingComponent.class), MatchRenderingComponent.class);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rendComponent.camera.update();
        rendComponent.spriteBatch.setProjectionMatrix(rendComponent.camera.combined);
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
    }

}
