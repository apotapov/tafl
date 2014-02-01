package com.pactstudios.games.tafl.core.es;

import java.util.BitSet;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.EntitySystem;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.PieceType;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.roundtriangles.games.zaria.camera.Bounded2DCamera;

public class TaflWorld implements Disposable {

    public TaflGame game;
    public World world;

    public OrthographicCamera camera;
    public Stage stage;

    public TaflLevel level;
    public TaflMatch match;

    private Array<EntitySystem> activeSystems;

    public LifeCycle lifecycle;

    public TaflWorld(TaflGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }

    public void setLevel(TaflLevel level) {
        this.level = level;
    }

    public void initialize() {
        this.world = new World();
        this.camera = new Bounded2DCamera();
        this.activeSystems = new Array<EntitySystem>();

        //world.setManager(new TaflManager());
        world.setManager(new SingletonComponentManager());
        world.setManager(new GroupManager());
        SystemFactory.initSystems(this, activeSystems);
        world.initialize();

        match.initialize(game.databaseService);

        createEntities();

        lifecycle = LifeCycle.PLAY;
        world.getSystem(CellHighlightSystem.class).highlightTeam(match.turn);

        if (match.versusComputer &&
                match.turn == match.computerTeam) {
            AiTurnEvent aiTurn = world.createEvent(AiTurnEvent.class);
            world.postEvent(null, aiTurn);
        }
    }

    public void render(float delta) {
        switch (lifecycle) {
        case QUIT:
            game.setScreen(game.getMainMenuScreen());
            break;
        case RESTART:
            restart();
            break;
        default:
            world.setDelta(delta);
            world.process();
        }
    }

    public void resize(int width, int height) {
        float boardSize = match.getBoardDimensionWithBorders();
        float center = boardSize / 2.0f;

        this.camera.position.set(center, center, 0);
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.zoom = boardSize / width;
        this.stage.setViewport(Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT, true);
    }

    public void pause() {
        if (lifecycle == LifeCycle.PLAY) {
            LifeCycleEvent event = SystemEvent.createEvent(LifeCycleEvent.class);
            event.lifecycle = LifeCycle.MENU;
            world.postEvent(null, event);
        }
    }

    public void pauseSystems() {
        for (EntitySystem system : activeSystems) {
            system.setPassive(true);
        }
    }

    public void resumeSystems() {
        for (EntitySystem system : activeSystems) {
            system.setPassive(false);
        }
    }

    public void restart() {
        dispose();

        match.status = LifeCycle.RESTART;
        game.databaseService.updateMatch(match);

        createNewMatch(match.versusComputer, match.computerTeam == match.rulesEngine.getFirstTurn());

        initialize();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void dispose() {
        world.dispose();
        activeSystems.clear();
        stage.clear();
        lifecycle = null;
    }

    public boolean createNewMatch(boolean versusComputer, boolean computerStarts) {
        if (level == null && match == null) {
            game.setScreen(game.levelSelectionScreen);
        } else if (level == null) {
            level = game.levelService.getLevel(match.name);
        }
        match = game.levelService.createNewMatch(level, versusComputer, computerStarts);
        return match != null;
    }

    public boolean loadExistingMatch() {
        TaflMatch match = game.databaseService.loadMatch();
        if (match != null) {
            this.match = match;
            return true;
        }
        return false;
    }

    protected void createEntities() {
        EntityFactorySystem efs = world.getSystem(EntityFactorySystem.class);
        efs.createBoard(match);
        efs.createHud(match);
        efs.createRenderers(this);

        match.pieceEntities = new Entity[match.board.numberCells];

        BitSet pieces = match.board.bitBoards[PieceType.WHITE.bitBoardId()];
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            if (i == match.king) {
                match.pieceEntities[i] = efs.createPiece(match, i, PieceType.KING);
            } else {
                match.pieceEntities[i] = efs.createPiece(match, i, PieceType.WHITE);
            }
        }

        pieces = match.board.bitBoards[PieceType.BLACK.bitBoardId()];
        for (int i = pieces.nextSetBit(0); i >= 0; i = pieces.nextSetBit(i+1)) {
            match.pieceEntities[i] = efs.createPiece(match, i, PieceType.BLACK);
        }
    }
}
