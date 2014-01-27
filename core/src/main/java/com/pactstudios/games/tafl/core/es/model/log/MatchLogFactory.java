package com.pactstudios.games.tafl.core.es.model.log;

import java.util.Date;

import com.badlogic.gdx.utils.Array;
import com.j256.ormlite.dao.CloseableIterator;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class MatchLogFactory {

    public static MatchLogEntry log(TaflMatch match, Move move) {
        MatchLogEntry entry = new MatchLogEntry();
        entry.match = match;
        entry.piece = move.piece;
        entry.x1 = move.start.x;
        entry.y1 = move.start.y;
        entry.x2 = move.end.x;
        entry.y2 = move.end.y;
        entry.updated = new Date();
        return entry;
    }

    public static Move parseLog(MatchLogEntry entry, Array<GamePiece> pieces) {
        Move move = new Move();

        for (GamePiece piece : pieces) {
            if (piece.equals(entry.piece)) {
                move.piece = piece;
                break;
            }
        }
        move.start = entry.match.board.getCell(entry.x1, entry.y1);
        move.end = entry.match.board.getCell(entry.x2, entry.y2);

        CloseableIterator<GamePiece> iterator = entry.killedPieces.closeableIterator();
        while (iterator.hasNext()) {
            GamePiece capturedPiece = iterator.next();
            for (GamePiece piece : pieces) {
                if (piece.equals(capturedPiece)) {
                    move.captured.add(piece);
                    break;
                }
            }
        }
        iterator.closeQuietly();
        return move;
    }
}
