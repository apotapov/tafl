package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.SingletonComponentManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class MotionRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<VelocityComponent> velocityMapper;

    SingletonComponentManager singletonManager;

    @SuppressWarnings("unchecked")
    public MotionRenderSystem() {
        super(Aspect.getAspectForAll(VelocityComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        velocityMapper = world.getMapper(VelocityComponent.class);
        singletonManager = world.getManager(SingletonComponentManager.class);
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

        MatchComponent mc = singletonManager.getSingletonComponent(MatchComponent.class);

        //        int startX = velocityComponent.move.source;
        //        int
        //        ModelCell end = velocityComponent.move.destination;
        //        if (start.x - end.x == 0) {
        //            int y = start.y;
        //            int direction = (int) Math.signum(end.y - start.y);
        //            while (y != end.y) {
        //                drawCell(shapeRenderer, start.x, y);
        //                y += direction;
        //            }
        //        } else {
        //            int x = start.x;
        //            int direction = (int) Math.signum(end.x - start.x);
        //            while (x != end.x) {
        //                drawCell(shapeRenderer, x, start.y);
        //                x += direction;
        //            }
        //        }
        shapeRenderer.setColor(Constants.PieceConstants.END_COLOR);
        drawCell(shapeRenderer, mc.match, velocityComponent.move.destination);
    }

    private void drawCell(ShapeRenderer shapeRenderer, TaflMatch match, int cellId) {
        Vector2 position = match.getCellPosition(cellId);
        shapeRenderer.rect(position.x, position.y, Constants.BoardConstants.TILE_SIZE, Constants.BoardConstants.TILE_SIZE);
    }
}
