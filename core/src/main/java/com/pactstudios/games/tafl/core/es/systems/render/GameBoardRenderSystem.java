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
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.utils.BoardUtils;

public class GameBoardRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public GameBoardRenderSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
    }

    @Override
    protected void begin(MapRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.begin(ShapeType.Line);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);
        rendComponent.shapeRenderer.setColor(Constants.BoardConstants.LINE_COLOR);
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

        drawOutline(shapeRenderer, boardSize);
        drawGrid(matchComponent.match.board, shapeRenderer, boardSize);
        drawSpecialCells(matchComponent.match, shapeRenderer);
    }

    private void drawSpecialCells(TaflMatch match, ShapeRenderer shapeRenderer) {
        drawCell(shapeRenderer, match.board.cornerCells[0]);
        drawCell(shapeRenderer, match.board.cornerCells[1]);
        drawCell(shapeRenderer, match.board.cornerCells[2]);
        drawCell(shapeRenderer, match.board.cornerCells[3]);
        drawCell(shapeRenderer, match.getCastleCell());
    }

    private void drawCell(ShapeRenderer shapeRenderer, ModelCell cell) {
        Vector2 position = BoardUtils.getTilePosition(cell.x, cell.y);

        shapeRenderer.line(position.x,
                position.y,
                position.x + Constants.BoardConstants.TILE_SIZE,
                position.y + Constants.BoardConstants.TILE_SIZE);

        shapeRenderer.line(position.x,
                position.y + Constants.BoardConstants.TILE_SIZE,
                position.x + Constants.BoardConstants.TILE_SIZE,
                position.y);
    }

    private void drawOutline(ShapeRenderer shapeRenderer, float boardSize) {
        shapeRenderer.line(0, 0, 0, boardSize);
        shapeRenderer.line(0, 0, boardSize, 0);
        shapeRenderer.line(0, boardSize, boardSize, boardSize);
        shapeRenderer.line(boardSize, 0, boardSize, boardSize);
    }

    private void drawGrid(GameBoard board, ShapeRenderer shapeRenderer,
            float boardSize) {
        for (int i = 0; i <= board.dimensions; i++) {
            int location = Constants.BoardConstants.TILE_SIZE * i +
                    Constants.BoardConstants.BOARD_FRAME_WIDTH;
            shapeRenderer.line(location, 0, location, boardSize);
            shapeRenderer.line(0, location, boardSize, location);
        }
    }

}
