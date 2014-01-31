package com.pactstudios.games.tafl.core.es.model.rules;

import com.pactstudios.games.tafl.core.enums.RulesEngine;
import com.pactstudios.games.tafl.core.enums.RulesEngine.RulesEngineType;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

public class RulesFactory {

    public static RulesEngine getRules(RulesEngineType rule, TaflMatch game) {
        switch(rule) {
        case BASIC:
            return new OfficialRulesEngine(game);
        default:
            return null;
        }
    }
}
