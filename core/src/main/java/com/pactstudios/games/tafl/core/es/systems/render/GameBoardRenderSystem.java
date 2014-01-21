package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.GameBoardComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class GameBoardRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<GameBoardComponent> boardMapper;

    @SuppressWarnings("unchecked")
    public GameBoardRenderSystem() {
        super(Aspect.getAspectForAll(GameBoardComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        boardMapper = world.getMapper(GameBoardComponent.class);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.begin(ShapeType.Line);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);
        rendComponent.shapeRenderer.setColor(0f, 0f, 0f, 1f);
    }

    @Override
    protected void end(MapRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.end();
    }

    @Override
    protected void process(Entity e, MapRenderingComponent rendComponent) {
        GameBoardComponent boardComponent = boardMapper.get(e);

        int boardSize = boardComponent.board.width * Constants.BoardConstants.TILE_SIZE;
        for (int i = 0; i <= boardComponent.board.width; i++) {
            int location = Constants.BoardConstants.TILE_SIZE * i;
            rendComponent.shapeRenderer.line(location, 0, location, boardSize);
        }

        for (int i = 0; i <= boardComponent.board.width; i++) {
            int location = Constants.BoardConstants.TILE_SIZE * i;
            rendComponent.shapeRenderer.line(0, location, boardSize, location);
        }


        for (int i = 0; i < boardComponent.board.width; i++) {
            for (int j = 0; j < boardComponent.board.height; j++) {
                ModelCell cell = boardComponent.board.getCell(i, j);
                if (!cell.canWalk()) {
                    Vector2 position = MapUtils.getTilePosition(i, j);
                    rendComponent.shapeRenderer.line(position.x, position.y,
                            position.x + Constants.BoardConstants.TILE_SIZE, position.y + Constants.BoardConstants.TILE_SIZE);
                    rendComponent.shapeRenderer.line(position.x, position.y + Constants.BoardConstants.TILE_SIZE,
                            position.x + Constants.BoardConstants.TILE_SIZE, position.y);
                }
            }
        }
    }

}
