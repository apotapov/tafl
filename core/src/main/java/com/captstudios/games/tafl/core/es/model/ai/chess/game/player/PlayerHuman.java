///**************************************************************************
// * jcPlayerHuman.java - Interface to a human player
// * by Fran�ois Dominic Laram�e
// *
// * Purpose: This object allows a human player to play JavaChess.  Its only
// * real job is to query the human player for his move.
// *
// * Note that this is not the cleanest, most user-friendly piece of code around;
// * it is only intended as a test harness for the AI player, not as a full-
// * fledged application (which would be graphical, for one thing!)
// *
// * History:
// * 11.06.00 Creation
// **************************************************************************/
//package com.captstudios.games.tafl.core.es.model.ai.chess.game.player;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import com.captstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.Move;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.MoveListGenerator;
//
//public class PlayerHuman extends Player {
//    // The keyboard
//    InputStreamReader kbd;
//    char linebuf[];
//
//    // Validation help
//    MoveListGenerator pseudos;
//    Board successor;
//
//    // Constructor
//    public PlayerHuman(int which, InputStreamReader syskbd) {
//        this.setSide(which);
//        linebuf = new char[10];
//        kbd = syskbd;
//        pseudos = new MoveListGenerator();
//        successor = new Board();
//    }
//
//    // public jcMove GetMove( theBoard )
//    // Getting a move from the human player. Sorry, but this is very, very
//    // primitive: you need to enter square numbers instead of piece ID's, and
//    // both square numbers must be entered with two digits. Ex.: 04 00
//    @Override
//    public Move getMove(Board theBoard) {
//        // Read the move from the command line
//        boolean ok = false;
//        Move move = new Move();
//        do {
//            System.out.println("Your move, " + PlayerStrings[this.getSide()]
//                    + "?");
//
//            // Get data from the command line
//            int len = 0;
//            do {
//                try {
//                    len = kbd.read(linebuf, 0, 5);
//                } catch (IOException e) {
//                }
//            } while (len < 3);
//
//            String line = new String(linebuf, 0, 5);
//
//            if (line.equalsIgnoreCase("RESIG")) {
//                move.moveType = Move.MOVE_RESIGN;
//                return (move);
//            }
//
//            // Extract the source and destination squares from the line buffer
//            move.sourceSquare = Integer.parseInt(line.substring(0, 2));
//            move.destinationSquare = Integer.parseInt(line.substring(3, 5));
//            if ((move.sourceSquare < 0) || (move.sourceSquare > 63)) {
//                System.out.println("Sorry, illegal source square "
//                        + move.sourceSquare);
//                continue;
//            }
//            if ((move.destinationSquare < 0) || (move.destinationSquare > 63)) {
//                System.out.println("Sorry, illegal destination square "
//                        + move.destinationSquare);
//                continue;
//            }
//
//            // Time to try to figure out what the move means!
//            if (theBoard.getCurrentPlayer() == Player.SIDE_WHITE) {
//                // Is there a piece (of the moving player) on SourceSquare?
//                // If not, abort
//                move.movingPiece = theBoard.findWhitePiece(move.sourceSquare);
//                if (move.movingPiece == Board.EMPTY_SQUARE) {
//                    System.out
//                    .println("Sorry, You don't have a piece at square "
//                            + move.sourceSquare);
//                    continue;
//                }
//
//                // Three cases: there is a piece on the destination square (a
//                // capture),
//                // the destination square allows an en passant capture, or it is
//                // a
//                // simple non-capture move. If the destination contains a piece
//                // of the
//                // moving side, abort
//                if (theBoard.findWhitePiece(move.destinationSquare) != Board.EMPTY_SQUARE) {
//                    System.out.println("Sorry, can't capture your own piece!");
//                    continue;
//                }
//                move.capturedPiece = theBoard
//                        .findBlackPiece(move.destinationSquare);
//                if (move.capturedPiece != Board.EMPTY_SQUARE) {
//                    move.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                } else if ((theBoard.getEnPassantPawn() == (1 << move.destinationSquare))
//                        && (move.movingPiece == Board.WHITE_PAWN)) {
//                    move.capturedPiece = Board.BLACK_PAWN;
//                    move.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                }
//
//                // If the move isn't a capture, it may be a castling attempt
//                else if ((move.movingPiece == Board.WHITE_KING)
//                        && ((move.sourceSquare - move.destinationSquare) == 2)) {
//                    move.moveType = Move.MOVE_CASTLING_KINGSIDE;
//                } else if ((move.movingPiece == Board.WHITE_KING)
//                        && ((move.sourceSquare - move.destinationSquare) == -2)) {
//                    move.moveType = Move.MOVE_CASTLING_QUEENSIDE;
//                } else {
//                    move.moveType = Move.MOVE_NORMAL;
//                }
//            } else {
//                move.movingPiece = theBoard.findBlackPiece(move.sourceSquare);
//                if (move.movingPiece == Board.EMPTY_SQUARE) {
//                    System.out
//                    .println("Sorry, you don't have a piece in square "
//                            + move.sourceSquare);
//                    continue;
//                }
//
//                if (theBoard.findBlackPiece(move.destinationSquare) != Board.EMPTY_SQUARE) {
//                    System.out
//                    .println("Sorry, you can't capture your own piece in square "
//                            + move.destinationSquare);
//                    continue;
//                }
//                move.capturedPiece = theBoard
//                        .findWhitePiece(move.destinationSquare);
//                if (move.capturedPiece != Board.EMPTY_SQUARE) {
//                    move.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                } else if ((theBoard.getEnPassantPawn() == (1 << move.destinationSquare))
//                        && (move.movingPiece == Board.BLACK_PAWN)) {
//                    move.capturedPiece = Board.WHITE_PAWN;
//                    move.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                } else if ((move.movingPiece == Board.BLACK_KING)
//                        && ((move.sourceSquare - move.destinationSquare) == 2)) {
//                    move.moveType = Move.MOVE_CASTLING_KINGSIDE;
//                } else if ((move.movingPiece == Board.BLACK_KING)
//                        && ((move.sourceSquare - move.destinationSquare) == -2)) {
//                    move.moveType = Move.MOVE_CASTLING_QUEENSIDE;
//                } else {
//                    move.moveType = Move.MOVE_NORMAL;
//                }
//            }
//
//            // Now, if the move results in a pawn promotion, we must ask the
//            // user
//            // for the type of promotion!
//            if (((move.movingPiece == Board.WHITE_PAWN) && (move.destinationSquare < 8))
//                    || ((move.movingPiece == Board.BLACK_PAWN) && (move.destinationSquare > 55))) {
//                int car = -1;
//                System.out
//                .println("Promote the pawn to [K]night, [R]ook, [B]ishop, [Q]ueen?");
//                do {
//                    try {
//                        car = kbd.read();
//                    } catch (IOException e) {
//                    }
//                } while ((car != 'K') && (car != 'k') && (car != 'b')
//                        && (car != 'B') && (car != 'R') && (car != 'r')
//                        && (car != 'Q') && (car != 'q'));
//                if ((car == 'K') || (car == 'k')) {
//                    move.moveType += Move.MOVE_PROMOTION_KNIGHT;
//                } else if ((car == 'B') || (car == 'b')) {
//                    move.moveType += Move.MOVE_PROMOTION_BISHOP;
//                } else if ((car == 'R') || (car == 'r')) {
//                    move.moveType += Move.MOVE_PROMOTION_ROOK;
//                } else {
//                    move.moveType += Move.MOVE_PROMOTION_QUEEN;
//                }
//            }
//
//            // OK, now let's see if the move is actually legal! First step: a
//            // check
//            // for pseudo-legality, i.e., is it a valid successor to the current
//            // board?
//            pseudos.computeLegalMoves(theBoard);
//            if (!pseudos.find(move)) {
//                System.out
//                .print("Sorry, this move is not in the pseudo-legal list: ");
//                move.print();
//                pseudos.print();
//                continue;
//            }
//
//            // If pseudo-legal, then verify whether it leaves the king in check
//            successor.clone(theBoard);
//            successor.applyMove(move);
//            if (!pseudos.computeLegalMoves(successor)) {
//                System.out
//                .print("Sorry, this move leaves your king in check: ");
//                move.print();
//                continue;
//            }
//
//            // If we have made it here, we have a valid move to play!
//            System.out.println("Move is accepted...");
//            ok = true;
//
//        } while (!ok);
//
//        return (move);
//    }
//}