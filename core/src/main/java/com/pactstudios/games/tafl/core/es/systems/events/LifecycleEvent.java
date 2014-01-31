package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.enums.Lifecycle;
import com.pactstudios.games.tafl.core.enums.Team;

public class LifecycleEvent extends SystemEvent {
    public Lifecycle lifecycle;
    public Team winner;

    @Override
    protected void resetForPooling() {
        lifecycle = null;
        winner = null;
    }

}
