package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.search.AISearchAgent;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.search.AISearchAgentAlphabeta;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;

public class AlphaBetaMoveStrategy implements AiStrategy {

    AISearchAgent<TaflMove, TaflBoard> agent;

    public AlphaBetaMoveStrategy(
            TranspositionTable transpositionTable,
            HistoryTable<TaflMove> historyTable,
            BoardEvaluator<TaflBoard> evaluator,
            RulesChecker<TaflMove,
            TaflBoard> rulesChecker,
            int depth) {
        agent = new AISearchAgentAlphabeta<TaflMove, TaflBoard>(
                transpositionTable, historyTable, evaluator, rulesChecker, depth);
    }

    @Override
    public TaflMove search(TaflMatch match) throws InterruptedException {
        return agent.pickBestMove(match.board, match.turn);
    }
}
