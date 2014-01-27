package com.pactstudios.games.tafl.core.es.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent.InputType;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.utils.BoardUtils;

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

            if (!matchComponent.animationInProgress) {
                Vector2 touchPosition = BoardUtils.getMapPosition(gameTouchPoint);
                ModelCell touchedCell = match.board.getCell(
                        (int)touchPosition.x, (int)touchPosition.y);

                if (touchedCell != null) {
                    if (match.rulesEngine.checkTurn(touchedCell.piece)) {
                        selectPiece(match, touchedCell);
                    } else if (match.board.selectedPiece != null) {
                        movePiece(match, touchedCell);
                    }
                }
            }
        }
    }

    private void movePiece(TaflMatch match, ModelCell end) {
        PositionComponent position = positionMapper.get(match.board.selectedPiece.entity);
        Vector2 currentMapLocation = BoardUtils.getMapPosition(position.position);
        ModelCell start = match.board.getCell((int)currentMapLocation.x, (int)currentMapLocation.y);
        if (match.rulesEngine.legalMove(match.board.selectedPiece, start, end)) {
            move(start, end);
        }
    }

    private void move(ModelCell start, ModelCell end) {
        PieceMoveEvent event = world.createEvent(PieceMoveEvent.class);
        event.move.piece = start.piece;
        event.move.start = start;
        event.move.end = end;
        world.postEvent(this, event);
    }

    private void selectPiece(TaflMatch match, ModelCell cell) {
        highlightSystem.clearCellHighlights();
        highlightSystem.highlightCell(cell);
        Array<ModelCell> legalMoves = match.rulesEngine.legalMoves(cell);
        highlightSystem.highlightCells(legalMoves);

        match.board.selectedPiece = cell.piece;
    }
}
