///***************************************************************************
// * jcAISearchAgentAlphabeta - The most basic search agent
// *
// * Purpose: Pick a best move using a simple, fixed-depth, full-width
// * alphabeta search, like they did in the Dark Ages ;-)
// *
// * History
// * 07.08.00 Initial writing
// **************************************************************************/
//
//package com.captstudios.games.tafl.core.es.model.ai.chess.search;
//
//import com.captstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.Move;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.MoveListGenerator;
//
//public class AISearchAgentAlphabeta extends AISearchAgent {
//    // Construction
//    public AISearchAgentAlphabeta() {
//        super();
//    }
//
//    // jcMove PickBestMove
//    // Implementation of the abstract method defined in the superclass
//    @Override
//    public Move pickBestMove(Board theBoard) {
//        // Store the identity of the moving side, so that we can tell Evaluator
//        // from whose perspective we need to evaluate positions
//        fromWhosePerspective = theBoard.getCurrentPlayer();
//        moveCounter++;
//
//        // Should we erase the history table?
//        if ((random.nextInt() % 4) == 2) {
//            historyTable.forget();
//        }
//
//        numRegularNodes = 0;
//        numQuiescenceNodes = 0;
//        numRegularTTHits = 0;
//        numQuiescenceTTHits = 0;
//
//        // Find the moves
//        Move theMove = new Move();
//        MoveListGenerator movegen = new MoveListGenerator();
//        movegen.computeLegalMoves(theBoard);
//        historyTable.sortMoveList(movegen, theBoard.getCurrentPlayer());
//
//        // The following code blocks look a lot like the MAX node case from
//        // jcAISearchAgent.Alphabeta, with an added twist: we need to store the
//        // actual best move, and not only pass around its minimax value
//        int bestSoFar = ALPHABETA_MINVAL;
//        Board newBoard = new Board();
//        Move mov;
//        int currentAlpha = ALPHABETA_MINVAL;
//
//        // Loop on all pseudo-legal moves
//        while ((mov = movegen.next()) != null) {
//            newBoard.clone(theBoard);
//            newBoard.applyMove(mov);
//            int movScore = alphaBeta(MINNODE, newBoard, 5, currentAlpha,
//                    ALPHABETA_MAXVAL);
//            if (movScore == ALPHABETA_ILLEGAL) {
//                continue;
//            }
//
//            currentAlpha = Math.max(currentAlpha, movScore);
//
//            if (movScore > bestSoFar) {
//                theMove.copy(mov);
//                bestSoFar = movScore;
//                theMove.moveEvaluation = movScore;
//            }
//        }
//
//        // And now, if the best we can do is ALPHABETA_GIVEUP or worse, then it
//        // is
//        // time to resign... Unless the opponent was kind wnough to put us in
//        // stalemate!
//        if (bestSoFar <= ALPHABETA_GIVEUP) {
//            // Check for a stalemate
//            // Stalemate occurs if the player's king is NOT in check, but all of
//            // his
//            // moves are illegal.
//            // First, verify whether we are in check
//            newBoard.clone(theBoard);
//            MoveListGenerator secondary = new MoveListGenerator();
//            newBoard.switchSides();
//            if (secondary.computeLegalMoves(newBoard)) {
//                // Then, we are not in check and may continue our efforts.
//                // We must now examine all possible moves; if at least one
//                // resuls in
//                // a legal position, there is no stalemate and we must assume
//                // that
//                // we are doomed
//                historyTable.sortMoveList(movegen, newBoard.getCurrentPlayer());
//                movegen.resetIterator();
//                // If we can scan all moves without finding one which results
//                // in a legal position, we have a stalemate
//                theMove.moveType = Move.MOVE_STALEMATE;
//                theMove.movingPiece = Board.KING
//                        + theBoard.getCurrentPlayer();
//                // Look at the moves
//                while ((mov = movegen.next()) != null) {
//                    newBoard.clone(theBoard);
//                    newBoard.applyMove(mov);
//                    if (secondary.computeLegalMoves(newBoard)) {
//                        theMove.moveType = Move.MOVE_RESIGN;
//                    }
//                }
//            } else {
//                // We're in check and our best hope is GIVEUP or worse, so
//                // either we are
//                // already checkmated or will be soon, without hope of escape
//                theMove.movingPiece = Board.KING
//                        + theBoard.getCurrentPlayer();
//                theMove.moveType = Move.MOVE_RESIGN;
//            }
//        }
//
//        System.out.print("  --> Transposition Table hits for regular nodes: ");
//        System.out.println(numRegularTTHits + " of " + numRegularNodes);
//        System.out
//        .print("  --> Transposition Table hits for quiescence nodes: ");
//        System.out.println(numQuiescenceTTHits + " of " + numQuiescenceNodes);
//
//        return theMove;
//    }
//}