///****************************************************************************
// * jcMove.java - An encapsulation of a chess move and its consequences
// * by Fran�ois Dominic Laram�e
// *
// * Purpose: This class is used all over the place.  It contains a move's
// * source and target squares, a type identifier (i.e., a normal move, a pawn
// * promotion, etc.) and a score, whether an actual evaluation of the position
// * which would result from the move or a value taken from the history table.
// *
// * History
// * 11.06.00 Creation
// * 09.07.00 Added fields MovingPiece and CapturedPiece; while not absolutely
// *          needed, they do accelerate move processing and help to make code
// *          easier to understand, so I gladly keep them around as optimizations
// * 14.08.00 Added "search depth" field, so that we can determine whether a
// *          transposition table entry should be used or not.
// ***************************************************************************/
//package com.captstudios.games.tafl.core.es.model.ai.chess.move;
//
//import com.captstudios.games.tafl.core.es.model.ai.chess.Board;
//
//public class Move {
//    /************************************************************************
//     * CONSTANTS
//     ***********************************************************************/
//
//    // The different types of moves recognized by the game
//    public static final int MOVE_NORMAL = 0;
//    public static final int MOVE_CAPTURE_ORDINARY = 1;
//    public static final int MOVE_CAPTURE_EN_PASSANT = 2;
//    public static final int MOVE_CASTLING_KINGSIDE = 4;
//    public static final int MOVE_CASTLING_QUEENSIDE = 8;
//    public static final int MOVE_RESIGN = 16;
//    public static final int MOVE_STALEMATE = 17;
//    public static final int MOVE_PROMOTION_KNIGHT = 32;
//    public static final int MOVE_PROMOTION_BISHOP = 64;
//    public static final int MOVE_PROMOTION_ROOK = 128;
//    public static final int MOVE_PROMOTION_QUEEN = 256;
//
//    // A pair of masks used to split the promotion and the non-promotion part of
//    // a move type ID
//    public static final int PROMOTION_MASK = 480;
//    public static final int NO_PROMOTION_MASK = 31;
//
//    // Alphabeta may return an actual move potency evaluation, or an upper or
//    // lower bound only (in case a cutoff happens). We need to store this
//    // information in the transposition table to make sure that a given
//    // value is actually useful in given circumstances.
//    public static final int EVALTYPE_ACCURATE = 0;
//    public static final int EVALTYPE_UPPERBOUND = 1;
//    public static final int EVALTYPE_LOWERBOUND = 2;
//
//    // A sentinel value used to identify jcMove fields without valid data
//    public static final int NULL_MOVE = -1;
//
//    /************************************************************************
//     * DATA MEMBERS Note: this class is intended as a C++ structure, so all data
//     * members have public access.
//     ***********************************************************************/
//
//    // The moving piece; one of the constants defined by jcBoard
//    public int movingPiece;
//
//    // The piece being captured by this move, if any; another jcBoard constant
//    public int capturedPiece;
//
//    // The squares involved in the move
//    public int sourceSquare, destinationSquare;
//
//    // A type ID: is this a regular move, a capture, a capture AND promotion
//    // from
//    // Pawn to Rook, etc. Move generation determines this, by definition;
//    // storing
//    // it here avoids having to "re-discover" the information in
//    // jcBoard.ApplyMove
//    // at the cost of a few bytes
//    public int moveType;
//
//    // An evaluation of the move's potency, either as a result of an alphabeta
//    // search of some kind or of a retrieval in the transposition table
//    public int moveEvaluation;
//    public int moveEvaluationType;
//    public int searchDepth;
//
//    /*************************************************************************
//     * PUBLIC METHODS
//     *************************************************************************/
//
//    public Move() {
//        this.reset();
//    }
//
//    public void copy(Move target) {
//        movingPiece = target.movingPiece;
//        capturedPiece = target.capturedPiece;
//        sourceSquare = target.sourceSquare;
//        destinationSquare = target.destinationSquare;
//        moveType = target.moveType;
//        moveEvaluation = target.moveEvaluation;
//        moveEvaluationType = target.moveEvaluationType;
//        searchDepth = target.searchDepth;
//    }
//
//    // public boolean Equals( jcMove target )
//    // Check whether two jcMove objects contain the same data (not necessarily
//    // whether they are the same object in memory)
//    public boolean equals(Move target) {
//        if (movingPiece != target.movingPiece) {
//            return false;
//        }
//        if (capturedPiece != target.capturedPiece) {
//            return false;
//        }
//        if (moveType != target.moveType) {
//            return false;
//        }
//        if (sourceSquare != target.sourceSquare) {
//            return false;
//        }
//        if (destinationSquare != target.destinationSquare) {
//            return false;
//        }
//        return true;
//    }
//
//    public boolean reset() {
//        movingPiece = Board.EMPTY_SQUARE;
//        capturedPiece = Board.EMPTY_SQUARE;
//        sourceSquare = NULL_MOVE;
//        destinationSquare = NULL_MOVE;
//        moveType = NULL_MOVE;
//        moveEvaluation = NULL_MOVE;
//        moveEvaluationType = NULL_MOVE;
//        searchDepth = NULL_MOVE;
//        return true;
//    }
//
//    public void print() {
//        System.out.print("Move: ");
//        if (moveType == MOVE_STALEMATE) {
//            System.out.println("STALEMATE!!!");
//        }
//        if (moveType != MOVE_RESIGN) {
//            System.out.print(Board.pieceStrings[movingPiece]);
//            System.out.print("  [ ");
//            System.out.print(sourceSquare);
//            System.out.print(", ");
//            System.out.print(destinationSquare);
//            System.out.print(" ] TYPE: ");
//            System.out.println(moveType);
//        } else {
//            System.out.println("RESIGNATION!");
//        }
//    }
//}