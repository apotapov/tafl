package com.pactstudios.games.tafl.core.level;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.map.TaflMap;
import com.pactstudios.games.tafl.core.es.model.map.objects.Piece;
import com.roundtriangles.games.zaria.AbstractLevel;

public class TaflLevel extends AbstractLevel<TaflLevel>{

    public TaflMap map;
    public String file;

    public Array<Piece> pieces;

    @Override
    public void dispose() {
        map = null;
    }
}
