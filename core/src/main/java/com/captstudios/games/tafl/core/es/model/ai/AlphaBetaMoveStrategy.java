package com.captstudios.games.tafl.core.es.model.ai;

import com.captstudios.games.tafl.core.es.model.TaflBoard;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;
import com.captstudios.games.tafl.core.es.model.ai.optimization.search.AISearchAgent;
import com.captstudios.games.tafl.core.es.model.ai.optimization.search.AISearchAgentAlphabeta;
import com.captstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;

public class AlphaBetaMoveStrategy implements AiStrategy {

    AISearchAgent<TaflBoard> agent;

    public AlphaBetaMoveStrategy(
            TranspositionTable transpositionTable,
            HistoryTable historyTable,
            BoardEvaluator<TaflBoard> evaluator,
            RulesChecker rulesChecker,
            int depth) {
        agent = new AISearchAgentAlphabeta<TaflBoard>(
                transpositionTable, historyTable, evaluator, rulesChecker, depth);
    }

    @Override
    public Move search(TaflMatch match) {
        return agent.pickBestMove(match.board, match.turn);
    }
}
