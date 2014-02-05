package com.pactstudios.games.tafl.core.es.model.ai;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflBoard;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.ai.evaluators.BoardEvaluator;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.HistoryTable;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.RulesChecker;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.OpeningBook;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.search.AISearchAgent;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.search.AISearchAgentMTDF;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.TranspositionTable;

public class MtdfMoveStrategy implements AiStrategy {

    AISearchAgent<TaflMove, TaflBoard> agent;

    public MtdfMoveStrategy(
            TranspositionTable transpositionTable,
            HistoryTable<TaflMove> historyTable,
            BoardEvaluator<TaflBoard> evaluator,
            RulesChecker<TaflMove,
            TaflBoard> rulesChecker,
            OpeningBook<TaflMove> openings) {
        agent = new AISearchAgentMTDF<TaflMove, TaflBoard>(
                transpositionTable, historyTable, evaluator, rulesChecker, openings, Constants.AiConstants.MAX_TREE_DEPTH);
    }

    @Override
    public TaflMove search(TaflMatch match) {
        return agent.pickBestMove(match.board, match.turn);
    }
}
