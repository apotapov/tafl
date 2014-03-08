package com.captstudios.games.tafl.core.es.systems.passive;

import java.util.Random;

import com.artemis.systems.PassiveEntitySystem;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.roundtriangles.games.zaria.services.SoundService;

public class SoundSystem extends PassiveEntitySystem implements TaflMatchObserver {

    private static final int MOVE_SOUND_COUNT = 2;

    private static final String[][] MOVE_SOUNDS = new String[][] {
        new String[] {
                Assets.Sounds.WHITE_MOVE_1_SOUND,
                Assets.Sounds.WHITE_MOVE_2_SOUND
        },
        new String[] {
                Assets.Sounds.BLACK_MOVE_1_SOUND,
                Assets.Sounds.BLACK_MOVE_2_SOUND
        },
        // King
        new String[] {
                Assets.Sounds.WHITE_MOVE_1_SOUND,
                Assets.Sounds.WHITE_MOVE_2_SOUND
        },
    };

    private static final String[] CAPTURE_SOUNDS = new String[] {
        Assets.Sounds.CAPTURE_1_SOUND,
        Assets.Sounds.CAPTURE_2_SOUND,
        Assets.Sounds.CAPTURE_3_SOUND,
    };

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
        soundService.playSound(MOVE_SOUNDS[move.pieceType][random.nextInt(MOVE_SOUND_COUNT)]);
    }

    @Override
    public void undoMove(TaflMatch match, Move move) {
        soundService.playSound(Assets.Sounds.UNDO_SOUND);
    }

    @Override
    public void removePieces(TaflMatch match, int team, BitBoard capturedPieces) {
        if (!match.board.kingBitBoard().isEmpty()) {
            int cardinality = capturedPieces.cardinality();

            if (cardinality > 0) {
                soundService.vibrate(Constants.GameConstants.CAPTURE_VIBRATION_LENGTH);
                soundService.playSound(CAPTURE_SOUNDS[cardinality - 1]);
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
            soundService.vibrate(Constants.GameConstants.GAME_OVER_VIBRATION_LENGTH);
            break;
        case LOSS:
            soundService.playSound(Assets.Sounds.KING_LOSE_SOUND);
            soundService.vibrate(Constants.GameConstants.GAME_OVER_VIBRATION_LENGTH);
            break;
        default:
        }
    }
}
