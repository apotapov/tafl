package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class BackgroundRenderSystem extends RenderingSystem<MapRenderingComponent> {

    ComponentMapper<MapComponent> mapper;

    @SuppressWarnings("unchecked")
    public BackgroundRenderSystem() {
        super(Aspect.getAspectForAll(MapComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        mapper = world.getMapper(MapComponent.class);
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
        MapComponent component = mapper.get(e);

        for (int i = 0; i <= component.map.width; i++) {
            int location = Constants.Map.TILE_SIZE * i;
            rendComponent.shapeRenderer.line(location, 0, location, Constants.Map.MAP_SIZE);
        }

        for (int i = 0; i <= component.map.width; i++) {
            int location = Constants.Map.TILE_SIZE * i;
            rendComponent.shapeRenderer.line(0, location, Constants.Map.MAP_SIZE, location);
        }


        for (int i = 0; i < component.map.width; i++) {
            for (int j = 0; j < component.map.height; j++) {
                ModelCell cell = component.map.getCell(i, j);
                if (!cell.canWalk()) {
                    Vector2 position = MapUtils.getTilePosition(i, j);
                    rendComponent.shapeRenderer.line(position.x, position.y,
                            position.x + Constants.Map.TILE_SIZE, position.y + Constants.Map.TILE_SIZE);
                    rendComponent.shapeRenderer.line(position.x, position.y + Constants.Map.TILE_SIZE,
                            position.x + Constants.Map.TILE_SIZE, position.y);
                }
            }
        }
    }

}
