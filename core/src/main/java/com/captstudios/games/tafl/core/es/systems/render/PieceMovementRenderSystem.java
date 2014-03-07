package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.managers.SingletonComponentManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.captstudios.games.tafl.core.es.model.TaflMatch;

public class PieceMovementRenderSystem extends RenderingSystem<MatchRenderingComponent> {

    ComponentMapper<VelocityComponent> velocityMapper;

    SingletonComponentManager singletonManager;

    @SuppressWarnings("unchecked")
    public PieceMovementRenderSystem() {
        super(Filter.allComponents(VelocityComponent.class), MatchRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        velocityMapper = world.getMapper(VelocityComponent.class);
        singletonManager = world.getManager(SingletonComponentManager.class);
    }

    @Override
    protected void begin(MatchRenderingComponent rendComponent) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rendComponent.shapeRenderer.begin(ShapeType.Filled);
        rendComponent.shapeRenderer.setProjectionMatrix(rendComponent.camera.combined);
    }

    @Override
    protected void end(MatchRenderingComponent rendComponent) {
        rendComponent.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    protected void process(Entity e, MatchRenderingComponent rendComponent) {
        MatchComponent mc = singletonManager.getSingletonComponent(MatchComponent.class);
        VelocityComponent velocityComponent = velocityMapper.get(e);
        ShapeRenderer shapeRenderer = rendComponent.shapeRenderer;

        int source = velocityComponent.move.source;
        int destination = velocityComponent.move.destination;

        shapeRenderer.setColor(Constants.BoardRenderConstants.PATH_COLOR);

        if (Math.abs(destination - source) < mc.match.board.dimensions) {
            int direction = (int) Math.signum(destination - source);
            for (int i = source + direction; i != destination; i += direction) {
                drawCell(shapeRenderer, mc.match, i);
            }
        } else {
            int direction = mc.match.board.dimensions * (int) Math.signum(destination - source);
            for (int i = source + direction; i != destination; i += direction) {
                drawCell(shapeRenderer, mc.match, i);
            }
        }

        shapeRenderer.setColor(Constants.BoardRenderConstants.END_COLOR);
        drawCell(shapeRenderer, mc.match, velocityComponent.move.source);
        drawCell(shapeRenderer, mc.match, velocityComponent.move.destination);
    }

    private void drawCell(ShapeRenderer shapeRenderer, TaflMatch match, int cellId) {
        Vector2 position = match.board.getCellPosition(cellId);
        shapeRenderer.rect(position.x,
                position.y,
                Constants.BoardRenderConstants.TILE_SIZE,
                Constants.BoardRenderConstants.TILE_SIZE);
    }
}
