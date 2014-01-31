package com.pactstudios.games.tafl.core.es.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent.InputType;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;

public class MatchInputSystem extends InputProcessingSystem<MapRenderingComponent> {

    protected ComponentMapper<MatchComponent> matchMapper;
    protected ComponentMapper<PositionComponent> positionMapper;

    protected CellHighlightSystem highlightSystem;

    @SuppressWarnings("unchecked")
    public MatchInputSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class), MapRenderingComponent.class);
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
        if (event.type == InputType.TOUCH_UP) {
            MatchComponent matchComponent = matchMapper.get(e);
            TaflMatch match = matchComponent.match;
            if (matchComponent.acceptInput()) {
                if (!matchComponent.animationInProgress) {
                    int cellId = match.getCellId(gameTouchPoint);
                    if (cellId >= 0 && cellId < match.board.numberCells) {
                        if (match.board.bitBoards[match.turn.bitBoardId()].get(cellId)) {
                            selectPiece(match, cellId);
                        } else if (match.selectedPiece != Constants.BoardConstants.NO_PIECE_SELECTED) {
                            movePiece(match, cellId);
                        }
                    }
                }
            }
        }
    }

    private void movePiece(TaflMatch match, int destination) {
        Entity pieceEntity = match.pieceEntities[match.selectedPiece];
        PositionComponent position = positionMapper.get(pieceEntity);

        int source = match.getCellId(position.position);
        if (match.rulesEngine.isMoveLegal(source, destination)) {
            move(match.turn.bitBoardId(), source, destination);
        }
    }

    private void move(int pieceType, int source, int destination) {
        PieceMoveEvent event = world.createEvent(PieceMoveEvent.class);
        event.move.pieceType = pieceType;
        event.move.source = source;
        event.move.destination = destination;
        world.postEvent(this, event);
    }

    private void selectPiece(TaflMatch match, int cellId) {
        if (cellId != match.selectedPiece) {
            highlightSystem.clearCellHighlights();
            highlightSystem.highlightCell(cellId);
            IntArray legalMoves = match.rulesEngine.legalMoves(cellId);
            highlightSystem.highlightCells(legalMoves);
            match.selectedPiece = cellId;
        }
    }
}
