/******************************************************************************
 * jcMoveListGenerator.java - Find all pseudo-legal moves given a board state
 * by F.D. Laram�e
 *
 * Purpose: Identify a list of possible moves
 *
 * History:
 * 27.07.00 Creation
 *****************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.moves;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.Move;
import com.pactstudios.games.tafl.core.es.model.board.GameBitBoard;

public class MoveListGenerator {

    // The list of moves, implemented as a java collection class, namely the
    Array<Move> moves;
    int boardSize;
    int pieceTypes;

    public MoveListGenerator(int boardSize, int pieceTypes) {
        this.boardSize = boardSize;
        this.pieceTypes = pieceTypes;

        moves = new Array<Move>();
    }

    /**
     * Look for a specific move in the list; if it is there, return true
     * 
     * @param move
     * @return
     */
    public boolean find(Move move) {
        for (Move testMove : moves) {
            if (testMove.equals(move)) {
                return true;
            }
        }
        return false;
    }

    /**
     * look for a move from "source" to "dest" in the list
     * 
     * @param source
     * @param dest
     * @return
     */
    public Move findMoveForSquares(int source, int dest) {
        for (Move testMove : moves) {
            if (testMove.source == source &&
                    testMove.destination == dest) {
                return testMove;
            }
        }
        return null;
    }


    /**
     * Look at the board received as a parameter, and build a list of legal
     * moves which can be derived from it. If there are no legal moves, or if
     * one of the moves is a king capture (which means that the opponent's
     * previous move left the king in check, which is illegal), return false.
     * 
     * @param board
     * @param teamId
     * @return
     */
    public Array<Move> computeLegalMoves(GameBitBoard board, int pieceType) {
        // First, clean up the old list of moves, if any
        moves.clear();

        computeMoves(board, pieceType);
        return moves;
    }

    private void computeMoves(GameBitBoard board, int pieceType) {
        // Fetch the bitboard containing positions of these pieces
        BitSet pieces = board.bitBoards[pieceType];

        // If there are no pieces of this type, no need to work very hard!
        if (!pieces.isEmpty()) {
            // This is a black piece, so let's start looking at the top
            // of the board
            for (int square = 0; square < 64; square++) {
                if (pieces.get(square)) {
                    // There is a piece here; find its moves
                    for (int ray = 0; ray < GeneratedMoves.ROOK_MOVES[square].length; ray++) {
                        for (int i = 0; i < GeneratedMoves.ROOK_MOVES[square][ray].length; i++) {
                            // Get the destination square
                            int dest = GeneratedMoves.ROOK_MOVES[square][ray][i];

                            // Is it occupied by a friendly piece? If so, can't move
                            // there
                            // AND we must discontinue the current ray
                            if (!board.bitBoards[pieceTypes].get(dest) &&
                                    !board.bitBoards[pieceTypes + 1].get(dest)) {
                                break;
                            }

                            // Otherwise, the move is legal, so we must prepare to
                            // add it
                            Move mov = new Move();
                            mov.source = square;
                            mov.destination = dest;
                            mov.pieceType = pieceType;
                            moves.add(mov);
                        }
                    }
                }
            }
        }
    }
}