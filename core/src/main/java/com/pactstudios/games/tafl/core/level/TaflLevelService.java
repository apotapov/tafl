package com.pactstudios.games.tafl.core.level;

import java.util.Date;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.model.rules.RulesFactory;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;
import com.roundtriangles.games.zaria.services.LevelService;

public class TaflLevelService extends LevelService<TaflLevel>{

    public TaflDatabaseService databaseService;

    public TaflLevelService(TaflDatabaseService databaseService) {
        super(TaflLevel.class, Assets.Game.LEVEL_LIST);
        this.databaseService = databaseService;
    }

    @Override
    public AsynchronousAssetLoader<TaflLevel, AssetLoaderParameters<TaflLevel>> getLevelLoader() {
        return new TaflLevelDataLoader(new InternalFileHandleResolver());
    }

    public TaflMatch createNewMatch(TaflLevel level, TaflWorld world) {

        TaflMatch match = new TaflMatch();
        match.created = new Date();
        match.updated = new Date();
        match.name = level.name;
        match.rules = level.rules;
        match.dimension = level.dimensions;

        match.pieces = level.pieces;
        match.board = new GameBoard(level.dimensions);
        match.rulesEngine = RulesFactory.getRules(level.rules, match);
        match.turn = match.rulesEngine.turn;

        createLevelObjects(match, world);

        databaseService.createMatch(match);

        return match;
    }

    protected void createLevelObjects(TaflMatch match, TaflWorld gameWorld) {
        EntityFactorySystem efs = gameWorld.world.getSystem(EntityFactorySystem.class);
        efs.createBoard(match);
        efs.createHud(match);
        efs.createRenderers(gameWorld);

        for (GamePiece piece : match.pieces) {
            piece.updated = new Date();
            piece.match = match;

            ModelCell cell = match.board.getCell(piece.x, piece.y);
            piece.entity = efs.createPiece(piece);
            cell.piece = piece;
        }
    }
}
