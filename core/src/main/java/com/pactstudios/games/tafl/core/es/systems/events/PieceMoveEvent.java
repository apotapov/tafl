package com.pactstudios.games.tafl.core.es.systems.events;

import com.artemis.systems.event.SystemEvent;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public class PieceMoveEvent extends SystemEvent {
    public TaflMove move = new TaflMove(Constants.BoardConstants.BIGGEST_BOARD_NUMBER_CELLS);

    @Override
    protected void resetForPooling() {
        move.reset();
    }
}
