package com.captstudios.games.tafl.core.utils.services;
//package com.captstudios.games.tafl.core.utils;
//
//import com.badlogic.gdx.utils.GdxRuntimeException;
//import com.captstudios.games.tafl.core.consts.Constants;
//import com.captstudios.games.tafl.core.enums.LifeCycle;
//import com.captstudios.games.tafl.core.es.model.TaflMatch;
//import com.captstudios.games.tafl.core.es.model.TaflMatchObserver;
//import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
//import com.roundtriangles.games.zaria.services.db.DatabaseService;
//import com.roundtriangles.games.zaria.services.db.DatabaseServiceConfig;
//import com.roundtriangles.games.zaria.services.db.upgrade.DatabaseUpgradeService;
//
//public class TaflDatabaseService extends DatabaseService implements TaflMatchObserver {
//
//    RuntimeExceptionDao<TaflMatch, Integer> matchDao;
//    RuntimeExceptionDao<MatchLogEntry, Integer> logDao;
//
//    PreparedQuery<TaflMatch> loadMatchQuery;
//    PreparedQuery<TaflMatch> matchLogQuery;
//
//
//
//    public TaflDatabaseService(ConnectionSource connectionSource) {
//        config = new DatabaseServiceConfig();
//        config.connectionSource = connectionSource;
//        config.upgradeHistory.currentVersion = Constants.DbConstants.CURRENT_DB_VERSION;
//        this.upgradeService = new DatabaseUpgradeService(config);
//
//        initializeDaos();
//        initializeQueries();
//    }
//
//    @SuppressWarnings("unchecked")
//    private void initializeDaos() {
//        try {
//            matchDao = new RuntimeExceptionDao<TaflMatch, Integer>((Dao<TaflMatch, Integer>)
//                    DaoManager.createDao(config.connectionSource, TaflMatch.class));
//            matchDao.setObjectCache(true);
//            matchDao.setObjectCache(new LruObjectCache(Constants.DbConstants.CACHE_SIZE));
//
//            logDao = new RuntimeExceptionDao<MatchLogEntry, Integer>((Dao<MatchLogEntry, Integer>)
//                    DaoManager.createDao(config.connectionSource, MatchLogEntry.class));
//            logDao.setObjectCache(true);
//            logDao.setObjectCache(new LruObjectCache(Constants.DbConstants.CACHE_SIZE));
//        } catch (Exception e) {
//            throw new GdxRuntimeException("Could not create the DAOs", e);
//        }
//    }
//
//    private void initializeQueries() {
//        try {
//            QueryBuilder<TaflMatch, Integer> qb = matchDao.queryBuilder();
//            qb.where().eq(TaflMatch.STATUS_COLUMN, LifeCycle.PLAY);
//            qb.orderBy(TaflMatch.CREATED_COLUMN, false);
//            loadMatchQuery = qb.prepare();
//        } catch (Exception e) {
//            throw new GdxRuntimeException("Could not create Prepared Queries", e);
//        }
//    }
//
//    @Override
//    protected void createTables() {
//        try {
//            TableUtils.createTableIfNotExists(config.connectionSource, TaflMatch.class);
//            TableUtils.createTableIfNotExists(config.connectionSource, MatchLogEntry.class);
//        } catch (Exception e) {
//            throw new GdxRuntimeException("Failed to created DB tables.", e);
//        }
//    }
//
//    @Override
//    protected void loadData() {
//
//    }
//
//    public TaflMatch loadMatch() {
//        return matchDao.queryForFirst(loadMatchQuery);
//    }
//
//    @Override
//    public void applyMove(TaflMatch match, TaflMove move) {
//        updateMatch(match);
//        createLogEntry(move.entry);
//    }
//
//    @Override
//    public void undoMove(TaflMatch match, TaflMove move) {
//        updateMatch(match);
//        deleteLogEntry(move.entry);
//    }
//
//    @Override
//    public void addPiece(TaflMatch match, int team, int pieces) {
//    }
//
//    @Override
//    public void removePieces(TaflMatch match, int team, BitBoard capturedPieces) {
//        updateMatch(match);
//    }
//
//    @Override
//    public void initializeMatch(TaflMatch match) {
//        if (match._id == 0) {
//            matchDao.create(match);
//        } else {
//            CloseableIterator<MatchLogEntry> it =
//                    match.persistedLog.closeableIterator();
//            while (it.hasNext()) {
//                match.board.undoStack.add(it.next().createMove());
//            }
//            it.closeQuietly();
//        }
//    }
//
//    @Override
//    public void changeTurn(TaflMatch match) {
//        updateMatch(match);
//    }
//
//    @Override
//    public void gameOver(TaflMatch match, LifeCycle status) {
//        updateMatch(match);
//    }
//
//    private void updateMatch(TaflMatch match) {
//        match.updated.setTime(System.currentTimeMillis());
//        match.boardRepresentation = match.getBoardRepresentation();
//
//        matchDao.update(match);
//    }
//
//    private void createLogEntry(MatchLogEntry entry) {
//        logDao.create(entry);
//    }
//
//    private void deleteLogEntry(MatchLogEntry entry) {
//        logDao.delete(entry);
//    }
//}
