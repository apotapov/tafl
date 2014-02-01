package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;

public class PlayerWarningEvent extends SystemEvent {

    public PlayerWarningEnum playerWarning;

    @Override
    protected void resetForPooling() {
        playerWarning = null;
    }
}
