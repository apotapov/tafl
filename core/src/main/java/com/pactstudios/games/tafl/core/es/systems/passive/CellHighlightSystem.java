package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.interaction.PieceComponent;
import com.pactstudios.games.tafl.core.es.components.render.HighlightComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class CellHighlightSystem extends PassiveEntitySystem {

    ComponentMapper<HighlightComponent> highlightMapper;
    ComponentMapper<PieceComponent> pieceMapper;

    GroupManager groupManager;
    SingletonComponentManager singletonManager;

    EntityFactorySystem efs;

    @Override
    public void initialize() {
        super.initialize();
        highlightMapper = world.getMapper(HighlightComponent.class);
        pieceMapper = world.getMapper(PieceComponent.class);
        groupManager = world.getManager(GroupManager.class);
        singletonManager = world.getManager(SingletonComponentManager.class);
        efs = world.getSystem(EntityFactorySystem.class);
    }

    public void highlightTeam(Team team) {
        MatchComponent matchComponent = singletonManager.getSingletonComponent(MatchComponent.class);
        Array<Entity> pieces = groupManager.getEntities(team.toString());
        for (Entity e : pieces) {
            PieceComponent piece = pieceMapper.get(e);
            ModelCell cell = matchComponent.match.board.getCell(piece.piece.x, piece.piece.y);
            if (cell != null && matchComponent.match.rulesEngine.legalMoves(cell).size > 0) {
                highlightCell(cell);
            }
        }
    }

    public void clearCellHighlights() {
        Array<Entity> highlights = groupManager.getEntities(Constants.GroupConstants.HIGHLIGHTED_CELLS);
        for (Entity highlight : highlights) {
            highlight.deleteFromWorld();
        }
    }

    public void highlightCell(ModelCell cell) {
        efs.createHighlightedCell(cell);
    }

    public void highlightCells(Array<ModelCell> cells) {
        for (ModelCell cell : cells) {
            highlightCell(cell);
        }
    }

    public void clearCellHighlights(ModelCell cell) {
        Array<Entity> highlights = groupManager.getEntities(Constants.GroupConstants.HIGHLIGHTED_CELLS);
        for (Entity e : highlights) {
            HighlightComponent c = highlightMapper.get(e);
            if (c.cell == cell) {
                e.deleteFromWorld();
            }
        }
    }
}
