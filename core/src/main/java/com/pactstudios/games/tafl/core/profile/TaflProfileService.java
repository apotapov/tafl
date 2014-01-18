package com.pactstudios.games.tafl.core.profile;

import com.roundtriangles.games.zaria.services.ProfileService;

public class TaflProfileService extends ProfileService<TaflProfile> {

    public TaflProfileService() {
        super(new TaflProfileFactory());
    }

}
