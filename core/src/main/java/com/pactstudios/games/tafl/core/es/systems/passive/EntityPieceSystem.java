package com.pactstudios.games.tafl.core.es.systems.passive;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class EntityPieceSystem extends PassiveEntitySystem implements TaflMatchObserver {

    public Entity[] pieceEntities;

    EntityFactorySystem efs;
    ComponentMapper<PositionComponent> positionMapper;

    @Override
    public void initialize() {
        efs = world.getSystem(EntityFactorySystem.class);
        positionMapper = world.getMapper(PositionComponent.class);
    }

    public Entity get(int piece) {
        return pieceEntities[piece];
    }

    @Override
    public void applyMove(TaflMatch match, TaflMove move) {
        Entity e = pieceEntities[move.source];
        pieceEntities[move.source] = null;
        pieceEntities[move.destination] = e;

        Entity entity = pieceEntities[move.destination];
        Vector2 newPosition = match.board.getCellPositionCenter(move.destination);
        PositionComponent position = positionMapper.get(entity);
        position.position.set(newPosition);
    }

    @Override
    public void undoMove(TaflMatch match, TaflMove move) {
        Entity e = pieceEntities[move.destination];
        pieceEntities[move.destination] = null;
        pieceEntities[move.source] = e;

        Entity entity = pieceEntities[move.source];
        if (entity != null) {
            Vector2 newPosition = match.board.getCellPositionCenter(move.source);
            PositionComponent position = positionMapper.get(entity);
            position.position.set(newPosition);
        } else {
            pieceEntities[move.source] = efs.createPiece(match, move.pieceType, move.source);
        }

        for (int i = move.capturedPieces.nextSetBit(0); i >= 0; i = move.capturedPieces.nextSetBit(i+1)) {
            entity = pieceEntities[i];
            if (entity == null) {
                pieceEntities[i] = efs.createPiece(match, (move.pieceType + 1) % 2, i);
            }
        }

    }

    @Override
    public void addPiece(TaflMatch match, int team, int piece) {
        pieceEntities[piece] = efs.createPiece(match, team, piece);
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        pieceEntities = new Entity[match.board.boardSize];
    }

    @Override
    public void removePieces(TaflMatch match, int team, BitBoard capturedPieces) {
        for (int i = capturedPieces.nextSetBit(0); i >= 0; i = capturedPieces.nextSetBit(i+1)) {
            Entity e = pieceEntities[i];
            if (e != null) {
                e.deleteFromWorld();
                pieceEntities[i] = null;
            }
        }

    }

    @Override
    public void changeTurn(TaflMatch match) {
    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
    }
}
