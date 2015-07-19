package com.captstudios.games.tafl.core.es.systems.passive;

import com.artemis.Entity;
import com.artemis.managers.GenericGroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.BoardType;
import com.captstudios.games.tafl.core.enums.CellHighlightGroup;
import com.captstudios.games.tafl.core.es.TaflWorld;
import com.captstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.captstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.utils.HighlightManager;

public class EntityFactorySystem extends PassiveEntitySystem {

    ComponentFactorySystem componentFactory;
    EntityPieceSystem entityPieceSystem;

    SingletonComponentManager singletonManager;
    GenericGroupManager<CellHighlightGroup> groupManager;

    @Override
    public void initialize() {
        super.initialize();
        componentFactory = world.getSystem(ComponentFactorySystem.class);
        entityPieceSystem = world.getSystem(EntityPieceSystem.class);
        singletonManager = world.getManager(SingletonComponentManager.class);
        groupManager = world.getManager(HighlightManager.class);
    }

    public Entity createMatch(TaflMatch match) {
        return singletonManager.addSingletonComponent(
                componentFactory.createMatchComponent(match));
    }

    public Entity createHud(TaflMatch match) {
        return singletonManager.addSingletonComponent(
                componentFactory.createHudComponent(match));
    }

    public Entity createRenderers(TaflWorld gameWorld, TaflMatch match) {
        singletonManager.addSingletonComponent(
                componentFactory.createMapRenderingComponent(gameWorld, match));
        return singletonManager.addSingletonComponent(
                componentFactory.createHudRenderingComponent(gameWorld));
    }

    public Entity createPiece(TaflMatch match, int team, int cellId) {
        Entity e = world.createEntity();

        Vector2 position = match.board.getCellPositionCenter(cellId);
        e.addComponent(componentFactory.createPositionComponent(position));

        String graphic;
        if (match.board.kingBitBoard().get(cellId)) {
            graphic = Assets.GameGraphics.KING_PIECE;
        } else if (team == Constants.BoardConstants.WHITE_TEAM) {
            graphic = Assets.GameGraphics.WHITE_PIECE;
        } else {
            graphic = Assets.GameGraphics.BLACK_PIECE;
        }


        DrawableComponent dc = componentFactory.createDrawableComponent(Assets.GraphicFiles.ATLAS_PIECES, graphic);
        e.addComponent(dc);
        if (match.board.boardType == BoardType.BOARD_SIZE_11_11) {
            e.addComponent(componentFactory.createScalingComponent(
                    Constants.PieceConstants.SCALING_11, Constants.PieceConstants.SCALING_11));
        }

        e.addToWorld();
        return e;
    }

    public Entity createHighlightedCell(int cellId, Color color) {
        Entity e = world.createEntity();

        e.addComponent(componentFactory.createHilightComponent(cellId, color));

        groupManager.add(e, CellHighlightGroup.HIGHLIGHT);

        e.addToWorld();
        return e;
    }

    public Entity createDragHighlightedCell(int cellId, Color color) {
        Entity e = world.createEntity();

        e.addComponent(componentFactory.createHilightComponent(cellId, color));

        groupManager.add(e, CellHighlightGroup.DRAG);
        groupManager.add(e, CellHighlightGroup.HIGHLIGHT);

        e.addToWorld();
        return e;
    }



    public Entity createCaptureAnimation(TaflMatch match, int cellId) {
        Entity e = world.createEntity();

        Vector2 position = match.board.getCellPositionCenter(cellId);
        e.addComponent(componentFactory.createPositionComponent(position));

        String graphic;
        if (match.board.kingBitBoard().get(cellId)) {
            graphic = Assets.GameGraphics.KING_PIECE_CAPTURE;
        } else if (match.turn == Constants.BoardConstants.WHITE_TEAM) {
            graphic = Assets.GameGraphics.BLACK_PIECE_CAPTURE;
        } else {
            graphic = Assets.GameGraphics.WHITE_PIECE_CAPTURE;
        }

        AnimationComponent ac = componentFactory.createAnimationComponent(
                Assets.GraphicFiles.ATLAS_PIECES,
                graphic,
                PlayMode.NORMAL,
                Constants.PieceConstants.CAPTURE_FRAME_DURATION);

        e.addComponent(ac);
        if (match.board.boardType == BoardType.BOARD_SIZE_11_11) {
            e.addComponent(componentFactory.createScalingComponent(
                    Constants.PieceConstants.SCALING_11, Constants.PieceConstants.SCALING_11));
        }

        e.addToWorld();
        return e;
    }

    public void movePiece(TaflMatch match, Move move, Vector2 velocity, float distanceRemaining) {
        Entity entity = entityPieceSystem.get(move.source);
        entity.addComponent(componentFactory.createVelocityComponent(
                move, velocity, distanceRemaining));
    }

    public void createAiProcessingPrompt(Entity e) {
        e.addComponent(componentFactory.createAiProcessingComponent());
    }
}
