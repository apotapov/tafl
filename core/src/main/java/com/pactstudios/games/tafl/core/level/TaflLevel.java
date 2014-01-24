package com.pactstudios.games.tafl.core.level;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.model.rules.RulesEngine.RulesEngineType;
import com.roundtriangles.games.zaria.AbstractLevel;

public class TaflLevel extends AbstractLevel<TaflLevel>{
    public int dimensions;
    public RulesEngineType rules;
    public Array<GamePiece> pieces;

    @Override
    public void dispose() {
        dimensions = 0;
        pieces = null;
    }
}
