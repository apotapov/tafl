/***************************************************************************
 * jcAISearchAgentMDTF - A sophisticated search agent
 **************************************************************************/
package com.pactstudios.games.tafl.core.es.model.ai.chess.search;

import com.pactstudios.games.tafl.core.es.model.ai.chess.Board;
import com.pactstudios.games.tafl.core.es.model.ai.chess.eval.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.chess.move.Move;

public class AISearchAgentMDTF extends AISearchAgent {

    // Construction
    public AISearchAgentMDTF() {
        evaluator = new BoardEvaluator();
    }

    // Move selection
    @Override
    public Move pickBestMove(Board theBoard) {
        // FDL Do the real work later
        return (new Move());
    }
}