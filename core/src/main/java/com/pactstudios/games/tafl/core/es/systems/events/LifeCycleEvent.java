package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.Team;

public class LifeCycleEvent extends SystemEvent {
    public LifeCycle lifecycle;
    public Team winner;
    public DrawReasonEnum drawReason;

    @Override
    protected void resetForPooling() {
        lifecycle = null;
        winner = null;
        drawReason = null;
    }

}
