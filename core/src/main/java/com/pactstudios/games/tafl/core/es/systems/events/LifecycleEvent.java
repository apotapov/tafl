package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;

public class LifecycleEvent extends SystemEvent {
    public enum Lifecycle {
        PLAY,
        MENU,
        QUIT,
        RESTART,
        WIN,
        LOSS;
    }

    public Lifecycle lifecycle;

    @Override
    protected void resetForPooling() {
        lifecycle = null;
    }

}
