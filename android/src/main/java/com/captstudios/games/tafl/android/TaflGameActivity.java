package com.captstudios.games.tafl.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.utils.device.DeviceType;
import com.captstudios.games.tafl.core.utils.device.TaflGameConfig;

public class TaflGameActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = Constants.GameConstants.USE_GL20;

        TaflGameConfig gameConfig = new TaflGameConfig();
        gameConfig.deviceType = DeviceType.ANDROID;
        gameConfig.vibrationSupported = true;

        initialize(new TaflGame(gameConfig), config);
    }
}
