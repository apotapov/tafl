package com.pactstudios.games.tafl.core.es.systems.passive;

import java.util.Random;

import com.artemis.systems.PassiveEntitySystem;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.roundtriangles.games.zaria.services.SoundService;

public class SoundSystem extends PassiveEntitySystem implements TaflMatchObserver {

    private SoundService soundService;
    private Random random;

    public SoundSystem(SoundService soundService) {
        this.soundService = soundService;
        random = new Random();
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        soundService.playMusic(Assets.Sounds.LEVEL_MUSIC);
    }

    @Override
    public void applyMove(TaflMatch match, Move move) {
        int rnd = random.nextInt(2);
        if (move.pieceType == Constants.BoardConstants.WHITE_TEAM) {
            if (rnd == 0) {
                soundService.playSound(Assets.Sounds.WHITE_MOVE_1_SOUND);
            } else {
                soundService.playSound(Assets.Sounds.WHITE_MOVE_2_SOUND);
            }
        } else {
            if (rnd == 0) {
                soundService.playSound(Assets.Sounds.BLACK_MOVE_1_SOUND);
            } else {
                soundService.playSound(Assets.Sounds.BLACK_MOVE_2_SOUND);
            }
        }
    }

    @Override
    public void undoMove(TaflMatch match, Move move) {
        soundService.playSound(Assets.Sounds.UNDO_SOUND);
    }

    @Override
    public void removePieces(TaflMatch match, int team, BitBoard capturedPieces) {
        if (match.board.king != Constants.BoardConstants.ILLEGAL_CELL) {
            int cardinality = capturedPieces.cardinality();

            if (cardinality > 0) {
                soundService.vibrate(Constants.GameConstants.VIBRATION_LENGTH);
            }

            switch (cardinality) {
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
        switch (status) {
        case WIN:
        case DRAW:
        case SURRENDER:
            soundService.playSound(Assets.Sounds.KING_WIN_SOUND);
            break;
        case LOSS:
            soundService.playSound(Assets.Sounds.KING_LOSE_SOUND);
            break;
        default:
        }
    }
}
