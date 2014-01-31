/******************************************************************************
 * jcApp.java - The JavaChess main program
 * by F.D. Laram�e
 *
 * Purpose: Entry point, and not much else!
 *
 * History:
 * 07.06.00 Initial build.
 *****************************************************************************/

package com.pactstudios.games.tafl.core.es.model.ai.chess.game;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

/*****************************************************************************
 * public class jcApp The application-level class, surrounding everything else.
 * 
 * Most of this code has been auto-generated by JBuilder; my role was limited to
 * re-formatting it to make it legible, and to add the jcGame calls at the end
 * of the main program.
 ****************************************************************************/

public class ChessApp {
    // Constructor
    public ChessApp() {
        // Make the window, since Java needs one
        // We won't be making much use of it, though; all of the i/o
        // will pass through the console
        ChessFrame frame = new ChessFrame();
        frame.validate();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    // Main method
    // Initialize and launch the jcGame object
    public static void main(String[] args) {
        // Extract the parameters
        String openingBook = args[0];
        String startingPos = "NONE";
        if (args.length > 1) {
            startingPos = args[1];
        }

        // Make the application
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ChessApp();

        // Initialize the game controller
        ChessGame theGame = new ChessGame();
        try {
            theGame.initializeGame(openingBook, startingPos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the game
        try {
            theGame.runGame();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}