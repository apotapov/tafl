package com.pactstudios.games.tafl.core.es.systems.interaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.systems.events.AiCompleteEvent;
import com.pactstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem2;
import com.pactstudios.games.tafl.core.es.systems.events.PieceMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.roundtriangles.games.zaria.services.resources.LocaleService;

public class AiSystem extends EventProcessingSystem2<AiTurnEvent, AiCompleteEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    LocaleService localeService;
    ExecutorService executor;

    EntityFactorySystem efs;

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
    }

    @Override
    protected void processEvent(Entity e, AiTurnEvent event) {
        MatchComponent component = matchMapper.get(e);
        final TaflMatch match = component.match;

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Move move = match.aiStrategy.search(match, match.computerTeam);
                AiCompleteEvent completeEvent = world.createEvent(AiCompleteEvent.class);
                completeEvent.move = move;
                world.postEvent(AiSystem.this, completeEvent);
            }
        });

        String text = localeService.get(LocalizedStrings.Game.AI_PROCESSING);
        efs.createAiProcessingPrompt(e, text);
    }

    @Override
    protected void processEvent2(Entity e, AiCompleteEvent event) {
        e.removeComponent(AiProcessingComponent.class);

        PieceMoveEvent moveEvent = world.createEvent(PieceMoveEvent.class);
        moveEvent.move.piece = event.move.piece;
        moveEvent.move.start = event.move.start;
        moveEvent.move.end = event.move.end;
        world.postEvent(this, moveEvent);
    }

}
