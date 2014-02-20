package com.captstudios.games.tafl.core.es.systems.input;

import com.artemis.Filter;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.captstudios.games.tafl.core.es.systems.events.InputEvent;

public class HudInputSystem extends InputProcessingSystem<HudRenderingComponent> {

    @SuppressWarnings("unchecked")
    public HudInputSystem() {
        super(Filter.allComponents(HudRenderingComponent.class), HudRenderingComponent.class);
    }

    @Override
    protected void process(Entity e, HudRenderingComponent rendComponent) {
        inputEvents.clear();
        world.getEvents(this, InputEvent.class, inputEvents);

        for (InputEvent event : inputEvents) {
            if (!event.handled) {
                switch (event.type) {
                case TOUCH_UP:
                    event.handled = rendComponent.hubStage.touchUp(event.x, event.y, event.pointer, event.button);
                    break;
                case TOUCH_DOWN:
                    event.handled = rendComponent.hubStage.touchDown(event.x, event.y, event.pointer, event.button);
                    break;
                case TOUCH_DRAG:
                    event.handled = rendComponent.hubStage.touchDragged(event.x, event.y, event.pointer);
                    break;
                }
            }
        }
    }

    @Override
    protected void process(Entity e, InputEvent event, Vector2 gameTouchPoint) {
    }
}
