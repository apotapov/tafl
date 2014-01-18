package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.utils.Array;

public abstract class EventProcessingSystem2<T extends SystemEvent, U extends SystemEvent> extends EventProcessingSystem<T> {

    Array<U> events2;
    Class<U> eventType2;

    public EventProcessingSystem2(Aspect aspect, Class<T> eventType, Class<U> eventType2) {
        super(aspect, eventType);
        this.eventType2 = eventType2;
        events2 = new Array<U>();
    }

    @Override
    protected void processEntities(Array<Entity> entities) {
        super.processEntities(entities);

        world.getEvents(this, eventType2, events2);
        for (U event : events2) {
            for (Entity e : entities) {
                processEvent2(e, event);
            }
        }
    }

    protected abstract void processEvent2(Entity e, U event);
}
