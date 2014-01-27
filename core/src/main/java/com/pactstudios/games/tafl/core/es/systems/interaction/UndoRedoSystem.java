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
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogFactory;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem2;
import com.pactstudios.games.tafl.core.es.systems.events.PieceCaptureEvent;
import com.pactstudios.games.tafl.core.es.systems.events.RedoEvent;
import com.pactstudios.games.tafl.core.es.systems.events.UndoEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.BoardUtils;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class UndoRedoSystem extends EventProcessingSystem2<UndoEvent, RedoEvent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    EntityFactorySystem efs;
    CellHighlightSystem highlightSystem;

    TaflDatabaseService dbService;

    @SuppressWarnings("unchecked")
    public UndoRedoSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), UndoEvent.class, RedoEvent.class);
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

    @Override
    protected void processEvent2(Entity e, RedoEvent event) {
        MatchComponent component = matchMapper.get(e);

        Move move = component.match.redoMove();
        if (move != null) {

            Vector2 newPosition = BoardUtils.getTilePositionCenter(move.end);
            PositionComponent position = positionMapper.get(move.piece.entity);
            position.position.set(newPosition);

            move.entry = log(component.match, move);
            dbService.updatePiece(move.piece);

            if (move.captured.size > 0) {
                PieceCaptureEvent captureEvent =
                        world.createEvent(PieceCaptureEvent.class);
                captureEvent.move = move;
                world.postEvent(this, captureEvent);
            }

            changeTurn(component.match);
        }
    }

    private void changeTurn(TaflMatch match) {
        match.rulesEngine.changeTurn();
        dbService.updateMatch(match);
        highlightSystem.highlightTeam(match.turn);
    }

    private MatchLogEntry log(TaflMatch match, Move move) {
        MatchLogEntry entry = MatchLogFactory.log(match, move);
        dbService.createLogEntry(entry);
        return entry;
    }

    @Override
    protected void processEvent(Entity e, UndoEvent event) {
        MatchComponent component = matchMapper.get(e);

        Move move = component.match.undoMove();
        if (move != null) {

            Vector2 newPosition = BoardUtils.getTilePositionCenter(move.start);
            PositionComponent position = positionMapper.get(move.piece.entity);
            position.position.set(newPosition);

            dbService.updatePiece(move.piece);

            for (GamePiece piece : move.captured) {
                piece.killed = null;
                dbService.updatePiece(piece);

                piece.entity = efs.createPiece(piece);

                ModelCell cell = component.match.board.getCell(piece.x, piece.y);
                cell.piece = piece;
            }
            dbService.deleteLogEntry(move.entry);

            changeTurn(component.match);
        }
    }

}
