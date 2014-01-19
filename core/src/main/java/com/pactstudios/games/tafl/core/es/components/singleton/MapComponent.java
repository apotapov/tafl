package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.pactstudios.games.tafl.core.es.model.map.TaflMap;
import com.pactstudios.games.tafl.core.es.model.map.objects.Team;

public class MapComponent implements Component {

    public TaflMap map;
    public Team turn;

    @Override
    public void reset() {
        map = null;
    }

    @Override
    public String toString() {
        return "Map";
    }

    public void changeTurn() {
        if (turn == Team.BLACK) {
            turn = Team.WHITE;
        } else {
            turn = Team.BLACK;
        }
    }
}
