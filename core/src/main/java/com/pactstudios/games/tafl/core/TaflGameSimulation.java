package com.pactstudios.games.tafl.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.RulesEngineType;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class TaflGameSimulation {

    private static final int NUMBER_OF_GAMES = 20;

    public static void main(String[] args) throws InterruptedException, IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/apotapov/tafl_output.txt"));

        int whiteWin = 0;
        int blackWin = 0;
        int draw = 0;

        Array<DrawReasonEnum> drawReasons = new Array<DrawReasonEnum>();

        FloatCounter drawTimeCounter = new FloatCounter(0);
        FloatCounter drawMoveCounter = new FloatCounter(0);
        FloatCounter drawWhitePieceCounter = new FloatCounter(0);
        FloatCounter drawBlackPieceCounter = new FloatCounter(0);

        FloatCounter whiteTimeCounter = new FloatCounter(0);
        FloatCounter whiteMoveCounter = new FloatCounter(0);
        FloatCounter whiteWhitePieceCounter = new FloatCounter(0);
        FloatCounter whiteBlackPieceCounter = new FloatCounter(0);

        FloatCounter blackTimeCounter = new FloatCounter(0);
        FloatCounter blackMoveCounter = new FloatCounter(0);
        FloatCounter blackWhitePieceCounter = new FloatCounter(0);
        FloatCounter blackBlackPieceCounter = new FloatCounter(0);

        FloatCounter kingMoves = new FloatCounter(0);

        for (int game = 0; game < NUMBER_OF_GAMES; game++) {
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
            boolean noMoves = true;
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
                        bw.write("\nMove #: " + count++);
                        bw.write("\nMove: " + move);
                        bw.write("\nWhite pieces: " + match.board.bitBoards[0].cardinality());
                        bw.write("\nBlack pieces: " + match.board.bitBoards[1].cardinality());
                        bw.write("\nKing: " + match.board.king);
                        String boardString = match.board.toString();

                        for (int i = 0; i < match.board.dimensions; i++) {
                            bw.write(boardString.substring(i * match.board.dimensions, i * match.board.dimensions + match.board.dimensions));
                        }
                        bw.write("\n\n");
                    }
                } else {
                    noMoves = true;
                    break;
                }
                bw.flush();
            }

            float time = ((System.currentTimeMillis() - start) / 1000.0f);

            int winner;
            if (noMoves) {
                if (match.board.rules.checkDraw((match.turn + 1)) != null) {
                    winner = -1;
                } else {
                    // surrender
                    winner = (match.turn + 1) % 2;
                }
            } else {
                winner = match.board.rules.checkWinner();
            }

            bw.write("\nGame #" + (game + 1));
            bw.write("\nGame lasted: " + time);
            bw.write("\nNumber of moves: " + match.board.undoStack.size);


            if (winner == 0) {
                bw.write("\nWinner is: white");
                whiteWin++;
                whiteTimeCounter.put(time);
                whiteMoveCounter.put(match.board.undoStack.size);
                whiteWhitePieceCounter.put(match.board.whiteBitBoard().cardinality());
                whiteBlackPieceCounter.put(match.board.blackBitBoard().cardinality());
            } else if (winner == 1) {
                bw.write("\nWinner is: black");
                blackWin++;
                blackTimeCounter.put(time);
                blackMoveCounter.put(match.board.undoStack.size);
                blackWhitePieceCounter.put(match.board.whiteBitBoard().cardinality());
                blackBlackPieceCounter.put(match.board.blackBitBoard().cardinality());
            } else {
                DrawReasonEnum reason = match.board.rules.checkDraw((match.turn + 1) % 2);
                bw.write("\nDraw: " + reason);
                draw++;
                drawReasons.add(reason);
                drawTimeCounter.put(time);
                drawMoveCounter.put(match.board.undoStack.size);
                drawWhitePieceCounter.put(match.board.whiteBitBoard().cardinality());
                drawBlackPieceCounter.put(match.board.blackBitBoard().cardinality());
            }

            bw.write("\nKing moves: " + toString(kingMoves));
            kingMoves.reset();

            if (winner == 1 && match.board.king == -1) {
                match.board.king = match.board.capturedKing;
            }

            String boardString = match.board.toString();

            for (int i = 0; i < match.board.dimensions; i++) {
                bw.write("\n" + boardString.substring(i * match.board.dimensions, i * match.board.dimensions + match.board.dimensions));
            }
            bw.write("\n");
        }

        bw.write("\nDraw: " + draw);
        bw.write("\nDraw reasons: " + drawReasons);
        bw.write("\nDraw wins time: " + toString(drawTimeCounter));
        bw.write("\nDraw moves counter: " + toString(drawMoveCounter));
        bw.write("\nDraw win, white pieces left: " + toString(drawWhitePieceCounter));
        bw.write("\nDraw win, black pieces left: " + toString(drawBlackPieceCounter));
        bw.write("\n");
        bw.write("\nWhite wins: " + whiteWin);
        bw.write("\nWhite wins time: " + toString(whiteTimeCounter));
        bw.write("\nWhite moves counter: " + toString(whiteMoveCounter));
        bw.write("\nWhite win, white pieces left: " + toString(whiteWhitePieceCounter));
        bw.write("\nWhite win, black pieces left: " + toString(whiteBlackPieceCounter));
        bw.write("\n");
        bw.write("\nBlack wins: " + blackWin);
        bw.write("\nBlack wins time: " + toString(blackTimeCounter));
        bw.write("\nBlack moves counter: " + toString(blackMoveCounter));
        bw.write("\nBlack win, white pieces left: " + toString(blackWhitePieceCounter));
        bw.write("\nBlack win, black pieces left: " + toString(blackBlackPieceCounter));
        bw.write("\n");
        bw.flush();
        bw.close();
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
