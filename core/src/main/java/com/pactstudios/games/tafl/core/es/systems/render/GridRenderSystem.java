package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

public class GridRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public GridRenderSystem() {
        super(Filter.allComponents(MatchComponent.class), MatchRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        rendComponent.spriteBatch.begin();
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
        rendComponent.spriteBatch.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
        rendComponent.spriteBatch.draw(
                rendComponent.gridTexture,
                Constants.BoardRenderConstants.GRID_RENDER_POSITION_X,
                Constants.BoardRenderConstants.GRID_RENDER_POSITION_Y);
    }
}
