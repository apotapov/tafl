package com.pactstudios.games.tafl.core.es.components.interaction;

import com.artemis.Component;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;

public class PieceComponent implements Component {

    public GamePiece piece;

    @Override
    public void reset() {
        piece = null;
    }
}
