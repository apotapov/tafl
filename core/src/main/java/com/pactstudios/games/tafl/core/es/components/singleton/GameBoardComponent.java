package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.board.RulesEngine;
import com.pactstudios.games.tafl.core.es.model.objects.Piece;

public class GameBoardComponent implements Component {

    public GameBoard board;
    public Piece selectedPiece;
    public RulesEngine rulesEngine;

    @Override
    public void reset() {
        board = null;
    }
}
