package com.pactstudios.games.tafl.core.utils;

import java.util.Date;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.LruObjectCache;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.AiFactory;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogFactory;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.model.rules.RulesFactory;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.roundtriangles.games.zaria.services.db.DatabaseService;
import com.roundtriangles.games.zaria.services.db.DatabaseServiceConfig;
import com.roundtriangles.games.zaria.services.db.upgrade.DatabaseUpgradeService;

public class TaflDatabaseService extends DatabaseService {

    RuntimeExceptionDao<TaflMatch, Integer> matchDao;
    RuntimeExceptionDao<GamePiece, Integer> pieceDao;
    RuntimeExceptionDao<MatchLogEntry, Integer> logDao;

    PreparedQuery<TaflMatch> loadMatchQuery;
    PreparedQuery<TaflMatch> matchLogQuery;

    public IntMap<ZorbistHash> hashs;

    public TaflDatabaseService(ConnectionSource connectionSource) {
        config = new DatabaseServiceConfig();
        config.connectionSource = connectionSource;
        config.upgradeHistory.currentVersion = Constants.DbConstants.CURRENT_DB_VERSION;
        this.upgradeService = new DatabaseUpgradeService(config);

        this.hashs = new IntMap<ZorbistHash>();

        initializeDaos();
        initializeQueries();
    }

    @SuppressWarnings("unchecked")
    private void initializeDaos() {
        try {
            matchDao = new RuntimeExceptionDao<TaflMatch, Integer>((Dao<TaflMatch, Integer>)
                    DaoManager.createDao(config.connectionSource, TaflMatch.class));
            matchDao.setObjectCache(true);
            matchDao.setObjectCache(new LruObjectCache(Constants.DbConstants.CACHE_SIZE));


            pieceDao = new RuntimeExceptionDao<GamePiece, Integer>((Dao<GamePiece, Integer>)
                    DaoManager.createDao(config.connectionSource, GamePiece.class));
            pieceDao.setObjectCache(true);
            pieceDao.setObjectCache(new LruObjectCache(Constants.DbConstants.CACHE_SIZE));

            logDao = new RuntimeExceptionDao<MatchLogEntry, Integer>((Dao<MatchLogEntry, Integer>)
                    DaoManager.createDao(config.connectionSource, MatchLogEntry.class));
            logDao.setObjectCache(true);
            logDao.setObjectCache(new LruObjectCache(Constants.DbConstants.CACHE_SIZE));
        } catch (Exception e) {
            throw new GdxRuntimeException("Could not create the DAOs", e);
        }
    }

    private void initializeQueries() {
        try {
            QueryBuilder<TaflMatch, Integer> qb = matchDao.queryBuilder();
            qb.where().eq(TaflMatch.STATUS_COLUMN, Lifecycle.PLAY);
            qb.orderBy(TaflMatch.CREATED_COLUMN, false);
            loadMatchQuery = qb.prepare();
        } catch (Exception e) {
            throw new GdxRuntimeException("Could not create Prepared Queries", e);
        }
    }

    @Override
    protected void createTables() {
        try {
            TableUtils.createTableIfNotExists(config.connectionSource, TaflMatch.class);
            TableUtils.createTableIfNotExists(config.connectionSource, GamePiece.class);
            TableUtils.createTableIfNotExists(config.connectionSource, MatchLogEntry.class);
        } catch (Exception e) {
            throw new GdxRuntimeException("Failed to created DB tables.", e);
        }
    }

    @Override
    protected void loadData() {
        //FIXME do not hard code these, and get them from the database.
        ZorbistHash hash = new ZorbistHash(Constants.PieceConstants.PIECE_TYPES, 9*9);
        hash.generate();
        hashs.put(9, hash);

        hash = new ZorbistHash(Constants.PieceConstants.PIECE_TYPES, 11*11);
        hash.generate();
        hashs.put(11, hash);
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

    public void deleteLogEntry(MatchLogEntry entry) {
        logDao.delete(entry);
    }

    public void updatePiece(GamePiece piece) {
        piece.updated = new Date();
        pieceDao.update(piece);
    }

    public TaflMatch loadMatch() {
        TaflMatch match = matchDao.queryForFirst(loadMatchQuery);

        if (match != null) {
            match.pieces = new Array<GamePiece>();
            match.board = new GameBoard(match.dimensions,
                    Constants.PieceConstants.PIECE_TYPES,
                    hashs.get(match.dimensions));

            match.rulesEngine = RulesFactory.getRules(match.rulesType, match);
            match.aiStrategy = AiFactory.getAiStrategy(match.aiType);

            CloseableIterator<GamePiece> it =
                    match.persistedPiece.closeableIterator();
            while (it.hasNext()) {
                GamePiece piece = it.next();
                match.pieces.add(piece);
            }
            it.closeQuietly();

            CloseableIterator<MatchLogEntry> it2 =
                    match.persistedLog.closeableIterator();
            while (it2.hasNext()) {
                match.undoStack.add(MatchLogFactory.parseLog(it2.next(), match.pieces));
            }
            it2.closeQuietly();
        }

        return match;
    }
}
