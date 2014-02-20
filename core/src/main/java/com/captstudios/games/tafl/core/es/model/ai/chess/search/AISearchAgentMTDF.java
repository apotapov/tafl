///***************************************************************************
// * jcAISearchAgentMTDF - A sophisticated search agent
// *
// * Purpose: A (mostly) state-of-the-art search agent, implementing advanced
// * techniques like the iterative-deepening MTDF search algorithm, transposition
// * table, opening book and history table.
// *
// * History:
// * 05.10.00 Completed initial version
// **************************************************************************/
//
//package com.captstudios.games.tafl.core.es.model.ai.chess.search;
//
//import com.captstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.Move;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.MoveListGenerator;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.OpeningBook;
//
//public class AISearchAgentMTDF extends AISearchAgent {
//    // A reference to the game's opening book
//    private OpeningBook openings;
//
//    // A measure of the effort we are willing to expend on search
//    private static final int MAX_SEARCH_SIZE = 50000;
//
//    // Construction
//    public AISearchAgentMTDF(OpeningBook ref) {
//        super();
//        openings = ref;
//    }
//
//    /****************************************************************************
//     * PUBLIC METHODS
//     ***************************************************************************/
//
//    // Move selection: An iterative-deepening paradigm calling MTD(f) repeatedly
//    @Override
//    public Move pickBestMove(Board theBoard) {
//        // First things first: look in the Opening Book, and if it contains a
//        // move for this position, don't search anything
//        moveCounter++;
//        Move move = null;
//        move = openings.query(theBoard);
//        if (move != null) {
//            return move;
//        }
//
//        // Store the identity of the moving side, so that we can tell Evaluator
//        // from whose perspective we need to evaluate positions
//        fromWhosePerspective = theBoard.getCurrentPlayer();
//
//        // Should we erase the history table?
//        if ((random.nextInt() % 6) == 2) {
//            historyTable.forget();
//        }
//
//        // Begin search. The search's maximum depth is determined on the fly,
//        // according to how much effort has been spent; if it's possible to
//        // search
//        // to depth 8 in 5 seconds, then by all means, do it!
//        int bestGuess = 0;
//        int iterdepth = 1;
//
//        while (true) {
//            // Searching to depth 1 is not very effective, so we begin at 2
//            iterdepth++;
//
//            // Compute efficiency statistics
//            numRegularNodes = 0;
//            numQuiescenceNodes = 0;
//            numRegularTTHits = 0;
//            numQuiescenceTTHits = 0;
//            numRegularCutoffs = 0;
//            numQuiescenceCutoffs = 0;
//
//            // Look for a move at the current depth
//            move = MTDF(theBoard, bestGuess, iterdepth);
//            bestGuess = move.moveEvaluation;
//
//            // Feedback!
//            System.out.print("Iteration of depth " + iterdepth
//                    + "; best move = ");
//            move.print();
//            System.out
//            .print("  --> Transposition Table hits for regular nodes: ");
//            System.out.println(numRegularTTHits + " of " + numRegularNodes);
//            System.out
//            .print("  --> Transposition Table hits for quiescence nodes: ");
//            System.out.println(numQuiescenceTTHits + " of "
//                    + numQuiescenceNodes);
//            System.out.println("  --> Number of cutoffs for regular nodes: "
//                    + numRegularCutoffs);
//            System.out.println("  --> Number of cutoffs in quiescence search: "
//                    + numQuiescenceCutoffs);
//
//            // Get out if we have searched deep enough
//            if ((numRegularNodes + numQuiescenceNodes) > MAX_SEARCH_SIZE) {
//                break;
//            }
//            if (iterdepth >= 15) {
//                break;
//            }
//        }
//
//        return move;
//    }
//
//    /***************************************************************************
//     * PRIVATE METHODS
//     **************************************************************************/
//
//    // private jcMove MTDF
//    // Use the MTDF algorithm to find a good move. MTDF repeatedly calls
//    // alphabeta with a zero-width search window, which creates very many quick
//    // cutoffs. If alphabeta fails low, the next call will place the search
//    // window lower; in a sense, MTDF is a sort of binary search mechanism into
//    // the minimax space.
//    private Move MTDF(Board theBoard, int target, int depth) {
//        int beta;
//        Move Mov;
//        int currentEstimate = target;
//        int upperbound = ALPHABETA_MAXVAL;
//        int lowerbound = ALPHABETA_MINVAL;
//
//        // This is the trick: make repeated calls to alphabeta, zeroing in on
//        // the
//        // actual minimax value of theBoard by narrowing the bounds
//        do {
//            if (currentEstimate == lowerbound) {
//                beta = currentEstimate + 1;
//            } else {
//                beta = currentEstimate;
//            }
//
//            Mov = unrolledAlphabeta(theBoard, depth, beta - 1, beta);
//            currentEstimate = Mov.moveEvaluation;
//
//            if (currentEstimate < beta) {
//                upperbound = currentEstimate;
//            } else {
//                lowerbound = currentEstimate;
//            }
//
//        } while (lowerbound < upperbound);
//
//        return Mov;
//    }
//
//    // private jcMove UnrolledAlphabeta
//    // The standard alphabeta, with the top level "unrolled" so that it can
//    // return a jcMove structure instead of a mere minimax value
//    // See jcAISearchAgent.Alphabeta for detailed comments on this code
//    private Move unrolledAlphabeta(Board theBoard, int depth, int alpha,
//            int beta) {
//        Move bestMove = new Move();
//
//        MoveListGenerator movegen = new MoveListGenerator();
//        movegen.computeLegalMoves(theBoard);
//        historyTable.sortMoveList(movegen, theBoard.getCurrentPlayer());
//
//        Board newBoard = new Board();
//        int bestSoFar;
//
//        bestSoFar = ALPHABETA_MINVAL;
//        int currentAlpha = alpha;
//        Move mov;
//
//        // Loop on the successors
//        while ((mov = movegen.next()) != null) {
//            // Compute a board position resulting from the current successor
//            newBoard.clone(theBoard);
//            newBoard.applyMove(mov);
//
//            // And search it in turn
//            int movScore = alphaBeta(MINNODE, newBoard, depth - 1,
//                    currentAlpha, beta);
//
//            // Ignore illegal moves in the alphabeta evaluation
//            if (movScore == ALPHABETA_ILLEGAL) {
//                continue;
//            }
//            currentAlpha = Math.max(currentAlpha, movScore);
//
//            // Is the current successor better than the previous best?
//            if (movScore > bestSoFar) {
//                bestMove.copy(mov);
//                bestSoFar = movScore;
//                bestMove.moveEvaluation = bestSoFar;
//
//                // Can we cutoff now?
//                if (bestSoFar >= beta) {
//                    transTable.storeBoard(theBoard, bestSoFar,
//                            Move.EVALTYPE_UPPERBOUND, depth, moveCounter);
//
//                    // Add this move's efficiency in the HistoryTable
//                    historyTable.addCount(theBoard.getCurrentPlayer(), mov);
//                    return bestMove;
//                }
//            }
//        }
//
//        // Test for checkmate or stalemate
//        if (bestSoFar <= ALPHABETA_GIVEUP) {
//            newBoard.clone(theBoard);
//            MoveListGenerator secondary = new MoveListGenerator();
//            newBoard.switchSides();
//            if (secondary.computeLegalMoves(newBoard)) {
//                // Then, we are not in check and may continue our efforts.
//                historyTable.sortMoveList(movegen, newBoard.getCurrentPlayer());
//                movegen.resetIterator();
//                bestMove.moveType = Move.MOVE_STALEMATE;
//                bestMove.movingPiece = Board.KING
//                        + theBoard.getCurrentPlayer();
//                while ((mov = movegen.next()) != null) {
//                    newBoard.clone(theBoard);
//                    newBoard.applyMove(mov);
//                    if (secondary.computeLegalMoves(newBoard)) {
//                        bestMove.moveType = Move.MOVE_RESIGN;
//                    }
//                }
//            } else {
//                // We're in check and our best hope is GIVEUP or worse, so
//                // either we are
//                // already checkmated or will be soon, without hope of escape
//                bestMove.movingPiece = Board.KING
//                        + theBoard.getCurrentPlayer();
//                bestMove.moveType = Move.MOVE_RESIGN;
//            }
//        }
//
//        // If we haven't returned yet, we have found an accurate minimax score
//        // for a position which is neither a checkmate nor a stalemate
//        transTable.storeBoard(theBoard, bestSoFar, Move.EVALTYPE_ACCURATE,
//                depth, moveCounter);
//
//        return bestMove;
//    }
//}