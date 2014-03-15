package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

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
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rendComponent.spriteBatch.begin();
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
        rendComponent.spriteBatch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
        float scaleX = Constants.GameConstants.GAME_WIDTH / (rendComponent.backgroundTexture.getWidth() * 2) + .06f;
        float scaleY = scaleX;

        float x = -rendComponent.gridTexture.getWidth() * scaleX / 2;
        float y = -rendComponent.gridTexture.getHeight() * scaleY / 2;

        rendComponent.spriteBatch.draw(
                rendComponent.gridTexture,
                x,
                y,
                0,
                0,
                rendComponent.gridTexture.getWidth(),
                rendComponent.gridTexture.getHeight(),
                scaleX,
                scaleY,
                0);

        x = -rendComponent.braid.getWidth() / 2;
        y = y - Constants.BoardRenderConstants.BRAID_OFFSET_BOTTOM;

        rendComponent.spriteBatch.draw(
                rendComponent.braid,
                x,
                y,
                0,
                0,
                rendComponent.braid.getWidth(),
                rendComponent.braid.getHeight(),
                1,
                1,
                0);

        y = y + rendComponent.gridTexture.getHeight() * scaleY +
                Constants.BoardRenderConstants.BRAID_OFFSET_BOTTOM +
                Constants.BoardRenderConstants.BRAID_OFFSET_TOP;

        rendComponent.spriteBatch.draw(
                rendComponent.braid,
                x,
                y,
                0,
                0,
                rendComponent.braid.getWidth(),
                rendComponent.braid.getHeight(),
                1,
                1,
                0);
    }
}
