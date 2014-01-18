package com.pactstudios.games.tafl.core.profile;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.roundtriangles.games.zaria.services.utils.ProfileFactory;

public class TaflProfileFactory implements ProfileFactory<TaflProfile> {

    private static final String CURRENT_LEVEL_FIELD = "currentLevel";
    protected Json json;

    public TaflProfileFactory() {
        this.json = new Json();
        //this.json.setSerializer(TaflProfile.class, this);
    }

    @Override
    public TaflProfile newProfile() {
        TaflProfile profile = new TaflProfile();
        return profile;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public void write(Json json, TaflProfile profile, Class knownType) {
        json.writeValue(CURRENT_LEVEL_FIELD, profile.currentLevel);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public TaflProfile read(Json json, JsonValue jsonData, Class type) {
        TaflProfile profile = new TaflProfile();
        profile.currentLevel = jsonData.getString(CURRENT_LEVEL_FIELD);
        return profile;
    }

    @Override
    public Json getJson() {
        return json;
    }

    @Override
    public Class<TaflProfile> getProfileClass() {
        return TaflProfile.class;
    }
}
