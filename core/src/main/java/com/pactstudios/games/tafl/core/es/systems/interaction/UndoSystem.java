package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.UndoEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;

public class UndoSystem extends EventProcessingSystem<UndoEvent> {

    ComponentMapper<MatchComponent> matchMapper;
    ComponentMapper<PositionComponent> positionMapper;

    EntityFactorySystem efs;
    CellHighlightSystem highlightSystem;

    @SuppressWarnings("unchecked")
    public UndoSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class), UndoEvent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        positionMapper = world.getMapper(PositionComponent.class);
        efs = world.getSystem(EntityFactorySystem.class);
        highlightSystem = world.getSystem(CellHighlightSystem.class);
    }

    @Override
    protected void processEvent(Entity e, UndoEvent event) {
        MatchComponent component = matchMapper.get(e);

        if (component.match.versusComputer && component.match.board.undoStack.size > 1) {
            component.match.undoMove();
            component.match.undoMove();
        } else {
            component.match.undoMove();
        }
    }
}
