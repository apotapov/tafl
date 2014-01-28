package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;

public class AiSystem extends EventProcessingSystem<AiTurnEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    @SuppressWarnings("unchecked")
    public AiSystem() {
        super(Aspect.getAspectForAll(MatchComponent.class), AiTurnEvent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
    }

    @Override
    protected void processEvent(Entity e, AiTurnEvent event) {
        MatchComponent component = matchMapper.get(e);
        TaflMatch match = component.match;
        Move move = match.aiStrategy.search(match, match.computerTeam);

        PieceMoveEvent moveEvent = world.createEvent(PieceMoveEvent.class);
        moveEvent.move.piece = move.piece;
        moveEvent.move.start = move.start;
        moveEvent.move.end = move.end;
        world.postEvent(this, moveEvent);
    }

}
