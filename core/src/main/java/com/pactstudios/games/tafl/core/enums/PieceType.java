package com.pactstudios.games.tafl.core.enums;

import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.es.model.board.IBitBoardIdentifier;


public enum PieceType implements IBitBoardIdentifier {
    WHITE(Assets.Graphics.WHITE_PIECE, Team.WHITE, 0),
    BLACK(Assets.Graphics.BLACK_PIECE, Team.BLACK, 1),
    KING(Assets.Graphics.KING_PIECE, Team.WHITE, 2);

    public String graphic;
    public Team team;

    private PieceType(String graphic, Team team, int bitBoardId) {
        this.graphic = graphic;
        this.team = team;
    }

    @Override
    public int bitBoardId() {
        return team.bitBoardId();
    }
}
