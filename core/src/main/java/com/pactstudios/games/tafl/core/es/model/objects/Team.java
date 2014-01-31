package com.pactstudios.games.tafl.core.es.model.objects;

import com.pactstudios.games.tafl.core.es.model.board.IBitBoardIdentifier;


public enum Team implements IBitBoardIdentifier {
    WHITE(0),
    BLACK(1);

    private int bitBoardId;

    private Team(int bitBoardId) {
        this.bitBoardId = bitBoardId;
    }

    @Override
    public int bitBoardId() {
        return bitBoardId;
    }

    public Team getOpositeTeam() {
        return this == WHITE ? BLACK : WHITE;
    }

    public static Team fromId(int id) {
        return id == Team.WHITE.bitBoardId ? Team.WHITE : Team.BLACK;
    }
}
