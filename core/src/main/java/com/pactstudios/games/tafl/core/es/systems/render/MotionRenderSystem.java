package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.utils.BoardUtils;

public class MotionRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<VelocityComponent> velocityMapper;

    @SuppressWarnings("unchecked")
    public MotionRenderSystem() {
        super(Aspect.getAspectForAll(VelocityComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        velocityMapper = world.getMapper(VelocityComponent.class);
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
        VelocityComponent velocityComponent = velocityMapper.get(e);
        ShapeRenderer shapeRenderer = rendComponent.shapeRenderer;

        shapeRenderer.setColor(Constants.PieceConstants.PATH_COLOR);

        ModelCell start = velocityComponent.move.start;
        ModelCell end = velocityComponent.move.end;
        if (start.x - end.x == 0) {
            int y = start.y;
            int direction = (int) Math.signum(end.y - start.y);
            while (y != end.y) {
                drawCell(shapeRenderer, start.x, y);
                y += direction;
            }
        } else {
            int x = start.x;
            int direction = (int) Math.signum(end.x - start.x);
            while (x != end.x) {
                drawCell(shapeRenderer, x, start.y);
                x += direction;
            }
        }
        shapeRenderer.setColor(Constants.PieceConstants.END_COLOR);
        drawCell(shapeRenderer, end.x, end.y);
    }

    private void drawCell(ShapeRenderer shapeRenderer, int x, int y) {
        Vector2 position = BoardUtils.getTilePosition(x, y);
        shapeRenderer.rect(position.x, position.y, Constants.BoardConstants.TILE_SIZE, Constants.BoardConstants.TILE_SIZE);
    }
}
