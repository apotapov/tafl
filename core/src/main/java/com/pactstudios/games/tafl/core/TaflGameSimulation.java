package com.pactstudios.games.tafl.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.RulesEngineType;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class TaflGameSimulation {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {


        PrintStream out = new PrintStream(new FileOutputStream("/Users/apotapov/tafl_output.txt"));
        System.setOut(out);

        int whiteWin = 0;
        int blackWin = 0;
        int draw = 0;
        int whiteSurrenders = 0;
        int blackSurrenders = 0;

        Array<DrawReasonEnum> drawReasons = new Array<DrawReasonEnum>();


        FloatCounter whiteTimeCounter = new FloatCounter(0);
        FloatCounter whiteMoveCounter = new FloatCounter(0);
        FloatCounter whiteWhitePieceCounter = new FloatCounter(0);
        FloatCounter whiteBlackPieceCounter = new FloatCounter(0);

        FloatCounter blackTimeCounter = new FloatCounter(0);
        FloatCounter blackMoveCounter = new FloatCounter(0);
        FloatCounter blackWhitePieceCounter = new FloatCounter(0);
        FloatCounter blackBlackPieceCounter = new FloatCounter(0);

        IntArray kingPositions = new IntArray();
        FloatCounter kingMoves = new FloatCounter(0);

        for (int game = 0; game < 100; game++) {
            long start = System.currentTimeMillis();

            TaflMatch match = new TaflMatch();
            match.created = System.currentTimeMillis();
            match.updated = match.created;
            match.name = "test";
            match.status = LifeCycle.PLAY;
            match.rulesType = RulesEngineType.OFFICIAL;
            match.versusComputer = true;
            match.computerStarts = true;
            match.aiType = AiType.AI_ADVANCED;
            match.boardRepresentation = "...BBBBB........B................B....W....BB...WWW...BBB.WWKWW.BBB...WWW...BB....W....B................B........BBBBB...";

            match.initialize();

            int count = 0;
            boolean surrender = true;
            while(!match.board.rules.isGameOver(0)) {
                Move move = match.aiStrategy.search(match);
                if (move != null) {
                    if (move.source == match.board.king) {
                        int distance = Math.abs(move.destination - move.source);
                        if ( distance >= match.board.dimensions) {
                            distance /= match.board.dimensions;
                        }
                        kingMoves.put(distance);
                    }
                    match.applyMove(move, false);
                    BitBoard captures = match.board.rules.getCapturedPieces(move);
                    if (captures.capacity() > 0) {
                        match.removePieces((match.turn + 1) % 2, captures);
                    }
                    match.changeTurn();
                    if (Constants.GameConstants.DEBUG) {
                        System.out.println("Move #: " + count++);
                        System.out.println("White pieces: " + match.board.bitBoards[0].cardinality());
                        System.out.println("Black pieces: " + match.board.bitBoards[1].cardinality());
                        System.out.println("King: " + match.board.king);
                    }
                } else {
                    if (match.board.rules.checkWinner() == -1) {
                        surrender = true;
                    }
                    break;
                }
            }

            float time = ((System.currentTimeMillis() - start) / 1000.0f);

            int winner;
            if (surrender) {
                winner = (match.turn + 1) % 2;
            } else {
                winner = match.board.rules.checkWinner();
            }
            if (winner == 0) {
                whiteWin++;
                whiteTimeCounter.put(time);
                whiteMoveCounter.put(match.board.undoStack.size);
                whiteWhitePieceCounter.put(match.board.whiteBitBoard().cardinality());
                whiteBlackPieceCounter.put(match.board.blackBitBoard().cardinality());
                if (surrender) {
                    blackSurrenders++;
                }
            } else if (winner == 1) {
                blackWin++;
                blackTimeCounter.put(time);
                blackMoveCounter.put(match.board.undoStack.size);
                blackWhitePieceCounter.put(match.board.whiteBitBoard().cardinality());
                blackBlackPieceCounter.put(match.board.blackBitBoard().cardinality());

                if (surrender) {
                    whiteSurrenders++;
                }
            } else {
                draw++;
                drawReasons.add(match.board.rules.checkDraw(match.turn));
            }

            System.out.println("Game #" + (game + 1));
            System.out.println("Game lasted: " + time);
            System.out.println("Number of moves: " + match.board.undoStack.size);
            if (surrender) {
                System.out.println("Surrender: " + match.turn);
            }

            if (winner == 0) {
                System.out.println("Winner is: white");
            } else if (winner == 1) {
                System.out.println("Winner is: black");
            } else {
                System.out.println("Draw: " + match.board.rules.checkDraw((match.turn + 1) % 2));
            }

            System.out.println("King moves: " + toString(kingMoves));
            kingMoves.reset();

            if (winner == 1) {
                match.board.king = match.board.capturedKing;
            }

            kingPositions.add(match.board.king);

            String boardString = match.board.toString();

            for (int i = 0; i < match.board.dimensions; i++) {
                System.out.println(boardString.substring(i * match.board.dimensions, i * match.board.dimensions + match.board.dimensions));
            }
            System.out.println();
        }

        System.out.println("Draw: " + draw);
        System.out.println("Draw reasons: " + drawReasons);
        System.out.println();
        System.out.println("White wins: " + whiteWin);
        System.out.println("White Surrenders: " + whiteSurrenders);
        System.out.println("White wins time: " + toString(whiteTimeCounter));
        System.out.println("White moves counter: " + toString(whiteMoveCounter));
        System.out.println("White win, white pieces left: " + toString(whiteWhitePieceCounter));
        System.out.println("White win, black pieces left: " + toString(whiteBlackPieceCounter));
        System.out.println();
        System.out.println("Black wins: " + blackWin);
        System.out.println("Black Surrenders: " + blackSurrenders);
        System.out.println("Black wins time: " + toString(blackTimeCounter));
        System.out.println("Black moves counter: " + toString(blackMoveCounter));
        System.out.println("Black win, white pieces left: " + toString(blackWhitePieceCounter));
        System.out.println("Black win, black pieces left: " + toString(blackBlackPieceCounter));
        System.out.println();
        System.out.println("King positions: " + kingPositions);
    }

    private static String toString(FloatCounter counter) {
        StringBuilder sb = new StringBuilder();
        sb.append("{count = " + counter.count);
        sb.append(", total = " + counter.total);
        sb.append(", min = " + counter.min);
        sb.append(", max = " + counter.max);
        sb.append(", average = " + counter.average);
        sb.append(", value = " + counter.value);
        sb.append("}");
        return sb.toString();
    }

}
