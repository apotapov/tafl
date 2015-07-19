package com.captstudios.games.tafl.core.utils.services;

import com.captstudios.games.tafl.core.TaflGame;
import com.roundtriangles.games.zaria.services.PreferenceService;
import com.roundtriangles.games.zaria.services.SoundService;

public class TaflSoundService extends SoundService {

    TaflGame game;

    public TaflSoundService(TaflGame game) {
        this.game = game;
    }

    @Override
    public void onPreferenceChange(String name, boolean value) {
        if (name.equals(PreferenceService.PREF_SOUND_ENABLED)) {
            soundEnabled = value;
            changeMusicStatus(value && game.preferenceService.isMusicEnabled());
        } else if (name.equals(PreferenceService.PREF_MUSIC_ENABLED)) {
            if (value) {
                game.preferenceService.setSoundEnabled(true);
            } else {
                changeMusicStatus(false);
            }
        } else {
            super.onPreferenceChange(name, value);
        }
    }

}
