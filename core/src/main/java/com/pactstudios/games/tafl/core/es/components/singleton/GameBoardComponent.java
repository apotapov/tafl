package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.board.RulesEngine;

public class GameBoardComponent implements Component {

    public GameBoard board;
    public RulesEngine rulesEngine;

    @Override
    public void reset() {
        board = null;
    }
}
