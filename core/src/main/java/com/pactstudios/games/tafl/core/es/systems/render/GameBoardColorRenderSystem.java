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
        shapeRenderer.setColor(Constants.BoardRenderConstants.BORDER_COLOR);

        shapeRenderer.rect(0, 0, boardSize, Constants.BoardRenderConstants.BOARD_FRAME_WIDTH);
        shapeRenderer.rect(0, 0, Constants.BoardRenderConstants.BOARD_FRAME_WIDTH, boardSize);
        shapeRenderer.rect(boardSize - Constants.BoardRenderConstants.BOARD_FRAME_WIDTH, 0,
                Constants.BoardRenderConstants.BOARD_FRAME_WIDTH, boardSize);
        shapeRenderer.rect(0, boardSize - Constants.BoardRenderConstants.BOARD_FRAME_WIDTH,
                boardSize, Constants.BoardRenderConstants.BOARD_FRAME_WIDTH);
    }

    private void drawSpecialCells(TaflMatch match, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Constants.BoardRenderConstants.CORNER_COLOR);
        drawRefugeCell(shapeRenderer, match, match.corners[0]);
        drawRefugeCell(shapeRenderer, match, match.corners[1]);
        drawRefugeCell(shapeRenderer, match, match.corners[2]);
        drawRefugeCell(shapeRenderer, match, match.corners[3]);

        shapeRenderer.setColor(Constants.BoardRenderConstants.CASTLE_COLOR);
        drawRefugeCell(shapeRenderer, match, match.center);
    }

    private void drawRefugeCell(ShapeRenderer shapeRenderer, TaflMatch match, int cellId) {
        Vector2 position = match.getCellPosition(cellId);
        shapeRenderer.rect(position.x,
                position.y,
                Constants.BoardRenderConstants.TILE_SIZE,
                Constants.BoardRenderConstants.TILE_SIZE);
    }

}
