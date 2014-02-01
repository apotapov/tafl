package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.board.GameBitBoard;

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
        GameBitBoard board = match.match.board;

        for (int i = 0; i < board.dimensions; i++) {
            String index = match.match.getHorizontalCellId(i);

            TextBounds bounds = rendComponent.font.getBounds(index);
            float textXoffset = bounds.width / 2;
            float textYoffset = bounds.height / 2;

            Vector2 position = match.match.getCellPosition(i).add(
                    Constants.BoardRenderConstants.HORIZONTAL_CELL_ID_HORIZONTAL_OFFSET - textXoffset,
                    Constants.BoardRenderConstants.HORIZONTAL_CELL_ID_BOTTOM_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);

            position = match.match.getCellPosition(board.numberCells - board.dimensions + i).add(
                    Constants.BoardRenderConstants.HORIZONTAL_CELL_ID_HORIZONTAL_OFFSET - textXoffset,
                    Constants.BoardRenderConstants.HORIZONTAL_CELL_ID_TOP_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);
        }

        for (int i = 0; i < board.dimensions; i++) {
            String index = match.match.getVerticalCellId(i);

            TextBounds bounds = rendComponent.font.getBounds(index);
            float textXoffset = bounds.width / 2;
            float textYoffset = bounds.height / 2;

            Vector2 position = match.match.getCellPosition(i * board.dimensions).add(
                    Constants.BoardRenderConstants.VERTICAL_CELL_ID_LEFT_OFFSET - textXoffset,
                    Constants.BoardRenderConstants.VERTICAL_CELL_ID_VERTICAL_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);

            position = match.match.getCellPosition(i * board.dimensions + board.dimensions - 1).add(
                    Constants.BoardRenderConstants.VERTICAL_CELL_ID_RIGHT_OFFSET - textXoffset,
                    Constants.BoardRenderConstants.VERTICAL_CELL_ID_VERTICAL_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);
        }
    }

}
