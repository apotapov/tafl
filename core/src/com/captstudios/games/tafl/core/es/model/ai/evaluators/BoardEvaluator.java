package com.captstudios.games.tafl.core.es.model.ai.evaluators;

import com.captstudios.games.tafl.core.es.model.ai.optimization.GameBoard;

public interface BoardEvaluator<T extends GameBoard> {

    public int evaluate(T board, int turn);
}
