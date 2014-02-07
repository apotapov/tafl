package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMove;

public interface AiStrategy {

    public TaflMove search(TaflMatch match) throws InterruptedException;

}
