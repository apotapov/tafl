package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.RenderingComponent;

public class PreRenderSystem extends RenderingSystem<MapRenderingComponent> {


    @SuppressWarnings("unchecked")
    public PreRenderSystem() {
        super(Aspect.getAspectForAll(RenderingComponent.class), MapRenderingComponent.class);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {

        Gdx.gl.glClearColor(Constants.BoardRenderConstants.BACKGROUND_COLOR.r,
                Constants.BoardRenderConstants.BACKGROUND_COLOR.g,
                Constants.BoardRenderConstants.BACKGROUND_COLOR.b,
                Constants.BoardRenderConstants.BACKGROUND_COLOR.a);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rendComponent.camera.update();
        rendComponent.spriteBatch.setProjectionMatrix(rendComponent.camera.combined);
    }

    @Override
    protected void end(MapRenderingComponent rendComponent) {
    }

    @Override
    protected void process(Entity e, MapRenderingComponent rendComponent) {
    }

}
