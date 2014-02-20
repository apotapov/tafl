package com.pactstudios.games.tafl.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.DeviceType;
import com.pactstudios.games.tafl.core.utils.TaflGameConfig;

public class TaflGameActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = Constants.GameConstants.USE_GL20;

        TaflGameConfig gameConfig = new TaflGameConfig();
        gameConfig.deviceType = DeviceType.PHONE;


        initialize(new TaflGame(gameConfig), config);
    }
}
