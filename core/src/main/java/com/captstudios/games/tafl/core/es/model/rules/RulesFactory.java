package com.captstudios.games.tafl.core.es.model.rules;

import com.captstudios.games.tafl.core.enums.RulesEngineType;

public class RulesFactory {

    public static RulesEngine getRules(RulesEngineType rule) {
        switch(rule) {
        case OFFICIAL:
            return new OfficialRulesEngine();
        default:
            return null;
        }
    }
}
