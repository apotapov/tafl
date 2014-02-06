package com.pactstudios.games.tafl.core.es.systems.passive;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.components.render.HighlightComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class CellHighlightSystem extends PassiveEntitySystem implements TaflMatchObserver {

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

    private void highlightTeam(int team) {
        clearCellHighlights();
        MatchComponent matchComponent = singletonManager.getSingletonComponent(MatchComponent.class);
        TaflMatch match = matchComponent.match;

        BitBoard pieces = match.board.bitBoards[team];
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            if (match.rulesEngine.getLegalMoves(match.turn, i).cardinality() > 0) {
                highlightCell(matchComponent.match, i);
            }
        }
    }

    public void clearCellHighlights() {
        Array<Entity> highlights = groupManager.getEntities(Constants.GroupConstants.HIGHLIGHTED_CELLS);
        for (Entity highlight : highlights) {
            highlight.deleteFromWorld();
        }
    }

    public void highlightCell(TaflMatch match, int cellId) {
        if (match.board.corners.get(cellId)) {
            efs.createHighlightedCell(cellId, Constants.BoardRenderConstants.CORNER_HIGHLIGHT_COLOR);
        } else {
            efs.createHighlightedCell(cellId, Constants.BoardRenderConstants.HIGHLIGHT_COLOR);
        }
    }

    public void highlightCells(TaflMatch match, BitBoard cells) {
        for (int i = cells.nextSetBit(0); i >= 0; i = cells.nextSetBit(i+1)) {
            highlightCell(match, i);
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

    @Override
    public void initializeMatch(TaflMatch match) {
    }

    @Override
    public void applyMove(TaflMatch match, TaflMove move) {
    }

    @Override
    public void undoMove(TaflMatch match, TaflMove move) {
    }

    @Override
    public void addPiece(TaflMatch match, int team, int pieces) {
    }

    @Override
    public void removePieces(TaflMatch match, int team, BitBoard capturedPieces) {
    }

    @Override
    public void changeTurn(TaflMatch match) {
        highlightTeam(match.turn);
    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
    }
}
