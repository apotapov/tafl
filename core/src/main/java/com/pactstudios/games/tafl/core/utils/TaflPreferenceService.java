package com.pactstudios.games.tafl.core.utils;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.roundtriangles.games.zaria.services.PreferenceService;

public class TaflPreferenceService extends PreferenceService {

    public static final String PREF_DEFAULT_LEVEL = "match.level";
    public static final String PREF_VERSUS_COMPUTER = "match.versus";
    public static final String PREF_COMPUTER_STARTS = "match.computerStarts";
    public static final String PREF_AI_TYPE = "match.aiType";

    public TaflPreferenceService(String preferencesName) {
        super(preferencesName);
    }

    public TaflPreferenceService(String preferencesName, PreferenceChangeListener...listeners) {
        super(preferencesName, listeners);
    }

    @Override
    public void initialize() {
        super.initialize();
        setLevel(getInteger(PREF_DEFAULT_LEVEL, Constants.GameConstants.DEFAULT_LEVEL_INDEX));
        setVersusComputer(getBoolean(PREF_VERSUS_COMPUTER, Constants.GameConstants.DEFAULT_VERSUS_COMPUTER));
        setComputerStarts(getBoolean(PREF_COMPUTER_STARTS, Constants.GameConstants.DEFAULT_COMPUTER_STARTS));
        setAiType(getString(PREF_AI_TYPE, Constants.AiConstants.DEFAULT_AI_TYPE.toString()));
    }

    public void setComputerStarts(boolean computerStarts) {
        setBoolean(PREF_COMPUTER_STARTS, computerStarts);
    }

    public boolean getComputerStarts() {
        return getBoolean(PREF_COMPUTER_STARTS, Constants.GameConstants.DEFAULT_COMPUTER_STARTS);
    }

    public void setVersusComputer(boolean versusComputer) {
        setBoolean(PREF_VERSUS_COMPUTER, versusComputer);
    }

    public boolean getVersusComputer() {
        return getBoolean(PREF_VERSUS_COMPUTER, Constants.GameConstants.DEFAULT_VERSUS_COMPUTER);
    }

    public void setLevel(int level) {
        setInteger(PREF_DEFAULT_LEVEL, level);
    }

    public int getLevel() {
        return getInteger(PREF_DEFAULT_LEVEL, Constants.GameConstants.DEFAULT_LEVEL_INDEX);
    }

    public void setAiType(AiType aiType) {
        setString(PREF_DEFAULT_LEVEL, aiType.toString());
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
}
