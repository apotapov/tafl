package com.pactstudios.games.tafl.core.level;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.board.RulesEngine;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Piece;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.utils.log.GameLog;
import com.roundtriangles.games.zaria.services.LevelService;

public class TaflLevelService extends LevelService<TaflLevel>{

    public TaflLevelService() {
        super(TaflLevel.class, Assets.Game.LEVEL_LIST);
    }

    @Override
    public AsynchronousAssetLoader<TaflLevel, AssetLoaderParameters<TaflLevel>> getLevelLoader() {
        return new TaflLevelDataLoader(new InternalFileHandleResolver());
    }

    public void initializeLevel(TaflLevel level, TaflWorld gameWorld) {
        if (level.board == null) {
            loadBoard(level, gameWorld);
        } else {
            level.reset();
        }
        createLevelObjects(level, gameWorld);
    }

    protected void loadBoard(TaflLevel level, TaflWorld gameWorld) {
        level.board = new GameBoard(level.dimensions, level.dimensions);
        level.log = new GameLog();
        level.rulesEngine = new RulesEngine(level, gameWorld.world);
    }

    protected void createLevelObjects(TaflLevel level, TaflWorld gameWorld) {
        EntityFactorySystem efs = gameWorld.world.getSystem(EntityFactorySystem.class);
        efs.createBoard(level);
        efs.createHud(level);
        efs.createRenderers(gameWorld);

        for (Piece piece : level.pieces) {
            ModelCell cell = level.board.getCell(piece.x, piece.y);
            piece.entity = efs.createPiece(piece);
            cell.piece = piece;
        }
    }
}
