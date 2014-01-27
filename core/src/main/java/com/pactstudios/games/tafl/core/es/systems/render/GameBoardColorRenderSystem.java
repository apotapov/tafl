package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.utils.BoardUtils;

public class GameBoardColorRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public GameBoardColorRenderSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.begin(ShapeType.Filled);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);
    }

    @Override
    protected void end(MapRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.end();
    }

    @Override
    protected void process(Entity e, MapRenderingComponent rendComponent) {
        MatchComponent matchComponent = matchMapper.get(e);
        ShapeRenderer shapeRenderer = rendComponent.shapeRenderer;

        float boardSize = matchComponent.match.getBoardDimensionWithBorders();

        drawBorder(shapeRenderer, boardSize);
        drawSpecialCells(matchComponent.match, shapeRenderer);
    }

    private void drawBorder(ShapeRenderer shapeRenderer, float boardSize) {
        shapeRenderer.setColor(Constants.BoardConstants.BORDER_COLOR);

        shapeRenderer.rect(0, 0, boardSize, Constants.BoardConstants.BOARD_FRAME_WIDTH);
        shapeRenderer.rect(0, 0, Constants.BoardConstants.BOARD_FRAME_WIDTH, boardSize);
        shapeRenderer.rect(boardSize - Constants.BoardConstants.BOARD_FRAME_WIDTH, 0,
                Constants.BoardConstants.BOARD_FRAME_WIDTH, boardSize);
        shapeRenderer.rect(0, boardSize - Constants.BoardConstants.BOARD_FRAME_WIDTH,
                boardSize, Constants.BoardConstants.BOARD_FRAME_WIDTH);
    }

    private void drawSpecialCells(TaflMatch match, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Constants.BoardConstants.CORNER_COLOR);
        drawCornerCell(shapeRenderer, match.board.cornerCells[0]);
        drawCornerCell(shapeRenderer, match.board.cornerCells[1]);
        drawCornerCell(shapeRenderer, match.board.cornerCells[2]);
        drawCornerCell(shapeRenderer, match.board.cornerCells[3]);

        shapeRenderer.setColor(Constants.BoardConstants.CASTLE_COLOR);
        drawCornerCell(shapeRenderer, match.getCastleCell());
    }

    private void drawCornerCell(ShapeRenderer shapeRenderer, ModelCell cell) {
        Vector2 position = BoardUtils.getTilePosition(cell.x, cell.y);
        shapeRenderer.rect(position.x, position.y, Constants.BoardConstants.TILE_SIZE, Constants.BoardConstants.TILE_SIZE);
    }

}
