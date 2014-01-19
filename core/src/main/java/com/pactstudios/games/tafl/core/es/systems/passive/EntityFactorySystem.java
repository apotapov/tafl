package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.pactstudios.games.tafl.core.es.model.map.TaflMap;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.map.objects.Piece;
import com.pactstudios.games.tafl.core.es.model.map.objects.PieceType;
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

    public Entity createMap(TaflMap map) {
        return singletonManager.addSingletonComponent(
                componentFactory.createMapComponent(map));
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
                Constants.Piece.FRAME_DURATION);
        e.addComponent(ac);
        e.addComponent(componentFactory.createDirectionComponent());
        e.addComponent(componentFactory.createScalingComponent(Constants.Piece.SCALING, Constants.Piece.SCALING));

        groupManager.add(e, piece.type.team.toString());
        if (piece.type == PieceType.KING) {
            groupManager.add(e, Constants.Groups.KING);
        }

        e.addToWorld();
        return e;
    }

    public void addSelection(Entity entity) {
        Array<Entity> selected = groupManager.getEntities(Constants.Groups.SELECTED_PIECE);
        for (Entity e : selected) {
            removeSelection(e);
        }

        groupManager.add(entity, Constants.Groups.SELECTED_PIECE);
    }

    public void removeSelection(Entity entity) {
        groupManager.remove(entity, Constants.Groups.SELECTED_PIECE);
    }

    public Entity createHighlightedCell(ModelCell cell) {
        Entity e = world.createEntity();

        e.addComponent(componentFactory.createHilightComponent(cell, Constants.Map.HIGHLIGHT_COLOR));

        groupManager.add(e, Constants.Groups.HIGHLIGHTED_CELLS);

        e.addToWorld();
        return e;
    }
}
