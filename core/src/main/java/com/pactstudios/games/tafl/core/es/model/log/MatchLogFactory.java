package com.pactstudios.games.tafl.core.es.model.log;

import java.util.Date;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;

public class MatchLogFactory {

    public static MatchLogEntry log(TaflMatch match, Move move) {
        MatchLogEntry entry = new MatchLogEntry();
        entry.match = match;
        entry.team = move.pieceType;
        entry.source = move.source;
        entry.destination = move.destination;
        entry.updated = new Date();

        entry.capture1 = move.capturedPieces.nextSetBit(0);
        entry.capture2 = move.capturedPieces.nextSetBit(entry.capture1 + 1);
        entry.capture3 = move.capturedPieces.nextSetBit(entry.capture2 + 1);

        return entry;
    }

    public static Move parseLog(MatchLogEntry entry, TaflMatch match) {
        Move move = new Move();

        move.pieceType = entry.team;
        move.source = entry.source;
        move.destination = entry.destination;

        move.capturedPieces.set(entry.capture1);

        if (entry.capture1 != Constants.BoardConstants.ILLEGAL_CELL) {
            move.capturedPieces.set(entry.capture1);
        }

        if (entry.capture2 != Constants.BoardConstants.ILLEGAL_CELL) {
            move.capturedPieces.set(entry.capture2);
        }

        if (entry.capture3 != Constants.BoardConstants.ILLEGAL_CELL) {
            move.capturedPieces.set(entry.capture3);
        }

        return move;
    }
}
