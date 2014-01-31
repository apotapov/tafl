package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.enums.Team;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;

public interface AiStrategy {

    public Move search(TaflMatch match, Team team);

}
