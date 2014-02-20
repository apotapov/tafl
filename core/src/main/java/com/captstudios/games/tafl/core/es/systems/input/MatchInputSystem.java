package com.captstudios.games.tafl.core.es.systems.input;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.systems.events.InputEvent;
import com.captstudios.games.tafl.core.es.systems.events.MoveFinishedEvent;
import com.captstudios.games.tafl.core.es.systems.events.PieceDragEvent;
import com.captstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.captstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;

public class MatchInputSystem extends InputProcessingSystem<MatchRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    CellHighlightSystem highlightSystem;

    Vector2 draggingLocation;

    TaflGame game;

    @SuppressWarnings("unchecked")
    public MatchInputSystem(TaflGame game) {
        super(Filter.allComponents(MatchComponent.class), MatchRenderingComponent.class);

        draggingLocation = new Vector2();

        this.game = game;
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        positionMapper = world.getMapper(PositionComponent.class);

        highlightSystem = world.getSystem(CellHighlightSystem.class);
    }

    @Override
    protected void process(Entity e, InputEvent event, Vector2 gameTouchPoint) {
        MatchComponent matchComponent = matchMapper.get(e);
        TaflMatch match = matchComponent.match;
        if (matchComponent.acceptInput()) {
            int cellId;
            if (matchComponent.dragging > Constants.GameConstants.DRAG_THRESHOLD) {
                draggingLocation.set(gameTouchPoint).add(game.deviceType.dragOffset);
                cellId = match.board.getCellId(draggingLocation);
            } else {
                cellId = match.board.getCellId(gameTouchPoint);
            }
            if (cellId >= 0 && cellId < match.board.boardSize) {
                switch(event.type) {
                case TOUCH_UP:
                    if (match.board.selectedPiece != Constants.BoardConstants.ILLEGAL_CELL) {
                        if (match.board.selectedPiece == cellId ||
                                !movePiece(match, cellId, matchComponent.dragging)) {
                            resetPiece(matchComponent);
                        }
                    }
                    matchComponent.dragging = 0;
                    matchComponent.draggedPiece = Constants.BoardConstants.ILLEGAL_CELL;
                    break;
                case TOUCH_DOWN:
                    if (match.board.bitBoards[match.turn].get(cellId)) {
                        selectPiece(match, cellId);
                        matchComponent.dragging += world.getDelta();
                        matchComponent.draggedPiece = cellId;
                    }
                    break;
                case TOUCH_DRAG:
                    if (matchComponent.draggedPiece != Constants.BoardConstants.ILLEGAL_CELL) {
                        matchComponent.dragging += world.getDelta();
                        dragPiece(gameTouchPoint);
                    }
                    break;
                }
            }
        }
    }

    private void resetPiece(MatchComponent component) {
        PieceDragEvent event = world.createEvent(PieceDragEvent.class);
        Vector2 position = component.match.board.getCellPositionCenter(component.match.board.selectedPiece);
        event.touchPoint.set(position.x, position.y);
        world.postEvent(this, event);
    }

    private void dragPiece(Vector2 gameTouchPoint) {
        PieceDragEvent event = world.createEvent(PieceDragEvent.class);
        event.touchPoint.set(gameTouchPoint).add(game.deviceType.dragOffset);
        world.postEvent(this, event);
    }

    private boolean movePiece(TaflMatch match, int destination, float dragging) {
        if (match.board.rules.isMoveLegal(match.turn, match.board.selectedPiece, destination)) {
            move(match.turn, match.board.selectedPiece, destination, dragging);
            return true;
        }
        return false;
    }

    private void move(int pieceType, int source, int destination, float dragging) {
        if (dragging > Constants.GameConstants.DRAG_THRESHOLD) {
            MoveFinishedEvent event = world.createEvent(MoveFinishedEvent.class);
            event.move = Move.movePool.obtain();
            event.move.pieceType = pieceType;
            event.move.source = source;
            event.move.destination = destination;
            world.postEvent(this, event);
        } else {
            PieceMoveEvent event = world.createEvent(PieceMoveEvent.class);
            event.move = Move.movePool.obtain();
            event.move.pieceType = pieceType;
            event.move.source = source;
            event.move.destination = destination;
            world.postEvent(this, event);
        }
    }

    private boolean selectPiece(TaflMatch match, int cellId) {
        if (cellId != match.board.selectedPiece) {
            highlightSystem.clearCellHighlights();
            highlightSystem.highlightCell(match, cellId);
            BitBoard legalMoves = match.board.rules.getLegalMoves(match.turn, cellId);
            highlightSystem.highlightCells(match, legalMoves);
            match.board.selectedPiece = cellId;
            return true;
        }
        return false;
    }
}
