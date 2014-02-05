package com.pactstudios.games.tafl.core.level;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.RulesEngineType;
import com.roundtriangles.games.zaria.AbstractLevel;

public class TaflLevel extends AbstractLevel<TaflLevel>{
    public RulesEngineType rules;
    public String boardRepresentation;
    int turn = Constants.BoardConstants.NO_TEAM;

    @Override
    public void dispose() {
        rules = null;
    }
}
