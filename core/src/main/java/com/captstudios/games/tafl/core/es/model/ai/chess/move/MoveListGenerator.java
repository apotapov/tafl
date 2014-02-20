///******************************************************************************
// * jcMoveListGenerator.java - Find all pseudo-legal moves given a board state
// * by F.D. Laram�e
// *
// * Purpose: Identify a list of possible moves
// *
// * History:
// * 27.07.00 Creation
// *****************************************************************************/
//
//package com.captstudios.games.tafl.core.es.model.ai.chess.move;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import com.captstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.captstudios.games.tafl.core.es.model.ai.chess.game.player.Player;
//
//public class MoveListGenerator {
//
//    /**************************************************************************
//     * INSTANCE VARIABLES
//     *************************************************************************/
//
//    // The list of moves, implemented as a java collection class, namely the
//    // ArrayList (dynamic array)
//    ArrayList<Move> moves;
//    Iterator<Move> movesIt;
//
//    /**************************************************************************
//     * PUBLIC METHODS
//     *************************************************************************/
//
//    // Construction
//    public MoveListGenerator() {
//        moves = new ArrayList<Move>(10);
//        movesIt = null;
//        resetIterator();
//    }
//
//    // public void ResetIterator
//    // Prepare an iterator for scanning through the list of moves
//    public void resetIterator() {
//        // Mark the old iterator, if any, for garbage collection
//        if (movesIt != null) {
//            movesIt = null;
//        }
//
//        // Make a new iterator ready for scanning
//        movesIt = moves.iterator();
//    }
//
//    // Accessors
//    public ArrayList<Move> getMoveList() {
//        return moves;
//    }
//
//    public int getSize() {
//        return moves.size();
//    }
//
//    // public boolean Find( jcMove mov )
//    // Look for a specific move in the list; if it is there, return true
//    // This is used by the jcPlayerHuman object, to verify whether a move
//    // entered
//    // by the player is actually valid
//    public boolean find(Move mov) {
//        resetIterator();
//        Move testMove;
//        while ((testMove = next()) != null) {
//            if (mov.equals(testMove)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // public jcMove FindMoveForSquares( int source, int dest )
//    // look for a move from "source" to "dest" in the list
//    public Move findMoveForSquares(int source, int dest) {
//        resetIterator();
//        Move testMove;
//        while ((testMove = next()) != null) {
//            if ((testMove.sourceSquare == source)
//                    && (testMove.destinationSquare == dest)) {
//                return testMove;
//            }
//        }
//        return null;
//    }
//
//    // public jcMove Next()
//    // Find the next move in the list, if any
//    public Move next() {
//        if (movesIt.hasNext()) {
//            return movesIt.next();
//        } else {
//            return null;
//        }
//    }
//
//    // public boolean ComputeLegalMoves
//    // Look at the board received as a parameter, and build a list of legal
//    // moves which can be derived from it. If there are no legal moves, or if
//    // one of the moves is a king capture (which means that the opponent's
//    // previous move left the king in check, which is illegal), return false.
//    public boolean computeLegalMoves(Board theBoard) {
//        // First, clean up the old list of moves, if any
//        moves.clear();
//
//        // Now, compute the moves, one piece type at a time
//        if (theBoard.getCurrentPlayer() == Player.SIDE_WHITE) {
//            // Clean up the data structures indicating that the last white move
//            // was a castling, if any
//            if (theBoard.getExtraKings(Player.SIDE_WHITE) != 0) {
//                theBoard.clearExtraKings(Player.SIDE_WHITE);
//            }
//            // Check for white moves, one piece type at a time
//            // if any one type can capture the king, stop the work immediately
//            // because the board position is illegal
//            if (!computeWhiteQueenMoves(theBoard)) {
//                return false;
//            }
//            if (!computeWhiteKingMoves(theBoard)) {
//                return false;
//            }
//            if (!computeWhiteRookMoves(theBoard, Board.WHITE_ROOK)) {
//                return false;
//            }
//            if (!computeWhiteBishopMoves(theBoard, Board.WHITE_BISHOP)) {
//                return false;
//            }
//            if (!computeWhiteKnightMoves(theBoard)) {
//                return false;
//            }
//            if (!computeWhitePawnMoves(theBoard)) {
//                return false;
//            }
//        } else // Compute Black's moves
//        {
//            if (theBoard.getExtraKings(Player.SIDE_BLACK) != 0) {
//                theBoard.clearExtraKings(Player.SIDE_BLACK);
//            }
//            if (!computeBlackQueenMoves(theBoard)) {
//                return false;
//            }
//            if (!computeBlackKingMoves(theBoard)) {
//                return false;
//            }
//            if (!computeBlackRookMoves(theBoard, Board.BLACK_ROOK)) {
//                return false;
//            }
//            if (!computeBlackBishopMoves(theBoard, Board.BLACK_BISHOP)) {
//                return false;
//            }
//            if (!computeBlackKnightMoves(theBoard)) {
//                return false;
//            }
//            if (!computeBlackPawnMoves(theBoard)) {
//                return false;
//            }
//        }
//
//        // And finally, if there are no pseudo-legal moves at all, we have an
//        // obvious error (there are no pieces on the board!); flag the condition
//        if (moves.size() == 0) {
//            return false;
//        } else {
//            resetIterator();
//            return true;
//        }
//    }
//
//    // public boolean ComputeQuiescenceMoves
//    // Find only the moves which are relevant to quiescence search; i.e.,
//    // captures
//    public boolean computeQuiescenceMoves(Board theBoard) {
//        computeLegalMoves(theBoard);
//        for (int i = moves.size() - 1; i >= 0; i--) {
//            Move mov = moves.get(i);
//            if ((mov.moveType != Move.MOVE_CAPTURE_ORDINARY)
//                    && (mov.moveType != Move.MOVE_CAPTURE_EN_PASSANT)) {
//                moves.remove(i);
//            }
//        }
//        resetIterator();
//        return (moves.size() > 0);
//    }
//
//    // public void Print()
//    // Dump the move list to standard output, for debugging purposes
//    public void print() {
//        // Do not use the iterator, to avoid messing up a regular operation!
//        for (int it = 0; it < moves.size(); it++) {
//            Move mov = moves.get(it);
//            mov.print();
//        }
//    }
//
//    /*************************************************************************
//     * PRIVATE METHODS For move generation
//     *************************************************************************/
//
//    private boolean computeWhiteQueenMoves(Board theBoard) {
//        if (!computeWhiteBishopMoves(theBoard, Board.WHITE_QUEEN)) {
//            return false;
//        }
//        if (!computeWhiteRookMoves(theBoard, Board.WHITE_QUEEN)) {
//            return false;
//        }
//        return true;
//    }
//
//    private boolean computeWhiteKingMoves(Board theBoard) {
//        // Fetch the bitboard containing position of the king
//        long pieces = theBoard.getBitBoard(Board.WHITE_KING);
//
//        // Find it! There is only one king, so look for it and stop
//        int square;
//        for (square = 0; square < 64; square++) {
//            if ((Board.squareBits[square] & pieces) != 0) {
//                break;
//            }
//        }
//
//        // Find its moves
//        for (int i = 0; i < GeneratedMoves.KING_MOVES[square].length; i++) {
//            // Get the destination square
//            int dest = GeneratedMoves.KING_MOVES[square][i];
//
//            // Is it occupied by a friendly piece? If so, can't move there
//            if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                continue;
//            }
//
//            // Otherwise, the move is legal, so we must prepare to add it
//            Move mov = new Move();
//            mov.sourceSquare = square;
//            mov.destinationSquare = dest;
//            mov.movingPiece = Board.WHITE_KING;
//
//            // Is the destination occupied by an enemy? If so, we have a capture
//            if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                mov.capturedPiece = theBoard.findBlackPiece(dest);
//
//                // If the piece we find is a king, abort because the board
//                // position is illegal!
//                if (mov.capturedPiece == Board.BLACK_KING) {
//                    return false;
//                }
//            }
//
//            // otherwise, it is a simple move
//            else {
//                mov.moveType = Move.MOVE_NORMAL;
//                mov.capturedPiece = Board.EMPTY_SQUARE;
//            }
//
//            // And we add the move to the list
//            moves.add(mov);
//        }
//
//        // Now, let's consider castling...
//        // Kingside first
//        if (theBoard.getCastlingStatus(Board.CASTLE_KINGSIDE
//                + Player.SIDE_WHITE)) {
//            // First, check whether there are empty squares between king and
//            // rook
//            if (((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.EMPTYSQUARES_WHITE_KINGSIDE) == 0)
//                    && ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.EMPTYSQUARES_WHITE_KINGSIDE) == 0)) {
//                Move mov = new Move();
//                mov.movingPiece = Board.WHITE_KING;
//                mov.sourceSquare = 60;
//                mov.destinationSquare = 62;
//                mov.moveType = Move.MOVE_CASTLING_KINGSIDE;
//                mov.capturedPiece = Board.EMPTY_SQUARE;
//                moves.add(mov);
//            }
//        }
//        if (theBoard.getCastlingStatus(Board.CASTLE_QUEENSIDE
//                + Player.SIDE_WHITE)) {
//            if (((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.EMPTYSQUARES_WHITE_QUEENSIDE) == 0)
//                    && ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.EMPTYSQUARES_WHITE_QUEENSIDE) == 0)) {
//                Move mov = new Move();
//                mov.movingPiece = Board.WHITE_KING;
//                mov.sourceSquare = 60;
//                mov.destinationSquare = 58;
//                mov.moveType = Move.MOVE_CASTLING_QUEENSIDE;
//                mov.capturedPiece = Board.EMPTY_SQUARE;
//                moves.add(mov);
//            }
//        }
//        return true;
//    }
//
//    // private boolean ComputeWhiteROOK_MOVES
//    // Receives an extra "pieceType" parameter, because the queen AND the rook
//    // need to use this function
//    private boolean computeWhiteRookMoves(Board theBoard, int pieceType) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(pieceType);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // This is a white piece, so let's start looking at the bottom
//        // of the board
//        for (int square = 63; square >= 0; square--) {
//            if ((pieces & Board.squareBits[square]) != 0) {
//                // There is a piece here; find its moves
//                for (int ray = 0; ray < GeneratedMoves.ROOK_MOVES[square].length; ray++) {
//                    for (int i = 0; i < GeneratedMoves.ROOK_MOVES[square][ray].length; i++) {
//                        // Get the destination square
//                        int dest = GeneratedMoves.ROOK_MOVES[square][ray][i];
//
//                        // Is it occupied by a friendly piece? If so, can't move
//                        // there
//                        // AND we must discontinue the current ray
//                        if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                            break;
//                        }
//
//                        // Otherwise, the move is legal, so we must prepare to
//                        // add it
//                        Move mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = pieceType;
//
//                        // Is the destination occupied by an enemy? If so, we
//                        // have a capture
//                        if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                            mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                            mov.capturedPiece = theBoard.findBlackPiece(dest);
//
//                            // If the piece we find is a king, abort because the
//                            // board
//                            // position is illegal!
//                            if (mov.capturedPiece == Board.BLACK_KING) {
//                                return false;
//                            }
//
//                            moves.add(mov);
//                            break;
//                        }
//                        // otherwise, it is a simple move
//                        else {
//                            mov.moveType = Move.MOVE_NORMAL;
//                            mov.capturedPiece = Board.EMPTY_SQUARE;
//                            moves.add(mov);
//                        }
//                    }
//                }
//                // Turn off the bit in the temporary bitboard; this way, we can
//                // detect whether we have found the last of this type of piece
//                // and short-circuit the loop
//                pieces ^= Board.squareBits[square];
//                if (pieces == 0) {
//                    return true;
//                }
//            }
//        }
//
//        // We should never get here, but the return statement is added to
//        // prevent
//        // obnoxious compiler warnings
//        return true;
//    }
//
//    private boolean computeWhiteBishopMoves(Board theBoard, int pieceType) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(pieceType);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // This is a white piece, so let's start looking at the bottom
//        // of the board
//        for (int square = 63; square >= 0; square--) {
//            if ((pieces & Board.squareBits[square]) != 0) {
//                // There is a piece here; find its moves
//                for (int ray = 0; ray < GeneratedMoves.BISHOP_MOVES[square].length; ray++) {
//                    for (int i = 0; i < GeneratedMoves.BISHOP_MOVES[square][ray].length; i++) {
//                        // Get the destination square
//                        int dest = GeneratedMoves.BISHOP_MOVES[square][ray][i];
//
//                        // Is it occupied by a friendly piece? If so, can't move
//                        // there
//                        // AND we must discontinue the current ray
//                        if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                            break;
//                        }
//
//                        // Otherwise, the move is legal, so we must prepare to
//                        // add it
//                        Move mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = pieceType;
//
//                        // Is the destination occupied by an enemy? If so, we
//                        // have a capture
//                        if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                            mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                            mov.capturedPiece = theBoard.findBlackPiece(dest);
//
//                            // If the piece we find is a king, abort because the
//                            // board
//                            // position is illegal!
//                            if (mov.capturedPiece == Board.BLACK_KING) {
//                                return false;
//                            }
//
//                            moves.add(mov);
//                            break;
//                        }
//                        // otherwise, it is a simple move
//                        else {
//                            mov.moveType = Move.MOVE_NORMAL;
//                            mov.capturedPiece = Board.EMPTY_SQUARE;
//                            moves.add(mov);
//                        }
//                    }
//                }
//                // Turn off the bit in the temporary bitboard; this way, we can
//                // detect whether we have found the last of this type of piece
//                // and short-circuit the loop
//                pieces ^= Board.squareBits[square];
//                if (pieces == 0) {
//                    return true;
//                }
//            }
//        }
//
//        // We should never get here, but the return statement is added to
//        // prevent
//        // obnoxious compiler warnings
//        return true;
//    }
//
//    private boolean computeWhiteKnightMoves(Board theBoard) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(Board.WHITE_KNIGHT);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // This is a white piece, so let's start looking at the bottom
//        // of the board
//        for (int square = 63; square >= 0; square--) {
//            if ((pieces & Board.squareBits[square]) != 0) {
//                // There is a piece here; find its moves
//                for (int i = 0; i < GeneratedMoves.KNIGHT_MOVES[square].length; i++) {
//                    // Get the destination square
//                    int dest = GeneratedMoves.KNIGHT_MOVES[square][i];
//
//                    // Is it occupied by a friendly piece? If so, can't move
//                    // there
//                    if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                        continue;
//                    }
//
//                    // Otherwise, the move is legal, so we must prepare to add
//                    // it
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_KNIGHT;
//
//                    // Is the destination occupied by an enemy? If so, we have a
//                    // capture
//                    if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//
//                        // If the piece we find is a king, abort because the
//                        // board
//                        // position is illegal!
//                        if (mov.capturedPiece == Board.BLACK_KING) {
//                            return false;
//                        }
//                    }
//                    // otherwise, it is a simple move
//                    else {
//                        mov.moveType = Move.MOVE_NORMAL;
//                        mov.capturedPiece = Board.EMPTY_SQUARE;
//                    }
//
//                    // And we add the move to the list
//                    moves.add(mov);
//                }
//
//                // Turn off the bit in the temporary bitboard; this way, we can
//                // detect whether we have found the last of this type of piece
//                // and short-circuit the loop
//                pieces ^= Board.squareBits[square];
//                if (pieces == 0) {
//                    return true;
//                }
//            }
//        }
//
//        // We should never get here, but the return statement is added to
//        // prevent
//        // obnoxious compiler warnings
//        return true;
//    }
//
//    private boolean computeWhitePawnMoves(Board theBoard) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(Board.WHITE_PAWN);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // a small optimization
//        long allPieces = theBoard.getBitBoard(Board.ALL_BLACK_PIECES)
//                | theBoard.getBitBoard(Board.ALL_WHITE_PIECES);
//
//        // This is a white piece, so let's start looking at the bottom
//        // of the board... But only consider positions where a pawn can
//        // actually dwell!
//        int dest;
//        for (int square = 55; square >= 8; square--) {
//            if ((pieces & Board.squareBits[square]) == 0) {
//                continue;
//            }
//
//            // First, try a normal pawn pushing
//            dest = square - 8;
//            if ((allPieces & Board.squareBits[dest]) == 0) {
//                // Unless this push results in a promotion...
//                if (square > 15) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_NORMAL;
//                    moves.add(mov);
//
//                    // Is there a chance to perform a double push? Only if the
//                    // piece
//                    // is in its original square
//                    if (square >= 48) {
//                        dest -= 8;
//                        if ((allPieces & Board.squareBits[dest]) == 0) {
//                            mov = new Move();
//                            mov.sourceSquare = square;
//                            mov.destinationSquare = dest;
//                            mov.movingPiece = Board.WHITE_PAWN;
//                            mov.moveType = Move.MOVE_NORMAL;
//                            moves.add(mov);
//                        }
//                    }
//                } else // if square < 16
//                {
//                    // We are now looking at pawn promotion!
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_QUEEN
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//                    mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_KNIGHT
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//                    mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_ROOK
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//                    mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_BISHOP
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//
//                }
//            }
//
//            // Now, let's try a capture
//            // Three cases: the pawn is on the 1st file, the 8th file, or
//            // elsewhere
//            if ((square % 8) == 0) {
//                dest = square - 7;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest < 8) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.capturedPiece = theBoard.findBlackPiece(dest);
//                    moves.add(mov);
//
//                    // Other promotion captures
//                    if (dest < 8) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.capturedPiece = Board.BLACK_PAWN;
//                    moves.add(mov);
//                }
//            } else if ((square % 8) == 7) {
//                dest = square - 9;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest < 8) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.capturedPiece = theBoard.findBlackPiece(dest);
//                    moves.add(mov);
//                    // Other promotion captures
//                    if (dest < 8) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.capturedPiece = Board.BLACK_PAWN;
//                    moves.add(mov);
//                }
//            } else {
//                dest = square - 7;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest < 8) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.capturedPiece = theBoard.findBlackPiece(dest);
//                    moves.add(mov);
//
//                    // Other promotion captures
//                    if (dest < 8) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.capturedPiece = Board.BLACK_PAWN;
//                    moves.add(mov);
//                }
//                dest = square - 9;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest < 8) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.capturedPiece = theBoard.findBlackPiece(dest);
//                    moves.add(mov);
//                    // Other promotion captures
//                    if (dest < 8) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.WHITE_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findBlackPiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.movingPiece = Board.WHITE_PAWN;
//                    mov.capturedPiece = Board.BLACK_PAWN;
//                    moves.add(mov);
//                }
//            }
//
//            // And perform the usual trick to abort the loop when we no longer
//            // have any pieces to look for
//            pieces ^= Board.squareBits[square];
//            if (pieces == 0) {
//                return true;
//            }
//
//        }
//        return true;
//    }
//
//    private boolean computeBlackQueenMoves(Board theBoard) {
//        if (!computeBlackRookMoves(theBoard, Board.BLACK_QUEEN)) {
//            return false;
//        }
//        if (!computeBlackBishopMoves(theBoard, Board.BLACK_QUEEN)) {
//            return false;
//        }
//        return true;
//    }
//
//    private boolean computeBlackKingMoves(Board theBoard) {
//        // Fetch the bitboard containing position of the king
//        long pieces = theBoard.getBitBoard(Board.BLACK_KING);
//
//        // Find it! There is only one king, so look for it and stop
//        int square;
//        for (square = 0; square < 64; square++) {
//            if ((Board.squareBits[square] & pieces) != 0) {
//                break;
//            }
//        }
//
//        // Find its moves
//        for (int i = 0; i < GeneratedMoves.KING_MOVES[square].length; i++) {
//            // Get the destination square
//            int dest = GeneratedMoves.KING_MOVES[square][i];
//
//            // Is it occupied by a friendly piece? If so, can't move there
//            if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                continue;
//            }
//
//            // Otherwise, the move is legal, so we must prepare to add it
//            Move mov = new Move();
//            mov.sourceSquare = square;
//            mov.destinationSquare = dest;
//            mov.movingPiece = Board.BLACK_KING;
//
//            // Is the destination occupied by an enemy? If so, we have a capture
//            if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                mov.capturedPiece = theBoard.findWhitePiece(dest);
//
//                // If the piece we find is a king, abort because the board
//                // position is illegal!
//                if (mov.capturedPiece == Board.WHITE_KING) {
//                    return false;
//                }
//            }
//
//            // otherwise, it is a simple move
//            else {
//                mov.moveType = Move.MOVE_NORMAL;
//                mov.capturedPiece = Board.EMPTY_SQUARE;
//            }
//
//            // And we add the move to the list
//            moves.add(mov);
//        }
//
//        // Now, let's consider castling...
//        // Kingside first
//        if (theBoard.getCastlingStatus(Board.CASTLE_KINGSIDE
//                + Player.SIDE_BLACK)) {
//            // First, check whether there are empty squares between king and
//            // rook
//            if (((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.EMPTYSQUARES_BLACK_KINGSIDE) == 0)
//                    && ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.EMPTYSQUARES_BLACK_KINGSIDE) == 0)) {
//                Move mov = new Move();
//                mov.movingPiece = Board.BLACK_KING;
//                mov.sourceSquare = 4;
//                mov.destinationSquare = 6;
//                mov.moveType = Move.MOVE_CASTLING_KINGSIDE;
//                mov.capturedPiece = Board.EMPTY_SQUARE;
//                moves.add(mov);
//            }
//        }
//        if (theBoard.getCastlingStatus(Board.CASTLE_QUEENSIDE
//                + Player.SIDE_BLACK)) {
//            if (((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.EMPTYSQUARES_BLACK_QUEENSIDE) == 0)
//                    && ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.EMPTYSQUARES_BLACK_QUEENSIDE) == 0)) {
//                Move mov = new Move();
//                mov.movingPiece = Board.BLACK_KING;
//                mov.sourceSquare = 4;
//                mov.destinationSquare = 2;
//                mov.moveType = Move.MOVE_CASTLING_QUEENSIDE;
//                mov.capturedPiece = Board.EMPTY_SQUARE;
//                moves.add(mov);
//            }
//        }
//        return true;
//    }
//
//    private boolean computeBlackRookMoves(Board theBoard, int pieceType) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(pieceType);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // This is a black piece, so let's start looking at the top
//        // of the board
//        for (int square = 0; square < 64; square++) {
//            if ((pieces & Board.squareBits[square]) != 0) {
//                // There is a piece here; find its moves
//                for (int ray = 0; ray < GeneratedMoves.ROOK_MOVES[square].length; ray++) {
//                    for (int i = 0; i < GeneratedMoves.ROOK_MOVES[square][ray].length; i++) {
//                        // Get the destination square
//                        int dest = GeneratedMoves.ROOK_MOVES[square][ray][i];
//
//                        // Is it occupied by a friendly piece? If so, can't move
//                        // there
//                        // AND we must discontinue the current ray
//                        if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                            break;
//                        }
//
//                        // Otherwise, the move is legal, so we must prepare to
//                        // add it
//                        Move mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = pieceType;
//
//                        // Is the destination occupied by an enemy? If so, we
//                        // have a capture
//                        if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                            mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                            mov.capturedPiece = theBoard.findWhitePiece(dest);
//
//                            // If the piece we find is a king, abort because the
//                            // board
//                            // position is illegal!
//                            if (mov.capturedPiece == Board.WHITE_KING) {
//                                return false;
//                            }
//
//                            moves.add(mov);
//                            break;
//                        }
//                        // otherwise, it is a simple move
//                        else {
//                            mov.moveType = Move.MOVE_NORMAL;
//                            mov.capturedPiece = Board.EMPTY_SQUARE;
//                            moves.add(mov);
//                        }
//                    }
//                }
//                // Turn off the bit in the temporary bitboard; this way, we can
//                // detect whether we have found the last of this type of piece
//                // and short-circuit the loop
//                pieces ^= Board.squareBits[square];
//                if (pieces == 0) {
//                    return true;
//                }
//            }
//        }
//
//        // We should never get here, but the return statement is added to
//        // prevent
//        // obnoxious compiler warnings
//        return true;
//    }
//
//    private boolean computeBlackBishopMoves(Board theBoard, int pieceType) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(pieceType);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // This is a black piece, so let's start looking at the top
//        // of the board
//        for (int square = 0; square < 64; square++) {
//            if ((pieces & Board.squareBits[square]) != 0) {
//                // There is a piece here; find its moves
//                for (int ray = 0; ray < GeneratedMoves.BISHOP_MOVES[square].length; ray++) {
//                    for (int i = 0; i < GeneratedMoves.BISHOP_MOVES[square][ray].length; i++) {
//                        // Get the destination square
//                        int dest = GeneratedMoves.BISHOP_MOVES[square][ray][i];
//
//                        // Is it occupied by a friendly piece? If so, can't move
//                        // there
//                        // AND we must discontinue the current ray
//                        if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                            break;
//                        }
//
//                        // Otherwise, the move is legal, so we must prepare to
//                        // add it
//                        Move mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = pieceType;
//
//                        // Is the destination occupied by an enemy? If so, we
//                        // have a capture
//                        if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                            mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                            mov.capturedPiece = theBoard.findWhitePiece(dest);
//
//                            // If the piece we find is a king, abort because the
//                            // board
//                            // position is illegal!
//                            if (mov.capturedPiece == Board.WHITE_KING) {
//                                return false;
//                            }
//
//                            // Otherwise, add the move to the list and interrupt
//                            // the ray
//                            moves.add(mov);
//                            break;
//                        }
//                        // otherwise, it is a simple move
//                        else {
//                            mov.moveType = Move.MOVE_NORMAL;
//                            mov.capturedPiece = Board.EMPTY_SQUARE;
//                            moves.add(mov);
//                        }
//                    }
//                }
//                // Turn off the bit in the temporary bitboard; this way, we can
//                // detect whether we have found the last of this type of piece
//                // and short-circuit the loop
//                pieces ^= Board.squareBits[square];
//                if (pieces == 0) {
//                    return true;
//                }
//            }
//        }
//
//        // We should never get here, but the return statement is added to
//        // prevent
//        // obnoxious compiler warnings
//        return true;
//    }
//
//    private boolean computeBlackKnightMoves(Board theBoard) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(Board.BLACK_KNIGHT);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // This is a black piece, so let's start looking at the top
//        // of the board
//        for (int square = 0; square < 64; square++) {
//            if ((pieces & Board.squareBits[square]) != 0) {
//                // There is a piece here; find its moves
//                for (int i = 0; i < GeneratedMoves.KNIGHT_MOVES[square].length; i++) {
//                    // Get the destination square
//                    int dest = GeneratedMoves.KNIGHT_MOVES[square][i];
//
//                    // Is it occupied by a friendly piece? If so, can't move
//                    // there
//                    if ((theBoard.getBitBoard(Board.ALL_BLACK_PIECES) & Board.squareBits[dest]) != 0) {
//                        continue;
//                    }
//
//                    // Otherwise, the move is legal, so we must prepare to add
//                    // it
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_KNIGHT;
//
//                    // Is the destination occupied by an enemy? If so, we have a
//                    // capture
//                    if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//
//                        // If the piece we find is a king, abort because the
//                        // board
//                        // position is illegal!
//                        if (mov.capturedPiece == Board.WHITE_KING) {
//                            return false;
//                        }
//                    }
//                    // otherwise, it is a simple move
//                    else {
//                        mov.moveType = Move.MOVE_NORMAL;
//                        mov.capturedPiece = Board.EMPTY_SQUARE;
//                    }
//
//                    // And we add the move to the list
//                    moves.add(mov);
//                }
//
//                // Turn off the bit in the temporary bitboard; this way, we can
//                // detect whether we have found the last of this type of piece
//                // and short-circuit the loop
//                pieces ^= Board.squareBits[square];
//                if (pieces == 0) {
//                    return true;
//                }
//            }
//        }
//
//        // We should never get here, but the return statement is added to
//        // prevent
//        // obnoxious compiler warnings
//        return true;
//    }
//
//    private boolean computeBlackPawnMoves(Board theBoard) {
//        // Fetch the bitboard containing positions of these pieces
//        long pieces = theBoard.getBitBoard(Board.BLACK_PAWN);
//
//        // If there are no pieces of this type, no need to work very hard!
//        if (pieces == 0) {
//            return true;
//        }
//
//        // a small optimization
//        long allPieces = theBoard.getBitBoard(Board.ALL_BLACK_PIECES)
//                | theBoard.getBitBoard(Board.ALL_WHITE_PIECES);
//
//        // This is a black piece, so let's start looking at the top
//        // of the board... But only consider positions where a pawn can
//        // actually dwell!
//        int dest;
//        for (int square = 8; square < 56; square++) {
//            if ((pieces & Board.squareBits[square]) == 0) {
//                continue;
//            }
//
//            // First, try a normal pawn pushing
//            dest = square + 8;
//            if ((allPieces & Board.squareBits[dest]) == 0) {
//                // Unless this push results in a promotion...
//                if (square < 48) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_NORMAL;
//                    moves.add(mov);
//
//                    // Is there a chance to perform a double push? Only if the
//                    // piece
//                    // is in its original square
//                    if (square < 16) {
//                        dest += 8;
//                        if ((allPieces & Board.squareBits[dest]) == 0) {
//                            mov = new Move();
//                            mov.sourceSquare = square;
//                            mov.destinationSquare = dest;
//                            mov.movingPiece = Board.BLACK_PAWN;
//                            mov.moveType = Move.MOVE_NORMAL;
//                            moves.add(mov);
//                        }
//                    }
//                } else // if square >= 48
//                {
//                    // We are now looking at pawn promotion!
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_QUEEN
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//                    mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_KNIGHT
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//                    mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_ROOK
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//                    mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_PROMOTION_BISHOP
//                            + Move.MOVE_NORMAL;
//                    moves.add(mov);
//
//                }
//            }
//
//            // Now, let's try a capture
//            // Three cases: the pawn is on the 1st file, the 8th file, or
//            // elsewhere
//            if ((square % 8) == 0) {
//                dest = square + 9;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest >= 56) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.capturedPiece = theBoard.findWhitePiece(dest);
//                    moves.add(mov);
//
//                    // Other promotion captures
//                    if (dest >= 56) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.capturedPiece = Board.WHITE_PAWN;
//                    moves.add(mov);
//                }
//            } else if ((square % 8) == 7) {
//                dest = square + 7;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest >= 56) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.capturedPiece = theBoard.findWhitePiece(dest);
//                    moves.add(mov);
//                    // Other promotion captures
//                    if (dest >= 56) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.capturedPiece = Board.WHITE_PAWN;
//                    moves.add(mov);
//                }
//            } else {
//                dest = square + 9;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest >= 56) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.capturedPiece = theBoard.findWhitePiece(dest);
//                    moves.add(mov);
//                    // Other promotion captures
//                    if (dest >= 56) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.capturedPiece = Board.WHITE_PAWN;
//                    moves.add(mov);
//                }
//                dest = square + 7;
//                // Try an ordinary capture first
//                if ((theBoard.getBitBoard(Board.ALL_WHITE_PIECES) & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.moveType = Move.MOVE_CAPTURE_ORDINARY;
//                    if (dest >= 56) {
//                        mov.moveType += Move.MOVE_PROMOTION_QUEEN;
//                    }
//                    mov.capturedPiece = theBoard.findWhitePiece(dest);
//                    moves.add(mov);
//                    // Other promotion captures
//                    if (dest >= 56) {
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_KNIGHT;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_BISHOP;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                        mov = new Move();
//                        mov.sourceSquare = square;
//                        mov.destinationSquare = dest;
//                        mov.movingPiece = Board.BLACK_PAWN;
//                        mov.moveType = Move.MOVE_CAPTURE_ORDINARY
//                                + Move.MOVE_PROMOTION_ROOK;
//                        mov.capturedPiece = theBoard.findWhitePiece(dest);
//                        moves.add(mov);
//                    }
//                }
//                // Now, try an en passant capture
//                else if ((theBoard.getEnPassantPawn() & Board.squareBits[dest]) != 0) {
//                    Move mov = new Move();
//                    mov.sourceSquare = square;
//                    mov.destinationSquare = dest;
//                    mov.moveType = Move.MOVE_CAPTURE_EN_PASSANT;
//                    mov.movingPiece = Board.BLACK_PAWN;
//                    mov.capturedPiece = Board.WHITE_PAWN;
//                    moves.add(mov);
//                }
//            }
//
//            // And perform the usual trick to abort the loop when we no longer
//            // have any pieces to look for
//            pieces ^= Board.squareBits[square];
//            if (pieces == 0) {
//                return true;
//            }
//
//        }
//        return true;
//    }
//
//
//}