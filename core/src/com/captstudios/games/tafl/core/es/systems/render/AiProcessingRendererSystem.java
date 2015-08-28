package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.roundtriangles.games.zaria.services.resources.LocaleService;

public class AiProcessingRendererSystem extends RenderingSystem<MatchRenderingComponent> {

    LocaleService localeService;
    GlyphLayout layout;
    
    ComponentMapper<AiProcessingComponent> promptMapper;

    @SuppressWarnings("unchecked")
    public AiProcessingRendererSystem(LocaleService localeService) {
        super(Filter.allComponents(AiProcessingComponent.class),
                MatchRenderingComponent.class);

        this.localeService = localeService;
        this.layout = new GlyphLayout();
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

        String text = localeService.get(LocalizedStrings.Ai.AI_PROCESSING_THREE);

        //Not sure why this hack is needed but we are getting NPE's here on some of the devices
        if (text == null) {
            text = "THINKING.";
        }
        layout.setText(rendComponent.font, text);
        
        float backgroundWidth = layout.width * 1.2f;
        float backgroundHeight = layout.height * 3f;

        float x = - backgroundWidth / 2;
        float y = 0;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rendComponent.shapeRenderer.begin(ShapeType.Filled);

        rendComponent.shapeRenderer.setColor(Constants.AiConstants.LOADING_PROMP_COLOR);
        rendComponent.shapeRenderer.rect(
                x,
                y,
                backgroundWidth,
                backgroundHeight);

        rendComponent.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        component.timeElapsed += world.getDelta();
        if (component.timeElapsed > Constants.AiConstants.AI_THINKING_ANIMATION) {
            component.timeElapsed -= Constants.AiConstants.AI_THINKING_ANIMATION;
            component.index = (component.index + 1) % LocalizedStrings.Ai.values().length;
        }

        text = localeService.get(LocalizedStrings.Ai.values()[component.index]);
        if (text == null) {
            text = "THINKING.";
        }

        x = - layout.width / 2;
        y = backgroundHeight - layout.height;

        rendComponent.spriteBatch.begin();
        rendComponent.font.draw(rendComponent.spriteBatch, text, x, y);
        rendComponent.spriteBatch.end();
    }

}
