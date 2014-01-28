package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public interface BoardEvaluator {

    public int evaluate(TaflMatch match, Team team);

}
