///****************************************************************************
// * jcOpeningBook - A hash table of well-known positions and moves
// *
// * Chess programs are notoriously bad at deciding what to do with complicated
// * positions, so everyone "cheats" by giving them a library of opening positions
// * taken from the ECO or something like that.  This one is very primitive and
// * contains very little, but it gets the job done.
// *
// * History:
// * 19.09.00 Creation
// *
// ****************************************************************************/
//
//package com.pactstudios.games.tafl.core.es.model.ai.chess.move;
//
//import java.io.FileReader;
//import java.io.StreamTokenizer;
//
//import com.pactstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.game.player.Player;
//
///****************************************************************************
// * PRIVATE class jcOpeningBookEntry A signature for a board position, and the
// * best moves for White and Black in that position.
// ***************************************************************************/
//
//class OpeningBookEntry {
//    // A signature for the board position stored in the entry
//    int theLock;
//
//    // Moves
//    Move whiteMove;
//    Move blackMove;
//
//    // A sentinel indicating that a move is invalid
//    public static final int NO_MOVE = -1;
//
//    // Construction
//    OpeningBookEntry() {
//        theLock = 0;
//        whiteMove = new Move();
//        whiteMove.moveType = NO_MOVE;
//        blackMove = new Move();
//        blackMove.moveType = NO_MOVE;
//    }
//}
//
///*****************************************************************************
// * PUBLIC class jcOpeningBook A hash table containing a certain number of slots
// * for well-known positions
// ****************************************************************************/
//
//public class OpeningBook {
//    // The hash table itself
//    private static final int TABLE_SIZE = 1024;
//    private OpeningBookEntry table[];
//
//    // Construction
//    public OpeningBook() {
//        table = new OpeningBookEntry[TABLE_SIZE];
//        for (int i = 0; i < TABLE_SIZE; i++) {
//            table[i] = new OpeningBookEntry();
//        }
//    }
//
//    // public jcMove Query
//    // Querying the table for a ready-made move to play. Return null if there
//    // is none
//    public Move query(Board theBoard) {
//        // First, look for a match in the table
//        int key = Math.abs(theBoard.hashKey() % TABLE_SIZE);
//        int lock = theBoard.hashLock();
//
//        // If the hash lock doesn't match the one for our position, get out
//        if (table[key].theLock != lock) {
//            return null;
//        }
//
//        // If there is an entry for this board in the table, verify that it
//        // contains a move for the current side
//        if (theBoard.getCurrentPlayer() == Player.SIDE_BLACK) {
//            if (table[key].blackMove.moveType != OpeningBookEntry.NO_MOVE) {
//                return table[key].blackMove;
//            }
//        } else {
//            if (table[key].whiteMove.moveType != OpeningBookEntry.NO_MOVE) {
//                return table[key].whiteMove;
//            }
//        }
//
//        // If we haven't found anything useful, quit
//        return null;
//    }
//
//    // Loading the table from a file
//    public boolean load(String fileName) throws Exception {
//        // Open the file as a Java tokenizer
//        FileReader fr = new FileReader(fileName);
//        StreamTokenizer tok = new StreamTokenizer(fr);
//        tok.eolIsSignificant(false);
//        tok.lowerCaseMode(false);
//
//        // Create a game board on which to "play" the opening sequences stored
//        // in
//        // the book, so that we know which position to associate with which move
//        Board board = new Board();
//        Move mov = new Move();
//        MoveListGenerator successors = new MoveListGenerator();
//
//        // How many lines of play do we have in the book?
//        tok.nextToken();
//        int numLines = (int) tok.nval;
//
//        for (int wak = 0; wak < numLines; wak++) {
//            // Begin the line of play with a clean board
//            board.startingBoard();
//
//            // Load the continuation
//            while (true) {
//                successors.computeLegalMoves(board);
//
//                // Is the token an end-of-continuation marker?
//                // If so, go on to the next continuation
//                if ((tok.nextToken() == StreamTokenizer.TT_WORD)
//                        && (tok.sval.equalsIgnoreCase("END"))) {
//                    break;
//                }
//
//                if (tok.ttype == StreamTokenizer.TT_EOL) {
//                    tok.nextToken();
//                }
//
//                // If not, gather the source and destination squares of the next
//                // move
//                int source = (int) tok.nval;
//                tok.nextToken();
//                int destination = (int) tok.nval;
//
//                // Make a jcMove structure out of the source and destination
//                // squares;
//                // this determines whether there is a capture involved, a
//                // castling, etc.
//                mov = successors.findMoveForSquares(source, destination);
//
//                // And now, store the move in the table
//                storeMove(board, mov);
//
//                // Finally, apply the move and get ready for the next one
//                board.applyMove(mov);
//
//            }
//        }
//
//        fr.close();
//        return true;
//    }
//
//    // private StoreMove( jcBoard, jcMov )
//    private boolean storeMove(Board theBoard, Move theMove) {
//        // Where should we store this data?
//        int key = Math.abs(theBoard.hashKey() % TABLE_SIZE);
//        int lock = theBoard.hashLock();
//
//        // Is there already an entry for a different board position where we
//        // want to put this? If so, mark it deleted
//        if (table[key].theLock != lock) {
//            table[key].blackMove.moveType = OpeningBookEntry.NO_MOVE;
//            table[key].whiteMove.moveType = OpeningBookEntry.NO_MOVE;
//        }
//
//        // And store the new move
//        table[key].theLock = lock;
//        if (theBoard.getCurrentPlayer() == Player.SIDE_BLACK) {
//            table[key].blackMove.copy(theMove);
//        } else {
//            table[key].whiteMove.copy(theMove);
//        }
//
//        return true;
//    }
//}