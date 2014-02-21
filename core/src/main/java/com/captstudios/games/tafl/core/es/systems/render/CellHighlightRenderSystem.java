package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.SingletonComponentManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.render.HighlightComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;

public class CellHighlightRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<HighlightComponent> mapper;

    SingletonComponentManager singletonManager;

    @SuppressWarnings("unchecked")
    public CellHighlightRenderSystem() {
        super(Filter.allComponents(HighlightComponent.class), MatchRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        mapper = world.getMapper(HighlightComponent.class);
        singletonManager = world.getManager(SingletonComponentManager.class);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        rendComponent.shapeRenderer.begin(ShapeType.Filled);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
        HighlightComponent component = mapper.get(e);
        rendComponent.shapeRenderer.setColor(component.color);

        MatchComponent matchComponent =
                singletonManager.getSingletonComponent(MatchComponent.class);

        Vector2 position = matchComponent.match.board.getCellPosition(component.cellId);

        rendComponent.shapeRenderer.rect(position.x,
                position.y,
                Constants.BoardRenderConstants.TILE_SIZE,
                Constants.BoardRenderConstants.TILE_SIZE);
    }

}