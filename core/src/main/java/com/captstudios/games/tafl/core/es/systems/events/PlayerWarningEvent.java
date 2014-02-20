package com.captstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.captstudios.games.tafl.core.enums.PlayerWarningEnum;

public class PlayerWarningEvent extends SystemEvent {

    public PlayerWarningEnum playerWarning;

    @Override
    protected void resetForPooling() {
        playerWarning = null;
    }
}
