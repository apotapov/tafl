package com.pactstudios.games.tafl.core.es.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.map.TaflMap;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent.InputType;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class MapInputSystem extends InputProcessingSystem<MapRenderingComponent> {

    protected ComponentMapper<MapComponent> mapMapper;
    protected ComponentMapper<PositionComponent> positionMapper;

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
        positionMapper = world.getMapper(PositionComponent.class);
        efs = world.getSystem(EntityFactorySystem.class);
        groupManager = world.getManager(GroupManager.class);
    }

    @Override
    protected void process(Entity e, InputEvent event, Vector2 gameTouchPoint) {
        if (event.type == InputType.TOUCH_UP) {
            MapComponent mapComponent = mapMapper.get(e);
            Vector2 touchPosition = MapUtils.getMapPosition(gameTouchPoint);
            ModelCell touchedCell = mapComponent.map.getCell((int)touchPosition.x, (int)touchPosition.y);
            if (touchedCell != null) {
                if (touchedCell.entity != null && groupManager.isInGroup(touchedCell.entity, mapComponent.turn.toString())) {
                    selectPiece(touchedCell);
                } else {
                    Array<Entity> selectedEntities = groupManager.getEntities(Constants.Groups.SELECTED_PIECE);
                    if (selectedEntities.size > 0) {
                        Entity selected = selectedEntities.first();
                        PositionComponent position = positionMapper.get(selected);
                        Vector2 currentMapLocation = MapUtils.getMapPosition(position.position);
                        ModelCell currentCell = mapComponent.map.getCell((int)currentMapLocation.x, (int)currentMapLocation.y);
                        if (legalMove(mapComponent.map, selected, currentCell, touchedCell)) {
                            movePiece(mapComponent, selected, position.position, currentCell, touchedCell);
                        }
                    }
                }
            }
        }
    }

    private void movePiece(MapComponent mapComponent, Entity selected, Vector2 position, ModelCell currentCell, ModelCell targetCell) {
        position.set(MapUtils.getTilePositionCenter(targetCell));
        currentCell.entity = null;
        targetCell.entity = selected;
        efs.removeSelection(selected);
        clearCellHighlights();
        mapComponent.changeTurn();
    }

    private boolean legalMove(TaflMap map, Entity entity, ModelCell currentCell, ModelCell targetCell) {
        if (targetCell.entity == null && (targetCell.canWalk() || groupManager.isInGroup(entity, Constants.Groups.KING))) {
            if (currentCell.x == targetCell.x) {
                return true;
            } else if (currentCell.y == targetCell.y) {
                return true;
            }
        }
        return false;
    }

    private void selectPiece(ModelCell cell) {
        clearCellHighlights();
        efs.createHighlightedCell(cell);
        efs.addSelection(cell.entity);
    }

    private void clearCellHighlights() {
        Array<Entity> highlights = groupManager.getEntities(Constants.Groups.HIGHLIGHTED_CELLS);
        for (Entity highlight : highlights) {
            highlight.deleteFromWorld();
        }
    }
}
