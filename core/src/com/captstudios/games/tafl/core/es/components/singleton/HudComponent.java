package com.captstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.captstudios.games.tafl.core.es.model.TaflMatch;

public class HudComponent implements Component {
    public TaflMatch match;

    public int fps;

    @Override
    public void reset() {
        match = null;
        fps = 0;
    }

}
