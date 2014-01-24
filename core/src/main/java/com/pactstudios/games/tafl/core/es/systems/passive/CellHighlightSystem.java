package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;

public class CellHighlightSystem extends PassiveEntitySystem {

    protected GroupManager groupManager;
    protected EntityFactorySystem efs;

    @Override
    public void initialize() {
        super.initialize();
        groupManager = world.getManager(GroupManager.class);
        efs = world.getSystem(EntityFactorySystem.class);
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
}
