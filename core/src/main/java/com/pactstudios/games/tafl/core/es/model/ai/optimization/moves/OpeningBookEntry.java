package com.pactstudios.games.tafl.core.es.model.ai.optimization.moves;



/****************************************************************************
 * A signature for a board position, and the best moves for each team.
 ***************************************************************************/

class OpeningBookEntry {
    // A signature for the board position stored in the entry
    int haskKey;
    int hashLock;

    Move moveTeam1;
    Move moveTeam2;
}
