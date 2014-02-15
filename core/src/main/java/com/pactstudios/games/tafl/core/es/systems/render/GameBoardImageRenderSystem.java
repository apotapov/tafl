package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

public class GameBoardImageRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public GameBoardImageRenderSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class), MatchRenderingComponent.class);
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
        rendComponent.spriteBatch.draw(
                rendComponent.backgroundTexture,
                Constants.BoardRenderConstants.BOARD_RENDER_POSITION_X,
                Constants.BoardRenderConstants.BOARD_RENDER_POSITION_Y);
    }
}
