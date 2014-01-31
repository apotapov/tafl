package com.pactstudios.games.tafl.core.level;

import com.pactstudios.games.tafl.core.es.model.board.GameBoardType;
import com.pactstudios.games.tafl.core.es.model.rules.RulesEngine.RulesEngineType;
import com.roundtriangles.games.zaria.AbstractLevel;

public class TaflLevel extends AbstractLevel<TaflLevel>{
    public GameBoardType boardType;
    public RulesEngineType rules;
    public String whitePieces;
    public String blackPieces;
    public int king;

    @Override
    public void dispose() {
        boardType = null;
        rules = null;
    }
}
