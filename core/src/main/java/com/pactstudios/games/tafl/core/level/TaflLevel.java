package com.pactstudios.games.tafl.core.level;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.board.RulesEngine;
import com.pactstudios.games.tafl.core.es.model.objects.Piece;
import com.pactstudios.games.tafl.core.utils.log.GameLog;
import com.roundtriangles.games.zaria.AbstractLevel;

public class TaflLevel extends AbstractLevel<TaflLevel>{

    public GameBoard board;
    public GameLog log;
    public int dimensions;

    public Array<Piece> pieces;
    public RulesEngine rulesEngine;

    @Override
    public void dispose() {
        board = null;
        log = null;
        dimensions = 0;
        pieces = null;
    }

    public void reset() {
        board.reset();
        log.reset();
    }
}
