package com.pactstudios.games.tafl.core.es.systems.render.debug;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.systems.render.RenderingSystem;

public class DebugCellIdRendererSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public DebugCellIdRendererSystem() {
        super(Filter.allComponents(MatchComponent.class),
                MatchRenderingComponent.class);
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
        MatchComponent match = matchMapper.get(e);
        TaflBoard board = match.match.board;

        for (int i = 0; i < board.boardSize; i++) {
            Vector2 position = match.match.board.getCellPosition(i);

            rendComponent.debugFont.draw(rendComponent.spriteBatch,
                    "" + i,
                    position.x + Constants.BoardRenderConstants.HALF_TILE_SIZE,
                    position.y + Constants.BoardRenderConstants.HALF_TILE_SIZE);
        }
    }

}
