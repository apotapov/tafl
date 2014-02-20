package com.captstudios.games.tafl.core.level;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.roundtriangles.games.zaria.services.LevelService;

public class TaflLevelService extends LevelService<TaflLevel>{

    TaflGame game;

    public TaflLevelService(TaflGame game) {
        super(TaflLevel.class, Assets.Game.LEVEL_LIST);
        this.game = game;
    }

    @Override
    public AsynchronousAssetLoader<TaflLevel, AssetLoaderParameters<TaflLevel>> getLevelLoader() {
        return new TaflLevelDataLoader(new InternalFileHandleResolver());
    }

    public TaflMatch createNewMatch(TaflLevel level) {

        TaflMatch match = new TaflMatch();
        match.created = System.currentTimeMillis();
        match.updated = match.created;
        match.name = level.name;
        match.status = LifeCycle.PLAY;
        match.rulesType = level.rules;
        match.versusComputer = game.preferenceService.getVersusComputer();
        match.computerStarts = game.preferenceService.getComputerStarts();
        match.aiType = game.preferenceService.getAiType();

        match.boardRepresentation = level.boardRepresentation;

        return match;
    }
}
