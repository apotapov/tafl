package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public interface AiStrategy {

    public enum AiType {
        NONE,
        RANDOM;
    }

    public Move search(TaflMatch match, Team team);

}
