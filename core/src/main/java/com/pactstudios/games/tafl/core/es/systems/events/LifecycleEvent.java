package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

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
    public Team winner;

    @Override
    protected void resetForPooling() {
        lifecycle = null;
        winner = null;
    }

}
