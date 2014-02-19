package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Entity;
import com.artemis.managers.GenericGroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.CellHighlightGroup;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.pactstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.utils.HighlightManager;

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

    public Entity createRenderers(TaflWorld gameWorld) {
        singletonManager.addSingletonComponent(
                componentFactory.createMapRenderingComponent(gameWorld));
        return singletonManager.addSingletonComponent(
                componentFactory.createHudRenderingComponent(gameWorld));
    }

    public Entity createPiece(TaflMatch match, int team, int cellId) {
        Entity e = world.createEntity();

        Vector2 position = match.board.getCellPositionCenter(cellId);
        e.addComponent(componentFactory.createPositionComponent(position));

        String graphic;
        if (cellId == match.board.king) {
            graphic = Assets.Graphics.KING_PIECE;
        } else if (team == Constants.BoardConstants.WHITE_TEAM) {
            graphic = Assets.Graphics.WHITE_PIECE;
        } else {
            graphic = Assets.Graphics.BLACK_PIECE;
        }


        DrawableComponent dc = componentFactory.createDrawableComponent(Assets.Graphics.PIECE_ATLAS, graphic);
        e.addComponent(dc);
        //        e.addComponent(componentFactory.createScalingComponent(
        //                Constants.PieceConstants.SCALING, Constants.PieceConstants.SCALING));

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



    public Entity createCaptureAnimation(Vector2 position) {
        Entity e = world.createEntity();

        e.addComponent(componentFactory.createPositionComponent(position));

        AnimationComponent ac = componentFactory.createAnimationComponent(
                Assets.Graphics.EXPLOSION_ATLAS,
                Assets.Graphics.EXPLOSION,
                Animation.NORMAL,
                Constants.PieceConstants.EXPLOSION_FRAME_DURATION);
        e.addComponent(ac);

        e.addToWorld();
        return e;
    }

    public void movePiece(TaflMatch match, Move move, Vector2 velocity, float distanceRemaining) {
        Entity entity = entityPieceSystem.get(move.source);
        entity.addComponent(componentFactory.createVelocityComponent(
                move, velocity, distanceRemaining));
    }

    public void createAiProcessingPrompt(Entity e, String text) {
        e.addComponent(componentFactory.createAiProcessingComponent(text));
    }
}
