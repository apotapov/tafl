package com.pactstudios.games.tafl.core.level;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.model.map.TaflMap;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.map.objects.Piece;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
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
        if (level.map == null) {
            loadMap(level, gameWorld);
        } else {
            level.map.reset();
        }
        createLevelObjects(level, gameWorld);
    }

    protected void loadMap(TaflLevel level, TaflWorld gameWorld) {
        level.map = new TaflMap();
        level.map.initialize();
    }

    protected void createLevelObjects(TaflLevel level, TaflWorld gameWorld) {
        EntityFactorySystem efs = gameWorld.world.getSystem(EntityFactorySystem.class);
        efs.createMap(level.map);
        efs.createHud(level);
        efs.createRenderers(gameWorld);

        for (Piece piece : level.pieces) {
            ModelCell cell = level.map.getCell(piece.x, piece.y);
            cell.entity = efs.createPiece(piece);
        }
    }
}
