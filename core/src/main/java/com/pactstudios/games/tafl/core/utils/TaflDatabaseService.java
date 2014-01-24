package com.pactstudios.games.tafl.core.utils;

import java.util.Date;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.roundtriangles.games.zaria.services.db.DatabaseService;
import com.roundtriangles.games.zaria.services.db.DatabaseServiceConfig;
import com.roundtriangles.games.zaria.services.db.upgrade.DatabaseUpgradeService;

public class TaflDatabaseService extends DatabaseService {

    RuntimeExceptionDao<TaflMatch, Integer> matchDao;
    RuntimeExceptionDao<GamePiece, Integer> pieceDao;
    RuntimeExceptionDao<MatchLogEntry, Integer> logDao;

    @SuppressWarnings("unchecked")
    public TaflDatabaseService(ConnectionSource connectionSource) {
        config = new DatabaseServiceConfig();
        config.connectionSource = connectionSource;
        config.upgradeHistory.currentVersion = Constants.DbConstants.CURRENT_DB_VERSION;
        this.upgradeService = new DatabaseUpgradeService(config);

        try {
            matchDao = new RuntimeExceptionDao<TaflMatch, Integer>((Dao<TaflMatch, Integer>)
                    DaoManager.createDao(config.connectionSource, TaflMatch.class));
            pieceDao = new RuntimeExceptionDao<GamePiece, Integer>((Dao<GamePiece, Integer>)
                    DaoManager.createDao(config.connectionSource, GamePiece.class));
            logDao = new RuntimeExceptionDao<MatchLogEntry, Integer>((Dao<MatchLogEntry, Integer>)
                    DaoManager.createDao(config.connectionSource, MatchLogEntry.class));
        } catch (Exception e) {
            throw new GdxRuntimeException("Could not create the DAOs", e);
        }
    }

    @Override
    protected void createTables() {
        try {
            TableUtils.createTableIfNotExists(config.connectionSource, TaflMatch.class);
            TableUtils.createTableIfNotExists(config.connectionSource, GamePiece.class);
            TableUtils.createTableIfNotExists(config.connectionSource, MatchLogEntry.class);
        } catch (Exception e) {

        }
    }

    public void createMatch(TaflMatch match) {
        matchDao.create(match);

        for (GamePiece piece : match.pieces) {
            pieceDao.create(piece);
        }
    }

    public void updateMatch(TaflMatch match) {
        match.updated = new Date();
        matchDao.update(match);
    }

    public void createLogEntry(MatchLogEntry entry) {
        logDao.create(entry);
    }

    public void updatePiece(GamePiece piece) {
        piece.updated = new Date();
        pieceDao.update(piece);
    }
}
