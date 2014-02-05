/******************************************************************************
 * jcMoveListGenerator.java - Find all pseudo-legal moves given a board state
 * by F.D. Laram�e
 *
 * Purpose: Identify a list of possible moves
 *
 * History:
 * 27.07.00 Creation
 *****************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.optimization.moves;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;


public interface RulesChecker<T extends Move<?>, U extends GameBoard<T>> {

    public Array<T> generateLegalMoves(U board, int pieceType);

    public boolean gameOver();

}