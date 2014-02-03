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
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityPieceSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.SoundSystem;
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

    public void initialize() {
        this.world = new World();
        this.camera = new Bounded2DCamera();
        this.activeSystems = new Array<EntitySystem>();

        //world.setManager(new TaflManager());
        world.setManager(new SingletonComponentManager());
        world.setManager(new GroupManager());
        SystemFactory.initSystems(this, activeSystems);
        world.initialize();

        createEntities();

        match.initialize(game.databaseService,
                world.getSystem(EntityPieceSystem.class),
                world.getSystem(CellHighlightSystem.class),
                world.getSystem(SoundSystem.class));

        lifecycle = LifeCycle.PLAY;

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
        this.camera.position.set(0, 0, 0);
        this.camera.viewportWidth = Constants.GameConstants.GAME_WIDTH;
        this.camera.viewportHeight = Constants.GameConstants.GAME_HEIGHT;
        //this.camera.zoom = boardSize / width;
        this.stage.setViewport(width, height, true);
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

        match.gameOver(LifeCycle.RESTART);
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
            level = game.levelService.getLevel(game.preferenceService.getLevel());
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

    protected void createEntities() {
        EntityFactorySystem efs = world.getSystem(EntityFactorySystem.class);
        efs.createMatch(match);
        efs.createHud(match);
        efs.createRenderers(this);
    }
}
