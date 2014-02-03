package com.pactstudios.games.tafl.core.es.systems.input;

import java.util.BitSet;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.InputType;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;

public class MatchInputSystem extends InputProcessingSystem<MapRenderingComponent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    CellHighlightSystem highlightSystem;

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
                    int cellId = match.board.getCellId(gameTouchPoint);
                    if (cellId >= 0 && cellId < match.board.numberCells) {
                        if (match.board.bitBoards[match.turn].get(cellId)) {
                            selectPiece(match, cellId);
                        } else if (match.board.selectedPiece != Constants.BoardConstants.ILLEGAL_CELL) {
                            movePiece(match, cellId);
                        }
                    }
                }
            }
        }
    }

    private void movePiece(TaflMatch match, int destination) {
        if (match.rulesEngine.isMoveLegal(match.board.selectedPiece, destination)) {
            move(match.turn, match.board.selectedPiece, destination);
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
        if (cellId != match.board.selectedPiece) {
            highlightSystem.clearCellHighlights();
            highlightSystem.highlightCell(cellId);
            BitSet legalMoves = match.rulesEngine.legalMoves(cellId);
            highlightSystem.highlightCells(legalMoves);
            match.board.selectedPiece = cellId;
        }
    }
}
