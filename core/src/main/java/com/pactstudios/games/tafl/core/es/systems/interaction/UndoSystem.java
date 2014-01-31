package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.UndoEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
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

            Entity entity = match.pieceEntities[move.source];
            if (entity != null) {
                Vector2 newPosition = match.getCellPositionCenter(move.source);
                PositionComponent position = positionMapper.get(entity);
                position.position.set(newPosition);
            } else {
                match.pieceEntities[move.source] = efs.createPiece(
                        match, move.source, match.getPieceType(move.source));
            }

            for (int i = 0; i < move.capturedPieces.size; i++) {
                int piece = move.capturedPieces.items[i];
                entity = match.pieceEntities[piece];
                if (entity == null) {
                    match.pieceEntities[move.capturedPieces.items[i]] =
                            efs.createPiece(match, piece, match.getPieceType(piece));
                }
            }
            dbService.deleteLogEntry(move.entry);

            changeTurn(match);
        }
    }
}
