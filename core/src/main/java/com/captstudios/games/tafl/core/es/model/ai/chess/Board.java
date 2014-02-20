///*****************************************************************************
// * jcBoard.java - Encapsulation of a chess board
// * by Fran�ois Dominic Laram�e
// *
// * Purpose: This object contains all of the data and methods required to
// * process a chess board in the game.  It uses the ubiquitous "bitboard"
// * representation.
// *
// * History:
// * 08.06.00 Created
// * 14.08.00 Made "HashLock" a relative clone of "HashKey"; the java
// *          Object.hashCode method is unsuitable to our purposes after all,
// *          probably because it includes memory addresses in the calculation.
// ***************************************************************************/
//
//package com.captstudios.games.tafl.core.es.model.ai.chess;
//
//import java.io.BufferedWriter;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.StreamTokenizer;
//import java.util.Random;
//
//import com.captstudios.games.tafl.core.es.model.ai.chess.game.player.Player;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.Move;
//
///****************************************************************************
// * public class jcBoard
// *
// * Notes: 1. The squares are numbered line by line, starting in the corner
// * occupied by Black's Queen's Rook at the beginning of the game. There are no
// * constants to represent squares, as they are usually manipulated
// * algorithmically, in sequences, instead of being explicitly identified in the
// * code.
// ***************************************************************************/
//
//public class Board {
//
//    /***************************************************************************
//     * CONSTANTS
//     **************************************************************************/
//
//    // Codes representing pieces
//    public static final int PAWN = 0;
//    public static final int KNIGHT = 2;
//    public static final int BISHOP = 4;
//    public static final int ROOK = 6;
//    public static final int QUEEN = 8;
//    public static final int KING = 10;
//    public static final int WHITE_PAWN = PAWN + Player.SIDE_WHITE;
//    public static final int WHITE_KNIGHT = KNIGHT + Player.SIDE_WHITE;
//    public static final int WHITE_BISHOP = BISHOP + Player.SIDE_WHITE;
//    public static final int WHITE_ROOK = ROOK + Player.SIDE_WHITE;
//    public static final int WHITE_QUEEN = QUEEN + Player.SIDE_WHITE;
//    public static final int WHITE_KING = KING + Player.SIDE_WHITE;
//    public static final int BLACK_PAWN = PAWN + Player.SIDE_BLACK;
//    public static final int BLACK_KNIGHT = KNIGHT + Player.SIDE_BLACK;
//    public static final int BLACK_BISHOP = BISHOP + Player.SIDE_BLACK;
//    public static final int BLACK_ROOK = ROOK + Player.SIDE_BLACK;
//    public static final int BLACK_QUEEN = QUEEN + Player.SIDE_BLACK;
//    public static final int BLACK_KING = KING + Player.SIDE_BLACK;
//    public static final int EMPTY_SQUARE = 12;
//
//    // Useful loop boundary constants, to allow looping on all bitboards and
//    // on all squares of a chessboard
//    public static final int ALL_PIECES = 12;
//    public static final int ALL_SQUARES = 64;
//
//    // Indices of the "shortcut" bitboards containing information on "all black
//    // pieces" and "all white pieces"
//    public static final int ALL_WHITE_PIECES = ALL_PIECES + Player.SIDE_WHITE;
//    public static final int ALL_BLACK_PIECES = ALL_PIECES + Player.SIDE_BLACK;
//    public static final int ALL_BITBOARDS = 14;
//
//    // The possible types of castling moves; add the "side" constant to
//    // pick a specific move for a specific player
//    public static final int CASTLE_KINGSIDE = 0;
//    public static final int CASTLE_QUEENSIDE = 2;
//
//    /***************************************************************************
//     * DATA MEMBERS
//     **************************************************************************/
//
//    // An array of bitfields, each of which contains the single bit associated
//    // with a square in a bitboard
//    public static long squareBits[];
//
//    // Private table of random numbers used to compute Zobrist hash values
//    // Contains a signature for any kind of piece on any square of the board
//    private static int hashKeyComponents[][];
//    private static int hashLockComponents[][];
//
//    // Private table of tokens (string representations) for all pieces
//    public static String pieceStrings[];
//
//    // Data needed to compute the evaluation function
//    private int materialValue[];
//    private int numPawns[];
//    private static int pieceValues[];
//
//    // And a few flags for special conditions. The ExtraKings are a device
//    // used to detect illegal castling moves: the rules of chess forbid castling
//    // when the king is in check or when the square it flies over is under
//    // attack; therefore, we add "phantom kings" to the board for one move only,
//    // and if the opponent can capture one of them with its next move, then
//    // castling was illegal and search can be cancelled
//    private long extraKings[];
//    public static long EXTRAKINGS_WHITE_KINGSIDE;
//    public static long EXTRAKINGS_WHITE_QUEENSIDE;
//    public static long EXTRAKINGS_BLACK_KINGSIDE;
//    public static long EXTRAKINGS_BLACK_QUEENSIDE;
//    public static long EMPTYSQUARES_WHITE_KINGSIDE;
//    public static long EMPTYSQUARES_WHITE_QUEENSIDE;
//    public static long EMPTYSQUARES_BLACK_KINGSIDE;
//    public static long EMPTYSQUARES_BLACK_QUEENSIDE;
//
//    // static member initialization
//    static {
//        // Build the SquareBits constants
//        squareBits = new long[ALL_SQUARES];
//        for (int i = 0; i < ALL_SQUARES; i++) {
//            // Note: the 1L specifies that the 1 we are shifting is a long int
//            // Java would, by default, make it a 4-byte int and be unable to
//            // shift the 1 to bits 32 to 63
//            squareBits[i] = (1L << i);
//        }
//
//        // Build the extrakings constants
//        EXTRAKINGS_WHITE_KINGSIDE = squareBits[60] | squareBits[61];
//        EXTRAKINGS_WHITE_QUEENSIDE = squareBits[60] | squareBits[59];
//        EXTRAKINGS_BLACK_KINGSIDE = squareBits[4] | squareBits[5];
//        EXTRAKINGS_BLACK_QUEENSIDE = squareBits[4] | squareBits[3];
//        EMPTYSQUARES_WHITE_KINGSIDE = squareBits[61] | squareBits[62];
//        EMPTYSQUARES_WHITE_QUEENSIDE = squareBits[59] | squareBits[58]
//                | squareBits[57];
//        EMPTYSQUARES_BLACK_KINGSIDE = squareBits[5] | squareBits[6];
//        EMPTYSQUARES_BLACK_QUEENSIDE = squareBits[3] | squareBits[2]
//                | squareBits[1];
//
//        // Build the hashing database
//        hashKeyComponents = new int[ALL_PIECES][ALL_SQUARES];
//        hashLockComponents = new int[ALL_PIECES][ALL_SQUARES];
//        Random rnd = new Random();
//        for (int i = 0; i < ALL_PIECES; i++) {
//            for (int j = 0; j < ALL_SQUARES; j++) {
//                hashKeyComponents[i][j] = rnd.nextInt();
//                hashLockComponents[i][j] = rnd.nextInt();
//            }
//        }
//
//        // Tokens representing the various concepts in the game, for printint
//        // and file i/o purposes
//        // PieceStrings contains an extra string representing empty squares
//        pieceStrings = new String[ALL_PIECES + 1];
//        pieceStrings[WHITE_PAWN] = "WP";
//        pieceStrings[WHITE_ROOK] = "WR";
//        pieceStrings[WHITE_KNIGHT] = "WN";
//        pieceStrings[WHITE_BISHOP] = "WB";
//        pieceStrings[WHITE_QUEEN] = "WQ";
//        pieceStrings[WHITE_KING] = "WK";
//        pieceStrings[BLACK_PAWN] = "BP";
//        pieceStrings[BLACK_ROOK] = "BR";
//        pieceStrings[BLACK_KNIGHT] = "BN";
//        pieceStrings[BLACK_BISHOP] = "BB";
//        pieceStrings[BLACK_QUEEN] = "BQ";
//        pieceStrings[BLACK_KING] = "BK";
//        pieceStrings[ALL_PIECES] = "  ";
//
//        // Numerical evaluation of piece material values
//        pieceValues = new int[ALL_PIECES];
//        pieceValues[WHITE_PAWN] = 100;
//        pieceValues[BLACK_PAWN] = 100;
//        pieceValues[WHITE_KNIGHT] = 300;
//        pieceValues[BLACK_KNIGHT] = 300;
//        pieceValues[WHITE_BISHOP] = 350;
//        pieceValues[BLACK_BISHOP] = 350;
//        pieceValues[WHITE_ROOK] = 500;
//        pieceValues[BLACK_ROOK] = 500;
//        pieceValues[BLACK_QUEEN] = 900;
//        pieceValues[WHITE_QUEEN] = 900;
//        pieceValues[WHITE_KING] = 2000;
//        pieceValues[BLACK_KING] = 2000;
//    }
//
//    // The actual data representation of a chess board. First, an array of
//    // bitboards, each of which contains flags for the squares where you can
//    // find a specific type of piece
//    private long bitBoards[];
//
//    // And a few other flags
//    private boolean castlingStatus[];
//    private boolean hasCastled[];
//    private long enPassantPawn;
//
//    // Whose turn is it?
//    int currentPlayer;
//
//    /**************************************************************************
//     * METHODS
//     **************************************************************************/
//
//    // Accessors
//    public boolean getCastlingStatus(int which) {
//        return castlingStatus[which];
//    }
//
//    public boolean getHasCastled(int which) {
//        return hasCastled[which];
//    }
//
//    public long getEnPassantPawn() {
//        return enPassantPawn;
//    }
//
//    public long getExtraKings(int side) {
//        return extraKings[side];
//    }
//
//    public void setExtraKings(int side, long val) {
//        // Mark a few squares as containing "phantom kings" to detect illegal
//        // castling
//        extraKings[side] = val;
//        bitBoards[KING + side] |= extraKings[side];
//        bitBoards[ALL_PIECES + side] |= extraKings[side];
//    }
//
//    public void clearExtraKings(int side) {
//        bitBoards[KING + side] ^= extraKings[side];
//        bitBoards[ALL_PIECES + side] ^= extraKings[side];
//        // Note: one of the Extra Kings is superimposed on the rook involved in
//        // the castling, so the next step is required to prevent ALL_PIECES from
//        // forgetting about the rook at the same time as the phantom king
//        bitBoards[ALL_PIECES + side] |= bitBoards[ROOK + side];
//        extraKings[side] = 0;
//    }
//
//    public int getCurrentPlayer() {
//        return currentPlayer;
//    }
//
//    public long getBitBoard(int which) {
//        return bitBoards[which];
//    }
//
//    // Look for the piece located on a specific square
//    public int findBlackPiece(int square) {
//        // Note: we look for kings first for two reasons: because it helps
//        // detect check, and because there may be a phantom king (marking an
//        // illegal castling move) and a rook on the same square!
//        if ((bitBoards[BLACK_KING] & squareBits[square]) != 0) {
//            return BLACK_KING;
//        }
//        if ((bitBoards[BLACK_QUEEN] & squareBits[square]) != 0) {
//            return BLACK_QUEEN;
//        }
//        if ((bitBoards[BLACK_ROOK] & squareBits[square]) != 0) {
//            return BLACK_ROOK;
//        }
//        if ((bitBoards[BLACK_KNIGHT] & squareBits[square]) != 0) {
//            return BLACK_KNIGHT;
//        }
//        if ((bitBoards[BLACK_BISHOP] & squareBits[square]) != 0) {
//            return BLACK_BISHOP;
//        }
//        if ((bitBoards[BLACK_PAWN] & squareBits[square]) != 0) {
//            return BLACK_PAWN;
//        }
//        return EMPTY_SQUARE;
//    }
//
//    public int findWhitePiece(int square) {
//        if ((bitBoards[WHITE_KING] & squareBits[square]) != 0) {
//            return WHITE_KING;
//        }
//        if ((bitBoards[WHITE_QUEEN] & squareBits[square]) != 0) {
//            return WHITE_QUEEN;
//        }
//        if ((bitBoards[WHITE_ROOK] & squareBits[square]) != 0) {
//            return WHITE_ROOK;
//        }
//        if ((bitBoards[WHITE_KNIGHT] & squareBits[square]) != 0) {
//            return WHITE_KNIGHT;
//        }
//        if ((bitBoards[WHITE_BISHOP] & squareBits[square]) != 0) {
//            return WHITE_BISHOP;
//        }
//        if ((bitBoards[WHITE_PAWN] & squareBits[square]) != 0) {
//            return WHITE_PAWN;
//        }
//        return EMPTY_SQUARE;
//    }
//
//    // Constructor
//    public Board() {
//        bitBoards = new long[ALL_BITBOARDS];
//        castlingStatus = new boolean[4];
//        hasCastled = new boolean[2];
//        extraKings = new long[2];
//        numPawns = new int[2];
//        materialValue = new int[2];
//        startingBoard();
//    }
//
//    // public boolean Clone
//    // Make a deep copy of a jcBoard object; assumes that memory has already
//    // been allocated for the new object, which is always true since we
//    // "allocate" jcBoards from a permanent array
//    public boolean clone(Board target) {
//        enPassantPawn = target.enPassantPawn;
//        for (int i = 0; i < 4; i++) {
//            castlingStatus[i] = target.castlingStatus[i];
//        }
//        for (int i = 0; i < ALL_BITBOARDS; i++) {
//            bitBoards[i] = target.bitBoards[i];
//        }
//        materialValue[0] = target.materialValue[0];
//        materialValue[1] = target.materialValue[1];
//        numPawns[0] = target.numPawns[0];
//        numPawns[1] = target.numPawns[1];
//        extraKings[0] = target.extraKings[0];
//        extraKings[1] = target.extraKings[1];
//        hasCastled[0] = target.hasCastled[0];
//        hasCastled[1] = target.hasCastled[1];
//        currentPlayer = target.currentPlayer;
//        return true;
//    }
//
//    // public boolean Print
//    // Display the board on standard output
//    public boolean print() {
//        for (int line = 0; line < 8; line++) {
//            System.out.println("-----------------------------------------");
//            System.out.println("|    |    |    |    |    |    |    |    |");
//            for (int col = 0; col < 8; col++) {
//                long bits = squareBits[line * 8 + col];
//
//                // Scan the bitboards to find a piece, if any
//                int piece = 0;
//                while ((piece < ALL_PIECES) && ((bits & bitBoards[piece]) == 0)) {
//                    piece++;
//                }
//
//                // One exception: don't show the "phantom kings" which the
//                // program places
//                // on the board to detect illegal attempts at castling over an
//                // attacked
//                // square
//                if ((piece == BLACK_KING)
//                        && ((extraKings[Player.SIDE_BLACK] & squareBits[line
//                                                                        * 8 + col]) != 0)) {
//                    piece = EMPTY_SQUARE;
//                }
//                if ((piece == WHITE_KING)
//                        && ((extraKings[Player.SIDE_WHITE] & squareBits[line
//                                                                        * 8 + col]) != 0)) {
//                    piece = EMPTY_SQUARE;
//                }
//
//                // Show the piece
//                System.out.print("| " + pieceStrings[piece] + " ");
//            }
//            System.out.println("|");
//            System.out.println("|    |    |    |    |    |    |    |    |");
//        }
//        System.out.println("-----------------------------------------");
//        if (currentPlayer == Player.SIDE_BLACK) {
//            System.out.println("NEXT MOVE: BLACK ");
//        } else {
//            System.out.println("NEXT MOVE: WHITE");
//        }
//
//        return true;
//    }
//
//    // public int SwitchSides
//    // Change the identity of the player to move
//    public int switchSides() {
//        if (currentPlayer == Player.SIDE_WHITE) {
//            setCurrentPlayer(Player.SIDE_BLACK);
//        } else {
//            setCurrentPlayer(Player.SIDE_WHITE);
//        }
//
//        return currentPlayer;
//    }
//
//    // public int HashKey
//    // Compute a 32-bit integer to represent the board, according to Zobrist[70]
//    public int hashKey() {
//        int hash = 0;
//        // Look at all pieces, one at a time
//        for (int currPiece = 0; currPiece < ALL_PIECES; currPiece++) {
//            long tmp = bitBoards[currPiece];
//            // Search for all pieces on all squares. We could optimize here: not
//            // looking for pawns on the back row (or the eight row), getting out
//            // of the "currSqaure" loop once we found one king of one color,
//            // etc.
//            // But for simplicity's sake, we'll keep things generic.
//            for (int currSquare = 0; currSquare < ALL_SQUARES; currSquare++) {
//                // Zobrist's method: generate a bunch of random bitfields, each
//                // representing a certain "piece X is on square Y" predicate;
//                // XOR
//                // the bitfields associated with predicates which are true.
//                // Therefore, if we find a piece (in tmp) in a certain square,
//                // we accumulate the related HashKeyComponent.
//                if ((tmp & squareBits[currSquare]) != 0) {
//                    hash ^= hashKeyComponents[currPiece][currSquare];
//                }
//            }
//        }
//        return hash;
//    }
//
//    // public int HashLock
//    // Compute a second 32-bit hash key, using an entirely different set
//    // piece/square components.
//    // This is required to be able to detect hashing collisions without
//    // storing an entire jcBoard in each slot of the jcTranspositionTable,
//    // which would gobble up inordinate amounts of memory
//    public int hashLock() {
//        int hash = 0;
//        for (int currPiece = 0; currPiece < ALL_PIECES; currPiece++) {
//            long tmp = bitBoards[currPiece];
//            for (int currSquare = 0; currSquare < ALL_SQUARES; currSquare++) {
//                if ((tmp & squareBits[currSquare]) != 0) {
//                    hash ^= hashLockComponents[currPiece][currSquare];
//                }
//            }
//        }
//        return hash;
//    }
//
//    // public boolean ApplyMove
//    // Change the jcBoard's internal representation to reflect the move
//    // received as a parameter
//    public boolean applyMove(Move theMove) {
//        // If the move includes a pawn promotion, an extra step will be required
//        // at the end
//        boolean isPromotion = (theMove.moveType >= Move.MOVE_PROMOTION_KNIGHT);
//        int moveWithoutPromotion = (theMove.moveType & Move.NO_PROMOTION_MASK);
//        int side = theMove.movingPiece % 2;
//
//        // For now, ignore pawn promotions
//        switch (moveWithoutPromotion) {
//        case Move.MOVE_NORMAL:
//            // The simple case
//            removePiece(theMove.sourceSquare, theMove.movingPiece);
//            addPiece(theMove.destinationSquare, theMove.movingPiece);
//            break;
//        case Move.MOVE_CAPTURE_ORDINARY:
//            // Don't forget to remove the captured piece!
//            removePiece(theMove.sourceSquare, theMove.movingPiece);
//            removePiece(theMove.destinationSquare, theMove.capturedPiece);
//            addPiece(theMove.destinationSquare, theMove.movingPiece);
//            break;
//        case Move.MOVE_CAPTURE_EN_PASSANT:
//            // Here, we can use our knowledge of the board to make a small
//            // optimization, since the pawn to be captured is always
//            // "behind" the moving pawn's destination square, we can compute its
//            // position on the fly
//            removePiece(theMove.sourceSquare, theMove.movingPiece);
//            addPiece(theMove.destinationSquare, theMove.movingPiece);
//            if ((theMove.movingPiece % 2) == Player.SIDE_WHITE) {
//                removePiece(theMove.destinationSquare + 8,
//                        theMove.capturedPiece);
//            } else {
//                removePiece(theMove.destinationSquare - 8,
//                        theMove.capturedPiece);
//            }
//            break;
//        case Move.MOVE_CASTLING_QUEENSIDE:
//            // Again, we can compute the rook's source and destination squares
//            // because of our knowledge of the board's structure
//            removePiece(theMove.sourceSquare, theMove.movingPiece);
//            addPiece(theMove.destinationSquare, theMove.movingPiece);
//            int theRook = ROOK + (theMove.movingPiece % 2);
//            removePiece(theMove.sourceSquare - 4, theRook);
//            addPiece(theMove.sourceSquare - 1, theRook);
//            // We must now mark some squares as containing "phantom kings" so
//            // that
//            // the castling can be cancelled by the next opponent's move, if he
//            // can move to one of them
//            if (side == Player.SIDE_WHITE) {
//                setExtraKings(side, EXTRAKINGS_WHITE_QUEENSIDE);
//            } else {
//                setExtraKings(side, EXTRAKINGS_BLACK_QUEENSIDE);
//            }
//            hasCastled[side] = true;
//            break;
//        case Move.MOVE_CASTLING_KINGSIDE:
//            // Again, we can compute the rook's source and destination squares
//            // because of our knowledge of the board's structure
//            removePiece(theMove.sourceSquare, theMove.movingPiece);
//            addPiece(theMove.destinationSquare, theMove.movingPiece);
//            theRook = ROOK + (theMove.movingPiece % 2);
//            removePiece(theMove.sourceSquare + 3, theRook);
//            addPiece(theMove.sourceSquare + 1, theRook);
//            // We must now mark some squares as containing "phantom kings" so
//            // that
//            // the castling can be cancelled by the next opponent's move, if he
//            // can move to one of them
//            if (side == Player.SIDE_WHITE) {
//                setExtraKings(side, EXTRAKINGS_WHITE_KINGSIDE);
//            } else {
//                setExtraKings(side, EXTRAKINGS_BLACK_KINGSIDE);
//            }
//            hasCastled[side] = true;
//            break;
//        case Move.MOVE_RESIGN:
//            // FDL Later, ask the AI player who resigned to print the
//            // continuation
//            break;
//        case Move.MOVE_STALEMATE:
//            System.out.println("Stalemate - Game is a draw.");
//            break;
//        }
//
//        // And now, apply the promotion
//        if (isPromotion) {
//            int promotionType = (theMove.moveType & Move.PROMOTION_MASK);
//            int color = (theMove.movingPiece % 2);
//            switch (promotionType) {
//            case Move.MOVE_PROMOTION_KNIGHT:
//                removePiece(theMove.destinationSquare, theMove.movingPiece);
//                addPiece(theMove.destinationSquare, KNIGHT + color);
//                break;
//            case Move.MOVE_PROMOTION_BISHOP:
//                removePiece(theMove.destinationSquare, theMove.movingPiece);
//                addPiece(theMove.destinationSquare, BISHOP + color);
//                break;
//            case Move.MOVE_PROMOTION_ROOK:
//                removePiece(theMove.destinationSquare, theMove.movingPiece);
//                addPiece(theMove.destinationSquare, ROOK + color);
//                break;
//            case Move.MOVE_PROMOTION_QUEEN:
//                removePiece(theMove.destinationSquare, theMove.movingPiece);
//                addPiece(theMove.destinationSquare, QUEEN + color);
//                break;
//            }
//        }
//
//        // If this was a 2-step pawn move, we now have a valid en passant
//        // capture possibility. Otherwise, no.
//        if ((theMove.movingPiece == Board.WHITE_PAWN)
//                && (theMove.sourceSquare - theMove.destinationSquare == 16)) {
//            setEnPassantPawn(theMove.destinationSquare + 8);
//        } else if ((theMove.movingPiece == Board.BLACK_PAWN)
//                && (theMove.destinationSquare - theMove.sourceSquare == 16)) {
//            setEnPassantPawn(theMove.sourceSquare + 8);
//        } else {
//            clearEnPassantPawn();
//        }
//
//        // And now, maintain castling status
//        // If a king moves, castling becomes impossible for that side, for the
//        // rest of the game
//        switch (theMove.movingPiece) {
//        case WHITE_KING:
//            setCastlingStatus(CASTLE_KINGSIDE + Player.SIDE_WHITE, false);
//            setCastlingStatus(CASTLE_QUEENSIDE + Player.SIDE_WHITE, false);
//            break;
//        case BLACK_KING:
//            setCastlingStatus(CASTLE_KINGSIDE + Player.SIDE_BLACK, false);
//            setCastlingStatus(CASTLE_QUEENSIDE + Player.SIDE_BLACK, false);
//            break;
//        default:
//            break;
//        }
//
//        // Or, if ANYTHING moves from a corner, castling becomes impossible on
//        // that side (either because it's the rook that is moving, or because
//        // it has been captured by whatever moves, or because it is already
//        // gone)
//        switch (theMove.sourceSquare) {
//        case 0:
//            setCastlingStatus(CASTLE_QUEENSIDE + Player.SIDE_BLACK, false);
//            break;
//        case 7:
//            setCastlingStatus(CASTLE_KINGSIDE + Player.SIDE_BLACK, false);
//            break;
//        case 56:
//            setCastlingStatus(CASTLE_QUEENSIDE + Player.SIDE_WHITE, false);
//            break;
//        case 63:
//            setCastlingStatus(CASTLE_KINGSIDE + Player.SIDE_WHITE, false);
//            break;
//        default:
//            break;
//        }
//
//        // All that remains to do is switch sides
//        setCurrentPlayer((getCurrentPlayer() + 1) % 2);
//        return true;
//    }
//
//    // public boolean Load
//    // Load a board from a file
//    public boolean load(String fileName) throws Exception {
//        // Clean the board first
//        emptyBoard();
//
//        // Open the file as a Java tokenizer
//        FileReader fr = new FileReader(fileName);
//        StreamTokenizer tok = new StreamTokenizer(fr);
//        tok.eolIsSignificant(false);
//        tok.lowerCaseMode(false);
//
//        // Whose turn is it to play?
//        tok.nextToken();
//        if (tok.sval
//                .equalsIgnoreCase(Player.PlayerStrings[Player.SIDE_WHITE])) {
//            setCurrentPlayer(Player.SIDE_WHITE);
//        } else {
//            setCurrentPlayer(Player.SIDE_BLACK);
//        }
//
//        // Read the positions of all the pieces
//        // First, look for the number of pieces on the board
//        tok.nextToken();
//        int numPieces = (int) tok.nval;
//
//        // Now, loop on the pieces in question
//        for (int i = 0; i < numPieces; i++) {
//            // What kind of piece is this, and where does it go?
//            tok.nextToken();
//            String whichPieceStr = tok.sval;
//
//            int whichPiece = 0;
//            while (!whichPieceStr.equalsIgnoreCase(pieceStrings[whichPiece])) {
//                whichPiece++;
//            }
//
//            tok.nextToken();
//            int whichSquare = (int) tok.nval;
//
//            // Add the piece to the board
//            addPiece(whichSquare, whichPiece);
//        }
//
//        // Now, read the castling status flags
//        for (int i = 0; i < 4; i++) {
//            tok.nextToken();
//            if ("TRUE".equalsIgnoreCase(tok.sval)) {
//                setCastlingStatus(i, true);
//            } else {
//                setCastlingStatus(i, false);
//            }
//        }
//
//        // And finally, read the bitboard representing the position of the en
//        // passant pawn, if any
//        tok.nextToken();
//        setEnPassantPawn((long) tok.nval);
//
//        fr.close();
//        return true;
//    }
//
//    // public boolean Save
//    // Save the state of the game to a file
//    public boolean save(String fileName) throws Exception {
//        // Open the file for business
//        FileWriter fr = new FileWriter(fileName);
//        BufferedWriter bw = new BufferedWriter(fr);
//
//        // Whose turn is it?
//        bw.write(Player.PlayerStrings[currentPlayer]);
//        bw.newLine();
//
//        // Count the pieces on the board
//        int numPieces = 0;
//        for (int i = 0; i < ALL_SQUARES; i++) {
//            if ((squareBits[i] & bitBoards[ALL_WHITE_PIECES]) != 0) {
//                numPieces++;
//            }
//            if ((squareBits[i] & bitBoards[ALL_BLACK_PIECES]) != 0) {
//                numPieces++;
//            }
//        }
//        bw.write(String.valueOf(numPieces));
//        bw.newLine();
//
//        // Dump the pieces, one by one
//        for (int piece = 0; piece < ALL_PIECES; piece++) {
//            for (int square = 0; square < ALL_SQUARES; square++) {
//                if ((bitBoards[piece] & squareBits[square]) != 0) {
//                    bw.write(pieceStrings[piece] + " " + String.valueOf(square));
//                    bw.newLine();
//                }
//            }
//        }
//
//        // And finally, dump the castling status and the en passant pawn
//        for (int i = 0; i < 4; i++) {
//            if (castlingStatus[i]) {
//                bw.write("TRUE");
//            } else {
//                bw.write("FALSE");
//            }
//            bw.newLine();
//        }
//
//        bw.write(String.valueOf(enPassantPawn));
//
//        bw.close();
//        return true;
//    }
//
//    // public int EvalMaterial
//    // Compute the board's material balance, from the point of view of the
//    // "side"
//    // player. This is an exact clone of the eval function in CHESS 4.5
//    public int evalMaterial(int side) {
//        // If both sides are equal, no need to compute anything!
//        if (materialValue[Player.SIDE_BLACK] == materialValue[Player.SIDE_WHITE]) {
//            return 0;
//        }
//
//        int otherSide = (side + 1) % 2;
//        int matTotal = materialValue[side] + materialValue[otherSide];
//
//        // Who is leading the game, material-wise?
//        if (materialValue[Player.SIDE_BLACK] > materialValue[Player.SIDE_WHITE]) {
//            // Black leading
//            int matDiff = materialValue[Player.SIDE_BLACK]
//                    - materialValue[Player.SIDE_WHITE];
//            int val = Math.min(2400, matDiff)
//                    + (matDiff * (12000 - matTotal) * numPawns[Player.SIDE_BLACK])
//                    / (6400 * (numPawns[Player.SIDE_BLACK] + 1));
//            if (side == Player.SIDE_BLACK) {
//                return val;
//            } else {
//                return -val;
//            }
//        } else {
//            // White leading
//            int matDiff = materialValue[Player.SIDE_WHITE]
//                    - materialValue[Player.SIDE_BLACK];
//            int val = Math.min(2400, matDiff)
//                    + (matDiff * (12000 - matTotal) * numPawns[Player.SIDE_WHITE])
//                    / (6400 * (numPawns[Player.SIDE_WHITE] + 1));
//
//            if (side == Player.SIDE_WHITE) {
//                return val;
//            } else {
//                return -val;
//            }
//        }
//    }
//
//    // public boolean StartingBoard
//    // Restore the board to a game-start position
//    public boolean startingBoard() {
//        // Put the pieces on the board
//        emptyBoard();
//        addPiece(0, BLACK_ROOK);
//        addPiece(1, BLACK_KNIGHT);
//        addPiece(2, BLACK_BISHOP);
//        addPiece(3, BLACK_QUEEN);
//        addPiece(4, BLACK_KING);
//        addPiece(5, BLACK_BISHOP);
//        addPiece(6, BLACK_KNIGHT);
//        addPiece(7, BLACK_ROOK);
//        for (int i = 8; i < 16; i++) {
//            addPiece(i, BLACK_PAWN);
//        }
//
//        for (int i = 48; i < 56; i++) {
//            addPiece(i, WHITE_PAWN);
//        }
//        addPiece(56, WHITE_ROOK);
//        addPiece(57, WHITE_KNIGHT);
//        addPiece(58, WHITE_BISHOP);
//        addPiece(59, WHITE_QUEEN);
//        addPiece(60, WHITE_KING);
//        addPiece(61, WHITE_BISHOP);
//        addPiece(62, WHITE_KNIGHT);
//        addPiece(63, WHITE_ROOK);
//
//        // And allow all castling moves
//        for (int i = 0; i < 4; i++) {
//            castlingStatus[i] = true;
//        }
//        hasCastled[0] = false;
//        hasCastled[1] = false;
//        clearEnPassantPawn();
//
//        // And ask White to play the first move
//        setCurrentPlayer(Player.SIDE_WHITE);
//        return true;
//    }
//
//    /******************************************************************************
//     * PRIVATE METHODS
//     *****************************************************************************/
//
//    // private boolean AddPiece
//    // Place a specific piece on a specific board square
//    private boolean addPiece(int whichSquare, int whichPiece) {
//        // Add the piece itself
//        bitBoards[whichPiece] |= squareBits[whichSquare];
//
//        // And note the new piece position in the bitboard containing all
//        // pieces of its color. Here, we take advantage of the fact that
//        // all pieces of a given color are represented by numbers of the same
//        // parity
//        bitBoards[ALL_PIECES + (whichPiece % 2)] |= squareBits[whichSquare];
//
//        // And adjust material balance accordingly
//        materialValue[whichPiece % 2] += pieceValues[whichPiece];
//        if (whichPiece == WHITE_PAWN) {
//            numPawns[Player.SIDE_WHITE]++;
//        } else if (whichPiece == BLACK_PAWN) {
//            numPawns[Player.SIDE_BLACK]++;
//        }
//
//        return true;
//    }
//
//    // private boolean RemovePiece
//    // Eliminate a specific piece from a specific square on the board
//    // Note that you MUST know that the piece is there before calling this,
//    // or the results will not be what you expect!
//    private boolean removePiece(int whichSquare, int whichPiece) {
//        // Remove the piece itself
//        bitBoards[whichPiece] ^= squareBits[whichSquare];
//        bitBoards[ALL_PIECES + (whichPiece % 2)] ^= squareBits[whichSquare];
//
//        // And adjust material balance accordingly
//        materialValue[whichPiece % 2] -= pieceValues[whichPiece];
//        if (whichPiece == WHITE_PAWN) {
//            numPawns[Player.SIDE_WHITE]--;
//        } else if (whichPiece == BLACK_PAWN) {
//            numPawns[Player.SIDE_BLACK]--;
//        }
//        return true;
//    }
//
//    // private boolean EmptyBoard
//    // Remove every piece from the board
//    private boolean emptyBoard() {
//        for (int i = 0; i < ALL_BITBOARDS; i++) {
//            bitBoards[i] = 0;
//        }
//        extraKings[0] = 0;
//        extraKings[1] = 0;
//        enPassantPawn = 0;
//        materialValue[0] = 0;
//        materialValue[1] = 0;
//        numPawns[0] = 0;
//        numPawns[1] = 0;
//        return true;
//    }
//
//    // private boolean SetCastlingStatus
//    // Change one of the "castling status" flags
//    // parameter whichFlag should be a sum of a side marker and a castling
//    // move identifier, for example, jcPlayer.SIDE_WHITE + CASTLE_QUEENSIDE
//    private boolean setCastlingStatus(int whichFlag, boolean newValue) {
//        castlingStatus[whichFlag] = newValue;
//        return true;
//    }
//
//    // private boolean SetEnPassantPawn
//    // If a pawn move has just made en passant capture possible, mark it as
//    // such in a bitboard (containing the en passant square only)
//    private boolean setEnPassantPawn(int square) {
//        clearEnPassantPawn();
//        enPassantPawn |= squareBits[square];
//        return true;
//    }
//
//    private boolean setEnPassantPawn(long bitboard) {
//        enPassantPawn = bitboard;
//        return true;
//    }
//
//    // private boolean ClearEnPassantPawn
//    // Indicates that there is no en passant square at all. Technically, this
//    // job could have been handled by SetEnPassaantPawn( long ) with a null
//    // parameter, but I have chosen to add a method to avoid problems if I ever
//    // forgot to specify 0L: using 0 would call the first form of the Set method
//    // and indicate an en passant pawn in a corner of the board, with possibly
//    // disastrous consequences!
//    private boolean clearEnPassantPawn() {
//        enPassantPawn = 0;
//        return true;
//    }
//
//    // private boolean SetCurrentPlayer
//    // Whose turn is it?
//    private boolean setCurrentPlayer(int which) {
//        currentPlayer = which;
//        return true;
//    }
//}