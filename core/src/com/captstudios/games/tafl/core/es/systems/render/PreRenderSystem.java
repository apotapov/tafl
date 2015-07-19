package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.Entity;
import com.artemis.Filter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.RenderingComponent;

public class PreRenderSystem extends RenderingSystem<MatchRenderingComponent> {


    @SuppressWarnings("unchecked")
    public PreRenderSystem() {
        super(Filter.allComponents(RenderingComponent.class), MatchRenderingComponent.class);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
    }

}
