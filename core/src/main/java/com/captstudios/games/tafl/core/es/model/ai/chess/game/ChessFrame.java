///***************************************************************************
// * jcFrame.java - GUI for JavaChess
// * by F.D. Laram�e
// *
// * Purpose: Sometime in the very distant future, I may graft a true GUI onto
// * this game (i.e., drag-and-drop pieces to move, etc.)  In the meantime, this
// * class will only contain the absolute bare minimum functionality required
// * by Java: an empty window with a "close box" allowing quick exit.
// ***************************************************************************/
//
//package com.captstudios.games.tafl.core.es.model.ai.chess.game;
//
//import java.awt.AWTEvent;
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.event.WindowEvent;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
///****************************************************************************
// * public class jcFrame
// ***************************************************************************/
//
//public class ChessFrame extends JFrame {
//    /**
//     *
//     */
//    private static final long serialVersionUID = -3778444901879958917L;
//    // GUI data members
//    JPanel contentPane;
//    BorderLayout borderLayout = new BorderLayout();
//
//    // Constructor
//    public ChessFrame() {
//        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
//        try {
//            jbInit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // GUI Component initialization
//    private void jbInit() throws Exception {
//        contentPane = (JPanel) this.getContentPane();
//        contentPane.setLayout(borderLayout);
//        this.setSize(new Dimension(400, 300));
//        this.setTitle("Java Chess 1.0");
//    }
//
//    /**************************************************************************
//     * Event handlers
//     *************************************************************************/
//
//    // processWindowEvent: Overridden so we can exit when window is closed
//    @Override
//    protected void processWindowEvent(WindowEvent e) {
//        super.processWindowEvent(e);
//        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//            System.exit(0);
//        }
//    }
//}