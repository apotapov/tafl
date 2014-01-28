package com.pactstudios.games.tafl.core.level;

import java.util.Date;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.AiFactory;
import com.pactstudios.games.tafl.core.es.model.ai.AiStrategy.AiType;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.model.rules.RulesFactory;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
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

    public TaflMatch createNewMatch(TaflLevel level, boolean versusComputer, boolean computerStarts) {

        TaflMatch match = new TaflMatch();
        match.created = new Date();
        match.updated = new Date();
        match.name = level.name;
        match.status = Lifecycle.PLAY;
        match.rulesType = level.rules;
        match.dimensions = level.dimensions;
        match.versusComputer = versusComputer;
        match.aiType = versusComputer ? AiType.MINIMAX_PIECE_COUNT : AiType.NONE;


        match.pieces = new Array<GamePiece>(level.pieces.size);
        for (GamePiece piece : level.pieces) {
            GamePiece matchPiece = piece.clone();
            matchPiece.updated = new Date();
            matchPiece.match = match;
            match.pieces.add(matchPiece);
        }

        match.board = new GameBoard(level.dimensions);
        match.rulesEngine = RulesFactory.getRules(level.rules, match);
        match.turn = match.rulesEngine.getFirstTurn();
        match.aiStrategy = AiFactory.getAiStrategy(match.aiType);

        match.computerTeam = computerStarts ? match.rulesEngine.getFirstTurn() :
            match.rulesEngine.getSecondTurn();

        databaseService.createMatch(match);

        return match;
    }
}
