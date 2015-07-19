package com.captstudios.games.tafl.core.es.systems.input;

import com.artemis.Filter;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.es.components.singleton.RenderingComponent;
import com.captstudios.games.tafl.core.es.systems.events.InputEvent;
import com.captstudios.games.tafl.core.es.systems.render.RenderingSystem;

public abstract class InputProcessingSystem<T extends RenderingComponent> extends RenderingSystem<T> {

    protected Array<InputEvent> inputEvents;

    public InputProcessingSystem(Filter aspect, Class<T> type) {
        super(aspect, type);
        inputEvents = new Array<InputEvent>();
    }

    @Override
    protected void begin(T rendComponent) {
    }

    @Override
    protected void end(T rendComponent) {
    }

    @Override
    protected void process(Entity e, T rendComponent) {
        world.getEvents(this, InputEvent.class, inputEvents);

        for (InputEvent event : inputEvents) {
            if (!event.handled) {
                Vector2 gameTouchPoint = InputUtil.translateTouchPoint(
                        rendComponent.getCamera(), event.x, event.y);
                process(e, event, gameTouchPoint);
            }
        }
    }

    protected abstract void process(Entity e, InputEvent event, Vector2 gameTouchPoint);

}
