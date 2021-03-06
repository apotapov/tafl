package com.captstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.DrawReasonEnum;
import com.captstudios.games.tafl.core.enums.LifeCycle;

public class LifeCycleEvent extends SystemEvent {
    public LifeCycle lifecycle;
    public int winner;
    public DrawReasonEnum drawReason;

    @Override
    protected void resetForPooling() {
        lifecycle = null;
        winner = Constants.BoardConstants.NO_TEAM;
        drawReason = null;
    }

}
