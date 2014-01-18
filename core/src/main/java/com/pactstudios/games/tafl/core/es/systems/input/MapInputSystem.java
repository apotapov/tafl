package com.pactstudios.games.tafl.core.es.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.MapComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent.InputType;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class MapInputSystem extends InputProcessingSystem<MapRenderingComponent> {

    protected ComponentMapper<MapComponent> mapMapper;

    protected EntityFactorySystem efs;

    protected GroupManager groupManager;

    @SuppressWarnings("unchecked")
    public MapInputSystem() {
        super(Aspect.getAspectForAll(MapComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        mapMapper = world.getMapper(MapComponent.class);
        efs = world.getSystem(EntityFactorySystem.class);
        groupManager = world.getManager(GroupManager.class);
    }

    @Override
    protected void process(Entity e, InputEvent event, Vector2 gameTouchPoint) {
        if (event.type == InputType.TOUCH_UP) {
            MapComponent mapComponent = mapMapper.get(e);
            Vector2 touchPosition = MapUtils.getMapPosition(gameTouchPoint);
            ModelCell cell = mapComponent.map.getCell((int)touchPosition.x, (int)touchPosition.y);
            if (cell != null) {
                if (cell.entity != null && groupManager.isInGroup(cell.entity, mapComponent.turn.toString())) {
                    selectPiece(cell);
                }
            }
        }
    }

    private void selectPiece(ModelCell cell) {
        clearSelection();
        efs.createHighlightedCell(cell);
        efs.addSelection(cell.entity);
    }

    private void clearSelection() {
        Array<Entity> highlights = groupManager.getEntities(Constants.Groups.HIGHLIGHTED_CELLS);
        for (Entity highlight : highlights) {
            highlight.deleteFromWorld();
        }
    }
}
