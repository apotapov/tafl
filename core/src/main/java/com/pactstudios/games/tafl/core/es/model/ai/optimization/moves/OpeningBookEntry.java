package com.pactstudios.games.tafl.core.es.model.ai.optimization.moves;



/****************************************************************************
 * A signature for a board position, and the best moves for each team.
 ***************************************************************************/

class OpeningBookEntry<T extends Move<?>> {
    // A signature for the board position stored in the entry
    int haskKey;
    int hashLock;

    T moveTeam1;
    T moveTeam2;
}
