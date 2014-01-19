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
import com.pactstudios.games.tafl.core.es.model.map.cells.CornerCell;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.map.objects.Team;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent.InputType;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
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

    private void movePiece(MapComponent mapComponent, Entity entity, Vector2 position, ModelCell currentCell, ModelCell targetCell) {
        position.set(MapUtils.getTilePositionCenter(targetCell));
        currentCell.entity = null;
        targetCell.entity = entity;
        efs.removeSelection(entity);
        clearCellHighlights();
        checkCapture(mapComponent.map, entity, targetCell);
        checkWin(entity, targetCell);
        mapComponent.changeTurn();
    }

    private void checkWin(Entity entity, ModelCell targetCell) {
        if (targetCell instanceof CornerCell && groupManager.isInGroup(entity, Constants.Groups.KING)) {
            LifecycleEvent event = world.createEvent(LifecycleEvent.class);
            event.lifecycle = Lifecycle.WIN;
            world.postEvent(this, event);
        }
    }

    private void checkCapture(TaflMap map, Entity entity, ModelCell targetCell) {
        Team movingTeam = getTeam(entity);

        ModelCell first = map.getCell(targetCell.x, targetCell.y + 1);
        ModelCell second = map.getCell(targetCell.x, targetCell.y + 2);
        ModelCell third = map.getCell(targetCell.x + 1, targetCell.y + 1);
        ModelCell fourth = map.getCell(targetCell.x - 1, targetCell.y + 1);
        checkCapture(movingTeam, first, second, third, fourth);

        first = map.getCell(targetCell.x, targetCell.y - 1);
        second = map.getCell(targetCell.x, targetCell.y - 2);
        third = map.getCell(targetCell.x + 1, targetCell.y - 1);
        fourth = map.getCell(targetCell.x - 1, targetCell.y - 1);
        checkCapture(movingTeam, first, second, third, fourth);

        first = map.getCell(targetCell.x + 1, targetCell.y);
        second = map.getCell(targetCell.x + 2, targetCell.y);
        third = map.getCell(targetCell.x + 1, targetCell.y + 1);
        fourth = map.getCell(targetCell.x + 1, targetCell.y - 1);
        checkCapture(movingTeam, first, second, third, fourth);

        first = map.getCell(targetCell.x - 1, targetCell.y);
        second = map.getCell(targetCell.x - 2, targetCell.y);
        third = map.getCell(targetCell.x - 1, targetCell.y + 1);
        fourth = map.getCell(targetCell.x - 1, targetCell.y - 1);
        checkCapture(movingTeam, first, second, third, fourth);
    }

    private void checkCapture(Team movingTeam, ModelCell first, ModelCell second, ModelCell third, ModelCell fourth) {
        if (first != null && first.entity != null && second != null && second.entity != null) {
            Team firstTeam = getTeam(first.entity);
            Team secondTeam = getTeam(second.entity);
            if (movingTeam != firstTeam && secondTeam == movingTeam) {
                if (groupManager.isInGroup(first.entity, Constants.Groups.KING)) {
                    if (third != null && third.entity != null && fourth != null && fourth.entity != null) {
                        Team thirdTeam = getTeam(third.entity);
                        Team fourthTeam = getTeam(fourth.entity);
                        if (movingTeam == thirdTeam && movingTeam == fourthTeam) {
                            first.entity.deleteFromWorld();
                            first.entity = null;
                            LifecycleEvent event = world.createEvent(LifecycleEvent.class);
                            event.lifecycle = Lifecycle.LOSS;
                            world.postEvent(this, event);
                        }
                    }
                } else {
                    first.entity.deleteFromWorld();
                    first.entity = null;
                }
            }
        }
    }

    private Team getTeam(Entity e) {
        if (groupManager.isInGroup(e, Team.WHITE.toString())) {
            return Team.WHITE;
        } else {
            return Team.BLACK;
        }
    }

    private boolean legalMove(TaflMap map, Entity entity, ModelCell currentCell, ModelCell targetCell) {
        if (targetCell.entity == null && (targetCell.canWalk() || groupManager.isInGroup(entity, Constants.Groups.KING))) {
            if (currentCell.x == targetCell.x) {
                int increment = Integer.signum(targetCell.y - currentCell.y);
                for (int i = currentCell.y + increment; i != targetCell.y; i += increment) {
                    ModelCell examined = map.getCell(currentCell.x, i);
                    if (examined.entity != null) {
                        return false;
                    }
                }
                return true;
            } else if (currentCell.y == targetCell.y) {
                int increment = Integer.signum(targetCell.x - currentCell.x);
                for (int i = currentCell.x + increment; i != targetCell.x; i += increment) {
                    ModelCell examined = map.getCell(i, currentCell.y);
                    if (examined.entity != null) {
                        return false;
                    }
                }
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
