package com.pactstudios.games.tafl.core.es.systems.interaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.systems.events.AiCompleteEvent;
import com.pactstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem2;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.AiThread;
import com.roundtriangles.games.zaria.services.resources.LocaleService;

public class AiSystem extends EventProcessingSystem2<AiTurnEvent, AiCompleteEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    LocaleService localeService;
    ExecutorService executor;

    EntityFactorySystem efs;

    public AiThread aiThread;

    @SuppressWarnings("unchecked")
    public AiSystem(LocaleService localeService) {
        super(Aspect.getAspectForAll(MatchComponent.class), AiTurnEvent.class, AiCompleteEvent.class);

        this.localeService = localeService;
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

        String text = localeService.get(LocalizedStrings.Game.AI_PROCESSING);
        efs.createAiProcessingPrompt(e, text);
    }

    @Override
    protected void processEvent2(Entity e, AiCompleteEvent event) {
        e.removeComponent(AiProcessingComponent.class);

        PieceMoveEvent moveEvent = world.createEvent(PieceMoveEvent.class);
        moveEvent.move.pieceType = event.move.pieceType;
        moveEvent.move.source = event.move.source;
        moveEvent.move.destination = event.move.destination;
        world.postEvent(this, moveEvent);
    }

    public void stopThread() {
        aiThread.interrupt();
    }

    @SuppressWarnings("deprecation")
    public void pauseThread() {
        aiThread.suspend();
    }

    @SuppressWarnings("deprecation")
    public void resumeThread() {
        aiThread.resume();
    }

}
