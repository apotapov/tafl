package com.pactstudios.games.tafl.core.es.systems.passive;

import java.util.BitSet;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.render.HighlightComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;

public class CellHighlightSystem extends PassiveEntitySystem {

    ComponentMapper<HighlightComponent> highlightMapper;

    GroupManager groupManager;
    SingletonComponentManager singletonManager;

    EntityFactorySystem efs;

    @Override
    public void initialize() {
        super.initialize();
        highlightMapper = world.getMapper(HighlightComponent.class);
        groupManager = world.getManager(GroupManager.class);
        singletonManager = world.getManager(SingletonComponentManager.class);
        efs = world.getSystem(EntityFactorySystem.class);
    }

    public void highlightTeam(int team) {
        clearCellHighlights();
        MatchComponent matchComponent = singletonManager.getSingletonComponent(MatchComponent.class);

        BitSet pieces = matchComponent.match.board.bitBoards[team];
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            if (matchComponent.match.rulesEngine.legalMoves(i).cardinality() > 0) {
                highlightCell(i);
            }
        }
    }

    public void clearCellHighlights() {
        Array<Entity> highlights = groupManager.getEntities(Constants.GroupConstants.HIGHLIGHTED_CELLS);
        for (Entity highlight : highlights) {
            highlight.deleteFromWorld();
        }
    }

    public void highlightCell(int cellId) {
        efs.createHighlightedCell(cellId);
    }

    public void highlightCells(BitSet cells) {
        for (int i = cells.nextSetBit(0); i >= 0; i = cells.nextSetBit(i+1)) {
            highlightCell(i);
        }
    }

    public void clearCellHighlights(int cellId) {
        Array<Entity> highlights = groupManager.getEntities(Constants.GroupConstants.HIGHLIGHTED_CELLS);
        for (Entity e : highlights) {
            HighlightComponent c = highlightMapper.get(e);
            if (c.cellId == cellId) {
                e.deleteFromWorld();
            }
        }
    }
}
