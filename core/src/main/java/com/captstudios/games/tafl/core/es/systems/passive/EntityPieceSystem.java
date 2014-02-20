package com.captstudios.games.tafl.core.es.systems.passive;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

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
    public void applyMove(TaflMatch match, Move move) {
        Entity e = pieceEntities[move.source];
        pieceEntities[move.source] = null;
        pieceEntities[move.destination] = e;

        Entity entity = pieceEntities[move.destination];
        Vector2 newPosition = match.board.getCellPositionCenter(move.destination);
        PositionComponent position = positionMapper.get(entity);
        position.position.set(newPosition);
    }

    @Override
    public void undoMove(TaflMatch match, Move move) {
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
    public void initializeMatch(TaflMatch match) {
        pieceEntities = new Entity[match.board.boardSize];

        BitBoard board = match.board.bitBoards[Constants.BoardConstants.WHITE_TEAM];
        for (int i = board.nextSetBit(0); i >= 0; i = board.nextSetBit(i+1)) {
            pieceEntities[i] = efs.createPiece(match, Constants.BoardConstants.WHITE_TEAM, i);
        }

        board = match.board.bitBoards[Constants.BoardConstants.BLACK_TEAM];
        for (int i = board.nextSetBit(0); i >= 0; i = board.nextSetBit(i+1)) {
            pieceEntities[i] = efs.createPiece(match, Constants.BoardConstants.BLACK_TEAM, i);
        }
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
