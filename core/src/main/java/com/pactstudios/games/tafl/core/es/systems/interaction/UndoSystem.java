package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.UndoEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.BoardUtils;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class UndoSystem extends EventProcessingSystem<UndoEvent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    EntityFactorySystem efs;
    CellHighlightSystem highlightSystem;

    TaflDatabaseService dbService;

    @SuppressWarnings("unchecked")
    public UndoSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), UndoEvent.class);
        this.dbService = dbService;
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        positionMapper = world.getMapper(PositionComponent.class);
        efs = world.getSystem(EntityFactorySystem.class);
        highlightSystem = world.getSystem(CellHighlightSystem.class);
    }

    private void changeTurn(TaflMatch match) {
        match.rulesEngine.changeTurn();
        dbService.updateMatch(match);
        highlightSystem.highlightTeam(match.turn);
    }

    @Override
    protected void processEvent(Entity e, UndoEvent event) {
        MatchComponent component = matchMapper.get(e);

        if (component.match.versusComputer && component.match.undoStack.size > 1) {
            undo(component.match);
            undo(component.match);
        } else {
            undo(component.match);
        }
    }

    private void undo(TaflMatch match) {
        Move move = match.undoMove();
        if (move != null) {

            if (move.piece.entity != null) {
                Vector2 newPosition = BoardUtils.getTilePositionCenter(move.start);
                PositionComponent position = positionMapper.get(move.piece.entity);
                position.position.set(newPosition);
            } else {
                move.piece.entity = efs.createPiece(move.piece);
            }

            dbService.updatePiece(move.piece);

            for (GamePiece piece : move.captured) {
                piece.killed = null;
                dbService.updatePiece(piece);

                if (piece.entity == null) {
                    piece.entity = efs.createPiece(piece);
                }

                ModelCell cell = match.board.getCell(piece.x, piece.y);
                cell.piece = piece;
            }
            dbService.deleteLogEntry(move.entry);

            changeTurn(match);
        }
    }
}
