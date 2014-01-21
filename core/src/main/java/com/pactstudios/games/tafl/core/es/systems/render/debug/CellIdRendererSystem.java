package com.pactstudios.games.tafl.core.es.systems.render.debug;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.components.singleton.GameBoardComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.render.RenderingSystem;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class CellIdRendererSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<GameBoardComponent> boardMapper;

    @SuppressWarnings("unchecked")
    public CellIdRendererSystem() {
        super(Aspect.getAspectForAll(GameBoardComponent.class),
                MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        boardMapper = world.getMapper(GameBoardComponent.class);
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
        GameBoardComponent board = boardMapper.get(e);

        for (int i = 0; i < board.board.width; i++) {
            for (int j = 0; j < board.board.height; j++) {
                if (board.board.getCell(i, j) != null) {
                    Vector2 position = MapUtils.getTilePosition(i, j);

                    rendComponent.font.draw(rendComponent.spriteBatch,
                            i + "," + j, position.x, position.y);
                }
            }
        }
    }

}
