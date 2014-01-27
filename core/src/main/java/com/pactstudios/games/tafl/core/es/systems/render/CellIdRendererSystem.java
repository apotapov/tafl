package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
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

        for (int i = 0; i < board.dimensions; i++) {
            String index = BoardUtils.getHorizontalCellId(i);

            TextBounds bounds = rendComponent.font.getBounds(index);
            float textXoffset = bounds.width / 2;
            float textYoffset = bounds.height / 2;

            Vector2 position = BoardUtils.getTilePosition(i, 0).add(
                    Constants.BoardConstants.HORIZONTAL_CELL_ID_HORIZONTAL_OFFSET - textXoffset,
                    Constants.BoardConstants.HORIZONTAL_CELL_ID_BOTTOM_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);

            position = BoardUtils.getTilePosition(i, match.match.dimensions - 1).add(
                    Constants.BoardConstants.HORIZONTAL_CELL_ID_HORIZONTAL_OFFSET - textXoffset,
                    Constants.BoardConstants.HORIZONTAL_CELL_ID_TOP_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);
        }

        for (int i = 0; i < board.dimensions; i++) {
            String index = BoardUtils.getVerticalCellId(i);

            TextBounds bounds = rendComponent.font.getBounds(index);
            float textXoffset = bounds.width / 2;
            float textYoffset = bounds.height / 2;

            Vector2 position = BoardUtils.getTilePosition(0, i).add(
                    Constants.BoardConstants.VERTICAL_CELL_ID_LEFT_OFFSET - textXoffset,
                    Constants.BoardConstants.VERTICAL_CELL_ID_VERTICAL_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);

            position = BoardUtils.getTilePosition(match.match.dimensions - 1, i).add(
                    Constants.BoardConstants.VERTICAL_CELL_ID_RIGHT_OFFSET - textXoffset,
                    Constants.BoardConstants.VERTICAL_CELL_ID_VERTICAL_OFFSET + textYoffset);

            rendComponent.font.draw(rendComponent.spriteBatch,
                    index,
                    position.x,
                    position.y);
        }
    }

}
