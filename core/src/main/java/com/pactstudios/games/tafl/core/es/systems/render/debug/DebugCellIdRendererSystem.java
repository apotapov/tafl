package com.pactstudios.games.tafl.core.es.systems.render.debug;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.systems.render.RenderingSystem;

public class DebugCellIdRendererSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public DebugCellIdRendererSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class),
                MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {
        rendComponent.spriteBatch.begin();
    }

    @Override
    protected void end(MapRenderingComponent rendComponent) {
        rendComponent.spriteBatch.end();
    }

    @Override
    protected void process(Entity e, MapRenderingComponent rendComponent) {
        MatchComponent match = matchMapper.get(e);
        GameBoard board = match.match.board;

        for (int i = 0; i < board.numberCells; i++) {
            Vector2 position = match.match.board.getCellPosition(i);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    "" + i,
                    position.x + Constants.BoardRenderConstants.HALF_TILE_SIZE,
                    position.y + Constants.BoardRenderConstants.HALF_TILE_SIZE);
        }
    }

}
