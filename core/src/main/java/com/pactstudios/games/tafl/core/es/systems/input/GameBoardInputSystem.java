package com.pactstudios.games.tafl.core.es.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.GameBoardComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent.InputType;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class GameBoardInputSystem extends InputProcessingSystem<MapRenderingComponent> {

    protected ComponentMapper<GameBoardComponent> boardMapper;
    protected ComponentMapper<PositionComponent> positionMapper;

    protected EntityFactorySystem efs;

    protected GroupManager groupManager;

    @SuppressWarnings("unchecked")
    public GameBoardInputSystem() {
        super(Aspect.getAspectForAll(GameBoardComponent.class), MapRenderingComponent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        boardMapper = world.getMapper(GameBoardComponent.class);
        positionMapper = world.getMapper(PositionComponent.class);
        efs = world.getSystem(EntityFactorySystem.class);
        groupManager = world.getManager(GroupManager.class);
    }

    @Override
    protected void process(Entity e, InputEvent event, Vector2 gameTouchPoint) {
        if (event.type == InputType.TOUCH_UP) {
            GameBoardComponent boardComponent = boardMapper.get(e);
            Vector2 touchPosition = MapUtils.getMapPosition(gameTouchPoint);
            ModelCell touchedCell = boardComponent.board.getCell((int)touchPosition.x, (int)touchPosition.y);
            if (touchedCell != null) {
                if (boardComponent.rulesEngine.checkTurn(touchedCell.piece)) {
                    selectPiece(boardComponent, touchedCell);
                } else {
                    movePiece(boardComponent, touchedCell);
                }
            }
        }
    }

    private void movePiece(GameBoardComponent boardComponent, ModelCell end) {
        if (boardComponent.selectedPiece != null) {
            PositionComponent position = positionMapper.get(boardComponent.selectedPiece.entity);
            Vector2 currentMapLocation = MapUtils.getMapPosition(position.position);
            ModelCell start = boardComponent.board.getCell((int)currentMapLocation.x, (int)currentMapLocation.y);
            if (boardComponent.rulesEngine.legalMove(boardComponent.selectedPiece, start, end)) {
                move(boardComponent, start, end, position);
            }
        }
    }

    private void move(GameBoardComponent boardComponent, ModelCell start, ModelCell end,
            PositionComponent position) {
        boardComponent.rulesEngine.movePiece(position.position, start, end);
        boardComponent.selectedPiece = null;
        clearCellHighlights();
        Lifecycle lifecycle =
                boardComponent.rulesEngine.checkGameState(end);
        if (lifecycle != Lifecycle.PLAY) {
            LifecycleEvent lce = world.createEvent(LifecycleEvent.class);
            lce.lifecycle = lifecycle;
            world.postEvent(this, lce);
        }
        boardComponent.rulesEngine.changeTurn();
    }

    private void selectPiece(GameBoardComponent boardComponent, ModelCell cell) {
        clearCellHighlights();
        efs.createHighlightedCell(cell);
        boardComponent.selectedPiece = cell.piece;
    }

    private void clearCellHighlights() {
        Array<Entity> highlights = groupManager.getEntities(Constants.GroupConstants.HIGHLIGHTED_CELLS);
        for (Entity highlight : highlights) {
            highlight.deleteFromWorld();
        }
    }
}
