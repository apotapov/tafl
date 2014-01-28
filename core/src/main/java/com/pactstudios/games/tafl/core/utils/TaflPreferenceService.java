package com.pactstudios.games.tafl.core.utils;

import com.pactstudios.games.tafl.core.consts.Constants;
import com.roundtriangles.games.zaria.services.PreferenceService;

public class TaflPreferenceService extends PreferenceService {

    public static final String PREF_DEFAULT_LEVEL = "match.level";
    public static final String PREF_VERSUS_COMPUTER = "match.versus";
    public static final String PREF_COMPUTER_STARTS = "sound.computerStarts";

    public TaflPreferenceService(String preferencesName) {
        super(preferencesName);
    }

    public TaflPreferenceService(String preferencesName, PreferenceChangeListener...listeners) {
        super(preferencesName, listeners);
    }

    @Override
    public void initialize() {
        super.initialize();
        setDefaultLevel(getInteger(PREF_DEFAULT_LEVEL, Constants.GameConstants.DEFAULT_LEVEL_INDEX));
        setVersusComputer(getBoolean(PREF_VERSUS_COMPUTER, Constants.GameConstants.DEFAULT_VERSUS_COMPUTER));
        setComputerStarts(getBoolean(PREF_COMPUTER_STARTS, Constants.GameConstants.DEFAULT_COMPUTER_STARTS));
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

    public void setDefaultLevel(int defaultLevel) {
        setInteger(PREF_DEFAULT_LEVEL, defaultLevel);
    }

    public int getDefaultLevel() {
        return getInteger(PREF_DEFAULT_LEVEL, Constants.GameConstants.DEFAULT_LEVEL_INDEX);
    }
}
