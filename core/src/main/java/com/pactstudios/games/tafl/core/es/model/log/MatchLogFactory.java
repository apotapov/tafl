package com.pactstudios.games.tafl.core.es.model.log;

import java.util.Date;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class MatchLogFactory {

    public static MatchLogEntry log(TaflMatch match, Move move) {
        MatchLogEntry entry = new MatchLogEntry();
        entry.match = match;
        entry.team = Team.fromId(move.pieceType);
        entry.source = move.source;
        entry.destination = move.destination;
        entry.updated = new Date();

        switch (move.capturedPieces.size) {
        case 3:
            entry.capture3 = move.capturedPieces.items[2];
        case 2:
            entry.capture2 = move.capturedPieces.items[1];
        case 1:
            entry.capture1 = move.capturedPieces.items[0];
            break;
        default:
        }
        return entry;
    }

    public static Move parseLog(MatchLogEntry entry, TaflMatch match) {
        Move move = new Move();

        move.pieceType = entry.team.bitBoardId();
        move.source = entry.source;
        move.destination = entry.destination;

        if (entry.capture1 != Constants.DbConstants.NO_CAPTURES) {
            move.capturedPieces.add(entry.capture1);
        }

        if (entry.capture2 != Constants.DbConstants.NO_CAPTURES) {
            move.capturedPieces.add(entry.capture3);
        }

        if (entry.capture3 != Constants.DbConstants.NO_CAPTURES) {
            move.capturedPieces.add(entry.capture2);
        }

        return move;
    }
}
