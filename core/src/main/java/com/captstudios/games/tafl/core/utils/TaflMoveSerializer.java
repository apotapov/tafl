package com.captstudios.games.tafl.core.utils;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.roundtriangles.games.zaria.utils.ModifiableString;

public class TaflMoveSerializer implements Serializer<Move> {

    private static final String[] PERSISTED_FIELDS = {
        "pieceType",
        "source",
        "destination",
        "eval",
        "evalType",
        "searchDepth",
        "capturedPieces"
    };

    ModifiableString capturedPiecesString;


    @SuppressWarnings("rawtypes")
    @Override
    public void write(Json json, Move move, Class knownType) {
        json.writeObjectStart();
        for (String field : PERSISTED_FIELDS) {
            json.writeField(move, field);
        }
        json.writeObjectEnd();
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    @Override
    public Move read(Json json, JsonValue jsonData, Class type) {
        Move move = new Move();
        json.readFields(move, jsonData);
        return move;
    }

}
