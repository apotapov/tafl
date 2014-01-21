package com.pactstudios.games.tafl.core.es.model.objects;

import com.pactstudios.games.tafl.core.consts.Assets;


public enum PieceType {
    WHITE(Assets.Graphics.WHITE_PIECE, Team.WHITE),
    BLACK(Assets.Graphics.BLACK_PIECE, Team.BLACK),
    KING(Assets.Graphics.KING_PIECE, Team.WHITE);

    public String graphic;
    public Team team;

    private PieceType(String graphic, Team team) {
        this.graphic = graphic;
        this.team = team;
    }
}
