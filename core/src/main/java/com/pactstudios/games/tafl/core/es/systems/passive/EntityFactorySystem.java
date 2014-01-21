package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Piece;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class EntityFactorySystem extends PassiveEntitySystem {

    protected ComponentFactorySystem componentFactory;
    protected SingletonComponentManager singletonManager;
    protected GroupManager groupManager;

    @Override
    public void initialize() {
        super.initialize();
        componentFactory = world.getSystem(ComponentFactorySystem.class);
        singletonManager = world.getManager(SingletonComponentManager.class);
        groupManager = world.getManager(GroupManager.class);
    }

    public Entity createBoard(TaflLevel level) {
        return singletonManager.addSingletonComponent(
                componentFactory.createBoardComponent(level));
    }

    public Entity createHud(TaflLevel level) {
        return singletonManager.addSingletonComponent(
                componentFactory.createHudComponent(level));
    }

    public Entity createRenderers(TaflWorld gameWorld) {
        singletonManager.addSingletonComponent(
                componentFactory.createMapRenderingComponent(gameWorld));
        return singletonManager.addSingletonComponent(
                componentFactory.createHudRenderingComponent(gameWorld));
    }

    public Entity createPiece(Piece piece) {
        Entity e = world.createEntity();

        Vector2 position = MapUtils.getTilePositionCenter(piece.x, piece.y);
        e.addComponent(componentFactory.createPositionComponent(position));

        AnimationComponent ac = componentFactory.createAnimationComponent(
                Assets.Graphics.CREATURE_ATLAS,
                piece.type.graphic,
                Animation.LOOP,
                Constants.PieceConstants.FRAME_DURATION);
        e.addComponent(ac);
        e.addComponent(componentFactory.createScalingComponent(Constants.PieceConstants.SCALING, Constants.PieceConstants.SCALING));

        groupManager.add(e, piece.type.team.toString());

        e.addToWorld();
        return e;
    }

    public Entity createHighlightedCell(ModelCell cell) {
        Entity e = world.createEntity();

        e.addComponent(componentFactory.createHilightComponent(cell, Constants.BoardConstants.HIGHLIGHT_COLOR));

        groupManager.add(e, Constants.GroupConstants.HIGHLIGHTED_CELLS);

        e.addToWorld();
        return e;
    }
}
