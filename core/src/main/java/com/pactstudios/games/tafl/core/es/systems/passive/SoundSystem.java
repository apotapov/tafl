package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.systems.PassiveEntitySystem;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.roundtriangles.games.zaria.services.SoundService;

public class SoundSystem extends PassiveEntitySystem {

    private SoundService soundService;

    public SoundSystem(SoundService soundService) {
        this.soundService = soundService;
    }

    public void playShoot() {
        soundService.playSound(Assets.Sounds.CLICK_SOUND);
    }
}
