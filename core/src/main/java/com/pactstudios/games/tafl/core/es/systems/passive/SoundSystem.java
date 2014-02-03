package com.pactstudios.games.tafl.core.es.systems.passive;

import java.util.BitSet;

import com.artemis.systems.PassiveEntitySystem;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
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
        soundService.playMusic(Assets.Sounds.LEVEL_MUSIC);
    }

    @Override
    public void applyMove(TaflMatch match, TaflMove move) {
        if (move.pieceType == Constants.BoardConstants.WHITE_TEAM) {
            soundService.playSound(Assets.Sounds.WHITE_MOVE_SOUND);
        } else {
            soundService.playSound(Assets.Sounds.BLACK_MOVE_SOUND);
        }
    }

    @Override
    public void undoMove(TaflMatch match, TaflMove move) {
        soundService.playSound(Assets.Sounds.UNDO_SOUND);
    }

    @Override
    public void addPiece(TaflMatch match, int team, int pieces) {
    }

    @Override
    public void removePieces(TaflMatch match, int captor, BitSet capturedPieces) {
        if (match.board.king == Constants.BoardConstants.ILLEGAL_CELL) {
            soundService.playSound(Assets.Sounds.CAPTURE_KING_SOUND);
        } else {
            switch (capturedPieces.cardinality()) {
            case 1:
                soundService.playSound(Assets.Sounds.CAPTURE_1_SOUND);
                break;
            case 2:
                soundService.playSound(Assets.Sounds.CAPTURE_2_SOUND);
                break;
            case 3:
                soundService.playSound(Assets.Sounds.CAPTURE_3_SOUND);
                break;
            default:
            }
        }
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
