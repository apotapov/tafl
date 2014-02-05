///***************************************************************************
// * jcGame.java - The JavaChess game controller
// * by Fran�ois Dominic Laram�e
// *
// * Purpose: Coordinate the efforts of all other game-related objects.  This
// * work has been separated from the jcApp (application-level object) because
// * the latter may come to take on more message-passing and related duties
// * if I ever add a GUI to the game, and I didn't want interface and game
// * mechanics to get mixed up in a single class.
// *
// * History
// * 08.06.00 Created
// **************************************************************************/
//package com.pactstudios.games.tafl.core.es.model.ai.chess.game;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import com.pactstudios.games.tafl.core.es.model.ai.chess.Board;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.game.player.Player;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.game.player.PlayerAI;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.game.player.PlayerHuman;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.move.Move;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.move.OpeningBook;
//import com.pactstudios.games.tafl.core.es.model.ai.chess.search.AISearchAgent;
//
///**************************************************************************
// * public class jcGame
// *************************************************************************/
//
//public class ChessGame {
//    // The two players involved in the current game
//    Player players[];
//
//    // The state of the game
//    Board gameBoard;
//
//    // The opening book
//    OpeningBook openings;
//
//    // A wrapper for the keyboard
//    InputStreamReader kbd;
//
//    // Constructor
//    public ChessGame() {
//    }
//
//    // boolean InitializeGame()
//    // Select the players, create subsidiary objects and prepare to play
//    public boolean initializeGame(String openingBook, String startingPos)
//            throws Exception {
//        // Read the opening book
//        openings = new OpeningBook();
//        openings.load(openingBook);
//
//        // Load the initial position, if any
//        gameBoard = new Board();
//        if (startingPos.equalsIgnoreCase("NONE")) {
//            gameBoard.startingBoard();
//        } else {
//            gameBoard.load(startingPos);
//        }
//
//        // Initialize the keyboard
//        kbd = new InputStreamReader(System.in);
//        int key;
//
//        // Identify the two players
//        players = new Player[2];
//        key = 'C';
//        System.out
//        .println("Welcome to Java Chess.  Who plays white: [H]uman or [C]omputer? ");
//        try {
//            do {
//                key = kbd.read();
//            } while ((key != 'H') && (key != 'h') && (key != 'c')
//                    && (key != 'C'));
//        } catch (IOException e) {
//        }
//
//        if ((key == 'H') || (key == 'h')) {
//            players[Player.SIDE_WHITE] = new PlayerHuman(
//                    Player.SIDE_WHITE, kbd);
//        } else {
//            players[Player.SIDE_WHITE] = new PlayerAI(Player.SIDE_WHITE,
//                    AISearchAgent.AISEARCH_MTDF, openings);
//        }
//
//        System.out.println("And who plays black: [H]uman or [C]omputer? ");
//        try {
//            do {
//                key = kbd.read();
//            } while ((key != 'H') && (key != 'h') && (key != 'c')
//                    && (key != 'C'));
//        } catch (IOException e) {
//        }
//
//        if ((key == 'H') || (key == 'h')) {
//            players[Player.SIDE_BLACK] = new PlayerHuman(
//                    Player.SIDE_BLACK, kbd);
//        } else {
//            players[Player.SIDE_BLACK] = new PlayerAI(Player.SIDE_BLACK,
//                    AISearchAgent.AISEARCH_MTDF, openings);
//        }
//
//        return true;
//    }
//
//    // boolean RunGame()
//    // A simple loop getting moves from the current player until the game is
//    // over
//    public boolean runGame() throws Exception {
//        Player currentPlayer;
//        Move move;
//
//        do {
//            // Show the current game board
//            gameBoard.print();
//
//            // Ask the next player for a move
//            currentPlayer = players[gameBoard.getCurrentPlayer()];
//            move = currentPlayer.getMove(gameBoard);
//            System.out.print(Player.PlayerStrings[gameBoard
//                                                  .getCurrentPlayer()]);
//            System.out.print(" selects move: ");
//            move.print();
//
//            // Change the state of the game accordingly
//            gameBoard.applyMove(move);
//
//            // Pause
//            Thread.sleep(2000);
//
//        } while ((move.moveType != Move.MOVE_RESIGN)
//                && (move.moveType != Move.MOVE_STALEMATE));
//
//        System.out.println("Game Over.  Thanks for playing!");
//
//        return true;
//    }
//}