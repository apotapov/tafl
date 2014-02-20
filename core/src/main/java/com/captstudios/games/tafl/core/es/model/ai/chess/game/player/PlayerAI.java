///**************************************************************************
// *
// * jcPlayerAI.java - Interface to a computer player
// * by Fran�ois Dominic Laram�e
// *
// * Purpose: This object allows a computer player to play JavaChess.  Its only
// * real job is to query an AI Search Agent for his move.
// *
// * History:
// * 11.06.00 Creation
// * 07.08.00 Association with the search agent
// *
// **************************************************************************/
//
//package com.captstudios.games.tafl.core.es.model.ai.chess.game.player;
//
//import com.captstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.Move;
//import com.captstudios.games.tafl.core.es.model.ai.chess.move.OpeningBook;
//import com.captstudios.games.tafl.core.es.model.ai.chess.search.AISearchAgent;
//
//public class PlayerAI extends Player {
//    /************************************************************************
//     * DATA MEMBERS
//     ***********************************************************************/
//
//    // The search agent in charge of the moves
//    AISearchAgent agent;
//
//    /***********************************************************************
//     * PUBLIC METHODS
//     **********************************************************************/
//
//    // Constructor
//    public PlayerAI(int whichPlayer, int whichType, OpeningBook ref) {
//        this.setSide(whichPlayer);
//        agent = AISearchAgent.makeNewAgent(whichType, ref);
//    }
//
//    // Attach a search agent to the AI player
//    public boolean attachSearchAgent(AISearchAgent theAgent) {
//        agent = theAgent;
//        return true;
//    }
//
//    // Getting a move from the machine
//    @Override
//    public Move getMove(Board theBoard) {
//        return (agent.pickBestMove(theBoard));
//    }
//}