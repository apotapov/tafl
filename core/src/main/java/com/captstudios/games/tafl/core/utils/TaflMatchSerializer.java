package com.captstudios.games.tafl.core.utils;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import com.captstudios.games.tafl.core.es.model.TaflMatch;

public class TaflMatchSerializer implements Serializer<TaflMatch> {

    private static final String[] PERSISTED_FIELDS = {
        "name",
        "status",
        "turn",
        "rulesType",
        "versusComputer",
        "computerTeam",
        "aiType",
        "boardRepresentation",
    };

    private static final String UNDO_STACK = "initialUndoStack";


    @SuppressWarnings("rawtypes")
    @Override
    public void write(Json json, TaflMatch match, Class knownType) {
        match.boardRepresentation = match.board.toString();
        json.writeObjectStart();
        for (String field : PERSISTED_FIELDS) {
            json.writeField(match, field);
        }
        json.writeValue(UNDO_STACK, match.board.undoStack);
        json.writeObjectEnd();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public TaflMatch read(Json json, JsonValue jsonData, Class type) {
        TaflMatch match = new TaflMatch();
        json.readFields(match, jsonData);
        return match;
    }

}
