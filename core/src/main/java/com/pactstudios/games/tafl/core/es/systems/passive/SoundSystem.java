package com.pactstudios.games.tafl.core.es.systems.passive;

import java.util.BitSet;

import com.artemis.systems.PassiveEntitySystem;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.roundtriangles.games.zaria.services.SoundService;

public class SoundSystem extends PassiveEntitySystem implements TaflMatchObserver {

    private SoundService soundService;

    public SoundSystem(SoundService soundService) {
        this.soundService = soundService;
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        // TODO play match start
    }

    @Override
    public void applyMove(TaflMatch match, TaflMove move) {
        soundService.playSound(Assets.Sounds.CLICK_SOUND);
    }

    @Override
    public void undoMove(TaflMatch match, TaflMove move) {
        // TODO play roll back time
    }

    @Override
    public void addPiece(TaflMatch match, int team, int pieces) {
    }

    @Override
    public void removePieces(TaflMatch match, int captor, BitSet capturedPieces) {
        // TODO capture sound
    }

    @Override
    public void changeTurn(TaflMatch match) {
        // TODO change turn??

    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
        // TODO play match end
    }
}
