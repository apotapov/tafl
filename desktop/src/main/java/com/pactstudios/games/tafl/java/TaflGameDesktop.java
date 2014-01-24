package com.pactstudios.games.tafl.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.j256.ormlite.db.SqliteDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.utils.TaflGameConfig;

public class TaflGameDesktop {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.GameConstants.GAME_WIDTH;
        config.height = Constants.GameConstants.GAME_HEIGHT;
        config.useGL20 = true;
        new LwjglApplication(new TaflGame(getGameConfig()), config);
    }

    private static TaflGameConfig getGameConfig() {
        return new TaflGameConfig() {

            @Override
            public ConnectionSource getConnectionSource() {
                try {
                    FileHandle database = Gdx.files.external(Constants.DbConstants.SQLITE_DB_FILE);
                    String url = Constants.DbConstants.DB_URL_PREFIX + database.path();
                    return new JdbcConnectionSource(url, new SqliteDatabaseType());
                } catch (Exception e) {
                    throw new GdxRuntimeException("Could not create JDBC connection", e);
                }
            }
        };
    }
}
