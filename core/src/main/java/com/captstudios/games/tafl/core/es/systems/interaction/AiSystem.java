package com.captstudios.games.tafl.core.es.systems.interaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.systems.event.EventProcessingSystem2;
import com.captstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.systems.events.AiCompleteEvent;
import com.captstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.captstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.captstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.captstudios.games.tafl.core.utils.AiThread;

public class AiSystem extends EventProcessingSystem2<AiTurnEvent, AiCompleteEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    ExecutorService executor;
    public AiThread aiThread;

    EntityFactorySystem efs;

    String thinking;

    @SuppressWarnings("unchecked")
    public AiSystem(String thinking) {
        super(Filter.allComponents(MatchComponent.class), AiTurnEvent.class, AiCompleteEvent.class);

        this.thinking = thinking;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        efs = world.getSystem(EntityFactorySystem.class);

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
}
