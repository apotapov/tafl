package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntitySystem;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.utils.Array;

public abstract class EventProcessingSystem<T extends SystemEvent> extends EntitySystem {

    Array<T> events;
    Class<T> eventType;

    public EventProcessingSystem(Aspect aspect, Class<T> eventType) {
        super(aspect);
        this.eventType = eventType;
        events = new Array<T>();
    }

    @Override
    protected void processEntities(Array<Entity> entities) {
        world.getEvents(this, eventType, events);
        for (T event : events) {
            for (Entity e : entities) {
                processEvent(e, event);
            }
        }
    }

    protected abstract void processEvent(Entity e, T event);
}
