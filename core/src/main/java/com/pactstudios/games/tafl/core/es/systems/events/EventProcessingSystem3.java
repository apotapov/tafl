package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.utils.Array;

public abstract class EventProcessingSystem3<T extends SystemEvent, U extends SystemEvent, W extends SystemEvent>
extends EventProcessingSystem2<T, U> {

    Array<W> events3;
    Class<W> eventType3;

    public EventProcessingSystem3(Aspect aspect, Class<T> eventType, Class<U> eventType2, Class<W> eventType3) {
        super(aspect, eventType, eventType2);
        this.eventType3 = eventType3;
        events3 = new Array<W>();
    }

    @Override
    protected void processEntities(Array<Entity> entities) {
        super.processEntities(entities);

        world.getEvents(this, eventType3, events3);
        for (W event : events3) {
            for (Entity e : entities) {
                processEvent3(e, event);
            }
        }
    }

    protected abstract void processEvent3(Entity e, W event);
}
