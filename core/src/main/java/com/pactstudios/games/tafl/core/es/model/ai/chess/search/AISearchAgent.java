///**************************************************************************
// * jcAISearchAgent - An object which picks a best move according to a
// *                   variant of alphabeta search or another
// *
// * Purpose:
// * This is the object which picks a move for the computer player.  Implemented
// * as an abstract class to allow multiple search strategies to be played with.
// *
// * History
// * 07.08.00 Creation
// * 05.10.00 Added statistics and some corrections
// *************************************************************************/
//
//package com.pactstudios.games.tafl.core.es.model.ai.chess.search;
//
//import java.util.Random;
//
//import com.pactstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.eval.BoardEvaluator;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.eval.TranspositionTable;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.move.HistoryTable;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.move.Move;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.move.MoveListGenerator;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.move.OpeningBook;
//
//public abstract class AISearchAgent {
//    /***************************************************************************
//     * DATA MEMBERS
//     **************************************************************************/
//
//    // A transposition table for this object
//    TranspositionTable transTable;
//
//    // A handle to the system's history table
//    HistoryTable historyTable;
//
//    // How will we assess position strengths?
//    protected BoardEvaluator evaluator;
//    protected int fromWhosePerspective;
//
//    // ID's for concrete subclasses; jcAISearchAgent works as a factory for its
//    // concrete subclasses
//    public static final int AISEARCH_ALPHABETA = 0;
//    public static final int AISEARCH_MTDF = 1;
//
//    // Search node types: MAXNODEs are nodes where the computer player is the
//    // one to move; MINNODEs are positions where the opponent is to move.
//    protected static final boolean MAXNODE = true;
//    protected static final boolean MINNODE = false;
//
//    // Alphabeta search boundaries
//    protected static final int ALPHABETA_MAXVAL = 30000;
//    protected static final int ALPHABETA_MINVAL = -30000;
//    protected static final int ALPHABETA_ILLEGAL = -31000;
//
//    // An approximate upper bound on the total value of all positional
//    // terms in the evaluation function
//    protected static final int EVAL_THRESHOLD = 200;
//
//    // A score below which we give up: if Alphabeta ever returns a value lower
//    // than this threshold, then all is lost and we might as well resign. Here,
//    // the value is equivalent to "mated by the opponent in 3 moves or less".
//    protected static final int ALPHABETA_GIVEUP = -29995;
//
//    Random random;
//
//    // Statistics
//    int numRegularNodes;
//    int numQuiescenceNodes;
//    int numRegularTTHits;
//    int numQuiescenceTTHits;
//    int numRegularCutoffs;
//    int numQuiescenceCutoffs;
//
//    // A move counter, so that the agent knows when it can delete old stuff from
//    // its transposition table
//    int moveCounter;
//
//    /***************************************************************************
//     * PUBLIC METHODS
//     **************************************************************************/
//
//    // Construction
//    public AISearchAgent() {
//        transTable = new TranspositionTable();
//        historyTable = HistoryTable.getInstance();
//        evaluator = new BoardEvaluator();
//        random = new Random();
//        moveCounter = 0;
//    }
//
//    public AISearchAgent(BoardEvaluator eval) {
//        attachEvaluator(eval);
//    }
//
//    // boolean AttachEvaluator( jcBoardEvaluator eval )
//    // Pick a function which the agent will use to assess the potency of a
//    // position. This may change during the game; for example, a special
//    // "mop-up" evaluator may replace the standard when it comes time to drive
//    // a decisive advantage home at the end of the game.
//    public boolean attachEvaluator(BoardEvaluator eval) {
//        evaluator = eval;
//        return true;
//    }
//
//    // int AlphaBeta
//    // The basic alpha-beta algorithm, used in one disguise or another by
//    // every search agent class
//    public int alphaBeta(boolean nodeType, Board theBoard, int depth,
//            int alpha, int beta) {
//        Move mov = new Move();
//
//        // Count the number of nodes visited in the full-width search
//        numRegularNodes++;
//
//        // First things first: let's see if there is already something useful
//        // in the transposition table, which might save us from having to search
//        // anything at all
//        if (transTable.lookupBoard(theBoard, mov) && (mov.searchDepth >= depth)) {
//            if (nodeType == MAXNODE) {
//                if ((mov.moveEvaluationType == Move.EVALTYPE_ACCURATE)
//                        || (mov.moveEvaluationType == Move.EVALTYPE_LOWERBOUND)) {
//                    if (mov.moveEvaluation >= beta) {
//                        numRegularTTHits++;
//                        return mov.moveEvaluation;
//                    }
//                }
//            } else {
//                if ((mov.moveEvaluationType == Move.EVALTYPE_ACCURATE)
//                        || (mov.moveEvaluationType == Move.EVALTYPE_UPPERBOUND)) {
//                    if (mov.moveEvaluation <= alpha) {
//                        numRegularTTHits++;
//                        return mov.moveEvaluation;
//                    }
//                }
//            }
//        }
//
//        // If we have reached the maximum depth of the search, stop recursion
//        // and begin quiescence search
//        if (depth == 0) {
//            return quiescenceSearch(nodeType, theBoard, alpha, beta);
//        }
//
//        // Otherwise, generate successors and search them in turn
//        // If ComputeLegalMoves returns false, then the current position is
//        // illegal
//        // because one or more moves could capture a king!
//        // In order to slant the computer's strategy in favor of quick mates, we
//        // give a bonus to king captures which occur at shallow depths, i.e.,
//        // the
//        // more plies left, the better. On the other hand, if you are losing, it
//        // really doesn't matter how fast...
//        MoveListGenerator movegen = new MoveListGenerator();
//        if (!movegen.computeLegalMoves(theBoard)) {
//            return ALPHABETA_ILLEGAL;
//        }
//
//        // Sort the moves according to History heuristic values
//        historyTable.sortMoveList(movegen, theBoard.getCurrentPlayer());
//
//        // OK, now, get ready to search
//        Board newBoard = new Board();
//        int bestSoFar;
//
//        // Case #1: We are searching a Max Node
//        if (nodeType == AISearchAgent.MAXNODE) {
//            bestSoFar = ALPHABETA_MINVAL;
//            int currentAlpha = alpha;
//
//            // Loop on the successors
//            while ((mov = movegen.next()) != null) {
//                // Compute a board position resulting from the current successor
//                newBoard.clone(theBoard);
//                newBoard.applyMove(mov);
//
//                // And search it in turn
//                int movScore = alphaBeta(!nodeType, newBoard, depth - 1,
//                        currentAlpha, beta);
//                // Ignore illegal moves in the alphabeta evaluation
//                if (movScore == ALPHABETA_ILLEGAL) {
//                    continue;
//                }
//
//                currentAlpha = Math.max(currentAlpha, movScore);
//
//                // Is the current successor better than the previous best?
//                if (movScore > bestSoFar) {
//                    bestSoFar = movScore;
//                    // Can we cutoff now?
//                    if (bestSoFar >= beta) {
//                        // Store this best move in the TransTable
//                        transTable.storeBoard(theBoard, bestSoFar,
//                                Move.EVALTYPE_UPPERBOUND, depth, moveCounter);
//
//                        // Add this move's efficiency in the HistoryTable
//                        historyTable.addCount(theBoard.getCurrentPlayer(), mov);
//                        numRegularCutoffs++;
//                        return bestSoFar;
//                    }
//                }
//            }
//
//            // Test for checkmate or stalemate
//            // Both cases occur if and only if there is no legal move for MAX,
//            // i.e.,
//            // if "bestSoFar" is ALPHABETA_MINVAL. There are two cases: we
//            // have checkmate (in which case the score is accurate) or stalemate
//            // (in
//            // which case the position should be re-scored as a draw with value
//            // 0.
//            if (bestSoFar <= ALPHABETA_MINVAL) {
//                // Can MIN capture MAX's king? First, ask the machine to
//                // generate
//                // moves for MIN
//                newBoard.clone(theBoard);
//                if (newBoard.getCurrentPlayer() == fromWhosePerspective) {
//                    newBoard.switchSides();
//                }
//
//                // And if one of MIN's moves is a king capture, indicating that
//                // the
//                // position is illegal, we have checkmate and must return
//                // MINVAL. We
//                // add the depth simply to "favor" delaying tactics: a mate in 5
//                // will
//                // score higher than a mate in 3, because the likelihood that
//                // the
//                // opponent will miss it is higher; might as well make life
//                // difficult!
//                if (!movegen.computeLegalMoves(newBoard)) {
//                    return bestSoFar + depth;
//                } else {
//                    return 0;
//                }
//            }
//        } else
//            // Case #2: Min Node
//        {
//            bestSoFar = ALPHABETA_MAXVAL;
//            int currentBeta = beta;
//            while ((mov = movegen.next()) != null) {
//                newBoard.clone(theBoard);
//                newBoard.applyMove(mov);
//
//                int movScore = alphaBeta(!nodeType, newBoard, depth - 1, alpha,
//                        currentBeta);
//                if (movScore == ALPHABETA_ILLEGAL) {
//                    continue;
//                }
//                currentBeta = Math.min(currentBeta, movScore);
//                if (movScore < bestSoFar) {
//                    bestSoFar = movScore;
//                    // Cutoff?
//                    if (bestSoFar <= alpha) {
//                        transTable.storeBoard(theBoard, bestSoFar,
//                                Move.EVALTYPE_UPPERBOUND, depth, moveCounter);
//                        historyTable.addCount(theBoard.getCurrentPlayer(), mov);
//                        numRegularCutoffs++;
//                        return bestSoFar;
//                    }
//                }
//            }
//            // Test for checkmate or stalemate
//            if (bestSoFar >= ALPHABETA_MAXVAL) {
//                // Can MAX capture MIN's king?
//                newBoard.clone(theBoard);
//                if (newBoard.getCurrentPlayer() != fromWhosePerspective) {
//                    newBoard.switchSides();
//                }
//                if (!movegen.computeLegalMoves(newBoard)) {
//                    return bestSoFar + depth;
//                } else {
//                    return 0;
//                }
//            }
//        }
//
//        // If we haven't returned yet, we have found an accurate minimax score
//        // for a position which is neither a checkmate nor a stalemate
//        transTable.storeBoard(theBoard, bestSoFar, Move.EVALTYPE_ACCURATE,
//                depth, moveCounter);
//        return bestSoFar;
//    }
//
//    // int QuiescenceSearch
//    // A slight variant of alphabeta which only considers captures and null
//    // moves
//    // This is necesary because the evaluation function can only be applied to
//    // "quiet" positions where the tactical situation (i.e., material balance)
//    // is
//    // unlikely to change in the near future.
//    // Note that, in this version of the code, the quiescence search is not
//    // limited
//    // by depth; we continue digging for as long as we can find captures. Some
//    // other
//    // programs impose a depth limit for time-management purposes.
//    public int quiescenceSearch(boolean nodeType, Board theBoard, int alpha,
//            int beta) {
//        Move mov = new Move();
//        numQuiescenceNodes++;
//
//        // First things first: let's see if there is already something useful
//        // in the transposition table, which might save us from having to search
//        // anything at all
//        if (transTable.lookupBoard(theBoard, mov)) {
//            if (nodeType == MAXNODE) {
//                if ((mov.moveEvaluationType == Move.EVALTYPE_ACCURATE)
//                        || (mov.moveEvaluationType == Move.EVALTYPE_LOWERBOUND)) {
//                    if (mov.moveEvaluation >= beta) {
//                        numQuiescenceTTHits++;
//                        return mov.moveEvaluation;
//                    }
//                }
//            } else {
//                if ((mov.moveEvaluationType == Move.EVALTYPE_ACCURATE)
//                        || (mov.moveEvaluationType == Move.EVALTYPE_UPPERBOUND)) {
//                    if (mov.moveEvaluation <= alpha) {
//                        numQuiescenceTTHits++;
//                        return mov.moveEvaluation;
//                    }
//                }
//            }
//        }
//
//        int bestSoFar = ALPHABETA_MINVAL;
//
//        // Start with evaluation of the null-move, just to see whether it is
//        // more
//        // effective than any capture, in which case we must stop looking at
//        // captures and damaging our position
//        // NOTE: If the quick evaluation is enough to cause a cutoff, we don't
//        // store
//        // the value in the transposition table. EvaluateQuickie is so fast that
//        // we
//        // wouldn't gain anything, and storing the value might force us to erase
//        // a
//        // more valuable entry in the table.
//        bestSoFar = evaluator.evaluateQuickie(theBoard, fromWhosePerspective);
//        if ((bestSoFar > (beta + EVAL_THRESHOLD))
//                || (bestSoFar < (alpha - EVAL_THRESHOLD))) {
//            return bestSoFar;
//        } else {
//            bestSoFar = evaluator.evaluateComplete(theBoard,
//                    fromWhosePerspective);
//        }
//
//        // Now, look at captures
//        MoveListGenerator movegen = new MoveListGenerator();
//        if (!movegen.computeQuiescenceMoves(theBoard)) {
//            return bestSoFar;
//        }
//
//        Board newBoard = new Board();
//
//        // Case #1: We are searching a Max Node
//        if (nodeType == AISearchAgent.MAXNODE) {
//            int currentAlpha = alpha;
//            // Loop on the successors
//            while ((mov = movegen.next()) != null) {
//                // Compute a board position resulting from the current successor
//                newBoard.clone(theBoard);
//                newBoard.applyMove(mov);
//
//                // And search it in turn
//                int movScore = quiescenceSearch(!nodeType, newBoard,
//                        currentAlpha, beta);
//                // Ignore illegal moves in the alphabeta evaluation
//                if (movScore == ALPHABETA_ILLEGAL) {
//                    continue;
//                }
//                currentAlpha = Math.max(currentAlpha, movScore);
//
//                // Is the current successor better than the previous best?
//                if (movScore > bestSoFar) {
//                    bestSoFar = movScore;
//                    // Can we cutoff now?
//                    if (bestSoFar >= beta) {
//                        transTable.storeBoard(theBoard, bestSoFar,
//                                Move.EVALTYPE_UPPERBOUND, 0, moveCounter);
//                        // Add this move's efficiency in the HistoryTable
//                        historyTable.addCount(theBoard.getCurrentPlayer(), mov);
//                        numQuiescenceCutoffs++;
//                        return bestSoFar;
//                    }
//                }
//            }
//
//            // Test for checkmate or stalemate
//            // Both cases occur if and only if there is no legal move for MAX,
//            // i.e.,
//            // if "bestSoFar" is ALPHABETA_MINVAL. There are two cases: we
//            // have checkmate (in which case the score is accurate) or stalemate
//            // (in
//            // which case the position should be re-scored as a draw with value
//            // 0.
//            if (bestSoFar <= ALPHABETA_MINVAL) {
//                // Can MIN capture MAX's king? First, ask the machine to
//                // generate
//                // moves for MIN
//                newBoard.clone(theBoard);
//                if (newBoard.getCurrentPlayer() == fromWhosePerspective) {
//                    newBoard.switchSides();
//                }
//                // And if one of MIN's moves is a king capture, indicating that
//                // the
//                // position is illegal, we have checkmate and must return
//                // MINVAL. We
//                // add the depth simply to "favor" delaying tactics: a mate in 5
//                // will
//                // score higher than a mate in 3, because the likelihood that
//                // the
//                // opponent will miss it is higher; might as well make life
//                // difficult!
//                if (!movegen.computeLegalMoves(newBoard)) {
//                    return bestSoFar;
//                } else {
//                    return 0;
//                }
//            }
//        } else
//            // Case #2: Min Node
//        {
//            int currentBeta = beta;
//            while ((mov = movegen.next()) != null) {
//                newBoard.clone(theBoard);
//                newBoard.applyMove(mov);
//
//                int movScore = quiescenceSearch(!nodeType, newBoard, alpha,
//                        currentBeta);
//                if (movScore == ALPHABETA_ILLEGAL) {
//                    continue;
//                }
//                currentBeta = Math.min(currentBeta, movScore);
//                if (movScore < bestSoFar) {
//                    bestSoFar = movScore;
//                    // Cutoff?
//                    if (bestSoFar <= alpha) {
//                        transTable.storeBoard(theBoard, bestSoFar,
//                                Move.EVALTYPE_UPPERBOUND, 0, moveCounter);
//                        historyTable.addCount(theBoard.getCurrentPlayer(), mov);
//                        numQuiescenceCutoffs++;
//                        return bestSoFar;
//                    }
//                }
//            }
//            // Test for checkmate or stalemate
//            if (bestSoFar >= ALPHABETA_MAXVAL) {
//                // Can MAX capture MIN's king?
//                newBoard.clone(theBoard);
//                if (newBoard.getCurrentPlayer() != fromWhosePerspective) {
//                    newBoard.switchSides();
//                }
//                if (!movegen.computeLegalMoves(newBoard)) {
//                    return bestSoFar;
//                } else {
//                    return 0;
//                }
//            }
//        }
//
//        // If we haven't returned yet, we have found an accurate minimax score
//        // for a position which is neither a checkmate nor a stalemate
//        transTable.storeBoard(theBoard, bestSoFar, Move.EVALTYPE_ACCURATE, 0,
//                moveCounter);
//        return bestSoFar;
//    }
//
//    // jcAISearchAgent MakeNewAgent
//    // Standard "subclass factory" design pattern
//    public static AISearchAgent makeNewAgent(int type, OpeningBook ref) {
//        switch (type) {
//        case AISEARCH_ALPHABETA:
//            return (new AISearchAgentAlphabeta());
//        case AISEARCH_MTDF:
//            return (new AISearchAgentMTDF(ref));
//        default:
//            return null;
//        }
//    }
//
//    // jcMove PickBestMove( jcBoard theBoard )
//    // Each agent class needs some way of picking a move!
//    public abstract Move pickBestMove(Board theBoard);
//}