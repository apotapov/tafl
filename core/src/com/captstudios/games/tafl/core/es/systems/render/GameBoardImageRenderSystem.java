package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

public class GameBoardImageRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public GameBoardImageRenderSystem() {
        super(Filter.allComponents(MatchComponent.class), MatchRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
        rendComponent.spriteBatch.begin();
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
        rendComponent.spriteBatch.end();
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {

        float scaleX = Constants.GameConstants.GAME_WIDTH / rendComponent.backgroundTexture.getWidth();
        float scaleY = Constants.GameConstants.GAME_HEIGHT / rendComponent.backgroundTexture.getHeight();

        float x = -rendComponent.backgroundTexture.getWidth() * scaleX / 2;
        float y = -rendComponent.backgroundTexture.getHeight() * scaleY / 2;

        rendComponent.spriteBatch.draw(
                rendComponent.backgroundTexture,
                x,
                y,
                0,
                0,
                rendComponent.backgroundTexture.getWidth(),
                rendComponent.backgroundTexture.getHeight(),
                scaleX,
                scaleY,
                0);
    }
}
