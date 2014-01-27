package com.pactstudios.games.tafl.core.es.model.objects;

import com.pactstudios.games.tafl.core.consts.LocalizedStrings;

public enum Team {
    WHITE(LocalizedStrings.Game.TEAM_WHITE),
    BLACK(LocalizedStrings.Game.TEAM_BLACK);

    private LocalizedStrings.Game localizedName;

    private Team(LocalizedStrings.Game localizedName) {
        this.localizedName = localizedName;
    }

    public Object getLocalizedName() {
        return localizedName;
    }
}
