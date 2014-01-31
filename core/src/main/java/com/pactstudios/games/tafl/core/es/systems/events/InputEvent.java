package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.enums.InputType;

public class InputEvent extends SystemEvent {

    public InputType type;
    public int x;
    public int y;
    public int pointer;
    public int button;

    @Override
    protected void resetForPooling() {
        type = null;
        x = 0;
        y = 0;
        pointer = -1;
        button = 0;
    }
}
