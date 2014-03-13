package com.captstudios.games.tafl.core.es.systems.interaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.systems.event.EventProcessingSystem3;
import com.captstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.systems.events.AiCompleteEvent;
import com.captstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.captstudios.games.tafl.core.es.systems.events.HintEvent;
import com.captstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.captstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.captstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.captstudios.games.tafl.core.utils.AiThread;

public class AiSystem extends EventProcessingSystem3<AiTurnEvent, AiCompleteEvent, HintEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    ExecutorService executor;
    public AiThread aiThread;

    EntityFactorySystem efs;
    CellHighlightSystem cellHighlightSystem;

    String thinking;

    @SuppressWarnings("unchecked")
    public AiSystem(String thinking) {
        super(Filter.allComponents(MatchComponent.class), AiTurnEvent.class, AiCompleteEvent.class, HintEvent.class);

        this.thinking = thinking;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        efs = world.getSystem(EntityFactorySystem.class);
        cellHighlightSystem = world.getSystem(CellHighlightSystem.class);

        this.aiThread = new AiThread(world, this);
    }

    @Override
    protected void processEvent(Entity e, AiTurnEvent event) {
        MatchComponent component = matchMapper.get(e);

        aiThread.match = component.match;
        executor.execute(aiThread);

        efs.createAiProcessingPrompt(e, thinking);
    }

    @Override
    protected void processEvent2(Entity e, AiCompleteEvent event) {
        e.removeComponent(AiProcessingComponent.class);

        PieceMoveEvent moveEvent = world.createEvent(PieceMoveEvent.class);
        moveEvent.move = event.move.clone();
        world.postEvent(this, moveEvent);
    }

    @Override
    protected void processEvent3(Entity e, HintEvent event) {
        MatchComponent component = matchMapper.get(e);

        if (component.acceptInput()) {
            Move move = component.match.hintStrategy.search(component.match);
            if (move != null) {
                move = component.match.board.rules.generateLegalMoves(component.match.turn).random();
            }
            if (move != null) {
                cellHighlightSystem.clearCellHighlights();
                cellHighlightSystem.highlightCell(component.match, move.source);
                cellHighlightSystem.highlightCell(component.match, move.destination);
            }

            component.match.board.rules.generateLegalMoves(component.match.turn);
        }
    }
}
