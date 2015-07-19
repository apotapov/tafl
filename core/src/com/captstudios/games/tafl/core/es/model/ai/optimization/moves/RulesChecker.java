/******************************************************************************
 * jcMoveListGenerator.java - Find all pseudo-legal moves given a board state
 * by F.D. Laram�e
 *
 * Purpose: Identify a list of possible moves
 *
 * History:
 * 27.07.00 Creation
 *****************************************************************************/

package com.captstudios.games.tafl.core.es.model.ai.optimization.moves;

import com.badlogic.gdx.utils.Array;


public interface RulesChecker {

    public Array<Move> generateLegalMoves(int team);
    public void freeMoves(Array<Move> moves);

    public boolean isGameOver(int team);

}