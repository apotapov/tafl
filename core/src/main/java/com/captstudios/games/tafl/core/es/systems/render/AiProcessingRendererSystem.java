package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

public class AiProcessingRendererSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<AiProcessingComponent> promptMapper;

    @SuppressWarnings("unchecked")
    public AiProcessingRendererSystem() {
        super(Filter.allComponents(AiProcessingComponent.class),
                MatchRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        promptMapper = world.getMapper(AiProcessingComponent.class);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
        AiProcessingComponent component = promptMapper.get(e);
        float x = - Constants.AiConstants.LOADING_PROMPT_WIDTH / 2;
        float y = 0;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rendComponent.shapeRenderer.begin(ShapeType.Filled);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);

        rendComponent.shapeRenderer.setColor(Constants.AiConstants.LOADING_PROMP_COLOR);
        rendComponent.shapeRenderer.rect(
                x,
                y,
                Constants.AiConstants.LOADING_PROMPT_WIDTH,
                Constants.AiConstants.LOADING_PROMPT_HEIGHT);

        rendComponent.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        TextBounds bounds = rendComponent.font.getBounds(component.text);
        x = - bounds.width / 2;
        y = Constants.AiConstants.LOADING_PROMPT_HEIGHT - bounds.height;

        rendComponent.spriteBatch.begin();
        rendComponent.font.draw(rendComponent.spriteBatch, component.text, x, y);
        rendComponent.spriteBatch.end();
    }

}
