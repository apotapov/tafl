package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public interface AiStrategy {

    public Move search(TaflMatch match) throws InterruptedException;

}
