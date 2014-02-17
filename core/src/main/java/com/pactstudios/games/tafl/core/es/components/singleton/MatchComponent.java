package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class MatchComponent implements Component {

    public TaflMatch match;
    public boolean animationInProgress;

    public float dragging;
    public int draggedPiece;

    public boolean acceptInput() {
        return !animationInProgress && match.acceptInput();
    }

    @Override
    public void reset() {
        match = null;
        animationInProgress = false;

        dragging = 0;
        draggedPiece = Constants.BoardConstants.ILLEGAL_CELL;
    }
}
