package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;

public class AiProcessingRendererSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<AiProcessingComponent> promptMapper;
    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public AiProcessingRendererSystem() {
        super(Aspect.getAspectForAll(AiProcessingComponent.class, MatchComponent.class),
                MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        promptMapper = world.getMapper(AiProcessingComponent.class);
        matchMapper = world.getMapper(MatchComponent.class);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {
    }

    @Override
    protected void end(MapRenderingComponent rendComponent) {
    }

    @Override
    protected void process(Entity e, MapRenderingComponent rendComponent) {
        AiProcessingComponent component = promptMapper.get(e);
        MatchComponent matchComponent = matchMapper.get(e);
        float boardSize = matchComponent.match.board.getDimensionWithBorders();

        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        rendComponent.shapeRenderer.begin(ShapeType.Filled);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);

        rendComponent.shapeRenderer.setColor(Constants.AiConstants.LOADING_PROMP_COLOR);
        rendComponent.shapeRenderer.rect(
                (boardSize - Constants.AiConstants.LOADING_PROMPT_WIDTH) / 2,
                boardSize,
                Constants.AiConstants.LOADING_PROMPT_WIDTH,
                Constants.AiConstants.LOADING_PROMPT_HEIGHT);

        rendComponent.shapeRenderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);

        TextBounds bounds = rendComponent.font.getBounds(component.text);
        float x = (boardSize - bounds.width) / 2;
        float y = boardSize + Constants.AiConstants.LOADING_PROMPT_HEIGHT - bounds.height;

        rendComponent.spriteBatch.begin();
        rendComponent.font.draw(rendComponent.spriteBatch, component.text, x, y);
        rendComponent.spriteBatch.end();
    }

}
