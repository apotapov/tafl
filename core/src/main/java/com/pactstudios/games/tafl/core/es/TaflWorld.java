package com.pactstudios.games.tafl.core.es;

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
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
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

    public Lifecycle lifecycle;

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

        createLevelObjects();

        lifecycle = Lifecycle.PLAY;
        world.getSystem(CellHighlightSystem.class).highlightTeam(match.turn);
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
        float boardSize = match.dimensions * Constants.BoardConstants.TILE_SIZE;
        float center = boardSize / 2.0f;

        this.camera.position.set(center, center, 0);
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.zoom = boardSize / width;
        this.stage.setViewport(Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT, true);
    }

    public void pause() {
        LifecycleEvent event = SystemEvent.createEvent(LifecycleEvent.class);
        event.lifecycle = Lifecycle.MENU;
        world.postEvent(null, event);
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

        match.status = Lifecycle.RESTART;
        game.databaseService.updateMatch(match);

        createNewMatch();

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

    public boolean createNewMatch() {
        if (level == null && match == null) {
            game.setScreen(game.levelSelectionScreen);
        } else if (level == null) {
            level = game.levelService.getLevel(match.name);
        }
        match = game.levelService.createNewMatch(level);
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

    protected void createLevelObjects() {
        EntityFactorySystem efs = world.getSystem(EntityFactorySystem.class);
        efs.createBoard(match);
        efs.createHud(match);
        efs.createRenderers(this);

        for (GamePiece piece : match.pieces) {
            if (piece.killed == null) {
                ModelCell cell = match.board.getCell(piece.x, piece.y);
                piece.entity = efs.createPiece(piece);
                cell.piece = piece;
            }
        }
    }
}
