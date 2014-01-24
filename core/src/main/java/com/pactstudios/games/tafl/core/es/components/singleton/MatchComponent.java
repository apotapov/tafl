package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class MatchComponent implements Component {

    public TaflMatch match;
    public boolean animationInProgress;

    @Override
    public void reset() {
        match = null;
        animationInProgress = false;
    }
}
