package com.pactstudios.games.tafl.core.es.systems.render.debug;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.systems.render.RenderingSystem;
import com.pactstudios.games.tafl.core.utils.BoardUtils;

public class CellIdRendererSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public CellIdRendererSystem() {
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

        for (int i = 0; i < board.dimentions; i++) {
            for (int j = 0; j < board.dimentions; j++) {
                if (board.getCell(i, j) != null) {
                    Vector2 position = BoardUtils.getTilePosition(i, j);

                    rendComponent.font.draw(rendComponent.spriteBatch,
                            i + "," + j,
                            position.x + Constants.BoardConstants.HALF_TILE_SIZE,
                            position.y + Constants.BoardConstants.HALF_TILE_SIZE);
                }
            }
        }
    }

}
