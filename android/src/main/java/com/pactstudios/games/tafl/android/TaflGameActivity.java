package com.pactstudios.games.tafl.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.utils.TaflGameConfig;

public class TaflGameActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = Constants.GameConstants.USE_GL20;

        initialize(new TaflGame(getGameConfig()), config);
    }

    private TaflGameConfig getGameConfig() {
        return new TaflGameConfig() {
            @Override
            public ConnectionSource getConnectionSource() {
                try {
                    return new AndroidConnectionSource(
                            new DatabaseHelper(TaflGameActivity.this,
                                    Constants.DbConstants.SQLITE_DB_NAME,
                                    null,
                                    Constants.DbConstants.CURRENT_DB_VERSION));
                } catch (Exception e) {
                    throw new GdxRuntimeException(e);
                }
            }
        };
    }
}
