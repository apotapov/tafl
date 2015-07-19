package com.captstudios.games.tafl.core.es.model.ai;

import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public interface AiStrategy {

    public Move search(TaflMatch match);

}
