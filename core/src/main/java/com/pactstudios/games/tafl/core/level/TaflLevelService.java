package com.pactstudios.games.tafl.core.level;

import java.util.Date;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.AiStrategy.AiType;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.roundtriangles.games.zaria.services.LevelService;

public class TaflLevelService extends LevelService<TaflLevel>{

    public TaflLevelService() {
        super(TaflLevel.class, Assets.Game.LEVEL_LIST);
    }

    @Override
    public AsynchronousAssetLoader<TaflLevel, AssetLoaderParameters<TaflLevel>> getLevelLoader() {
        return new TaflLevelDataLoader(new InternalFileHandleResolver());
    }

    public TaflMatch createNewMatch(TaflLevel level, boolean versusComputer, boolean computerStarts) {

        TaflMatch match = new TaflMatch();
        match.created = new Date();
        match.updated = new Date();
        match.name = level.name;
        match.status = Lifecycle.PLAY;
        match.rulesType = level.rules;
        match.boardType = level.boardType;
        match.versusComputer = versusComputer;
        match.aiType = versusComputer ? AiType.MINIMAX_PIECE_COUNT : AiType.NONE;
        match.computerStarts = computerStarts;

        match.whitePieces = level.whitePieces;
        match.blackPieces = level.blackPieces;
        match.king = level.king;

        return match;
    }
}
