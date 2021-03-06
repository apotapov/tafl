package com.captstudios.games.tafl.core.utils.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.AiType;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.TaflMatchObserver;
import com.captstudios.games.tafl.core.es.model.ai.AiFactory;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.utils.TaflMatchSerializer;
import com.captstudios.games.tafl.core.utils.TaflMoveSerializer;
import com.roundtriangles.games.zaria.services.PreferenceService;

public class TaflPreferenceService extends PreferenceService implements TaflMatchObserver {

    private static final String LOG_TAG = TaflPreferenceService.class.toString();

    public static final String PREF_DEFAULT_LEVEL = "match.level";
    public static final String PREF_COMPUTER_STARTS = "match.computerStarts";
    public static final String PREF_AI_TYPE = "match.aiType";
    public static final String PREF_SAVED_MATCH = "match.savedMatch";
    public static final String PREF_SHOW_HELP_ON_START = "game.showHelpOnStart";

    protected Json json;

    public TaflPreferenceService(PreferenceChangeListener...listeners) {
        super(TaflGame.class.getName(), Constants.GameConstants.DEFAULT_VOLUME, listeners);
        initializeJson();
    }

    private void initializeJson() {
        json = new Json();
        json.setSerializer(TaflMatch.class, new TaflMatchSerializer());
        json.setSerializer(Move.class, new TaflMoveSerializer());
        json.setOutputType(OutputType.minimal);
    }

    @Override
    public void initialize() {
        super.initialize();
        updateListeners(PREF_DEFAULT_LEVEL, getLevelIndex());
        updateListeners(PREF_COMPUTER_STARTS, getComputerStarts());
        updateListeners(PREF_SHOW_HELP_ON_START, getShowHelpOnStart());
        updateListeners(PREF_AI_TYPE, getAiType().toString());
    }

    public void setComputerStarts(boolean computerStarts) {
        setBoolean(PREF_COMPUTER_STARTS, computerStarts);
    }

    public boolean getComputerStarts() {
        return getBoolean(PREF_COMPUTER_STARTS, Constants.GameConstants.DEFAULT_COMPUTER_STARTS);
    }

    public void setShowHelpOnStart(boolean showHelp) {
        setBoolean(PREF_SHOW_HELP_ON_START, showHelp);
    }

    public boolean getShowHelpOnStart() {
        return getBoolean(PREF_SHOW_HELP_ON_START, Constants.GameConstants.DEFAULT_SHOW_HELP_ON_START);
    }

    public void setLevelIndex(int level) {
        setInteger(PREF_DEFAULT_LEVEL, level);
    }

    public int getLevelIndex() {
        return getInteger(PREF_DEFAULT_LEVEL, Constants.GameConstants.DEFAULT_LEVEL_INDEX);
    }

    public void setAiType(AiType aiType) {
        setString(PREF_AI_TYPE, aiType.toString());
    }

    public void setAiType(String aiType) {
        setString(PREF_AI_TYPE, aiType);
    }

    public AiType getAiType() {
        try {
            return AiType.valueOf(getString(PREF_AI_TYPE, Constants.AiConstants.DEFAULT_AI_TYPE.toString()));
        } catch (IllegalArgumentException e) {
            return Constants.AiConstants.DEFAULT_AI_TYPE;
        }
    }

    public TaflMatch loadMatch() {
        String match = getString(PREF_SAVED_MATCH, null);
        if (match != null) {
            try {
                return json.fromJson(TaflMatch.class, match);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        match.aiStrategy = AiFactory.getAiStrategy(getAiType(), match.board);
        updateMatch(match);
    }

    @Override
    public void applyMove(TaflMatch match, Move move) {
        updateMatch(match);
    }

    @Override
    public void undoMove(TaflMatch match, Move move) {
        updateMatch(match);
    }

    @Override
    public void removePieces(TaflMatch match, int captor,
            BitBoard capturedPieces) {
        updateMatch(match);
    }

    @Override
    public void changeTurn(TaflMatch match) {
        updateMatch(match);
    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
        if (status != LifeCycle.QUIT) {
            setString(PREF_SAVED_MATCH, "");
        }
    }

    private void updateMatch(TaflMatch match) {
        try {
            setString(PREF_SAVED_MATCH, json.toJson(match));
            if (Constants.GameConstants.DEBUG) {
                Gdx.app.log(LOG_TAG, match.board.toString());
            }
        } catch (Exception e) {
            Gdx.app.error(LOG_TAG, "Could not save the game.", e);
        }
    }

    public boolean hasSavedMatch() {
        return getString(PREF_SAVED_MATCH, null) != null;
    }
}
