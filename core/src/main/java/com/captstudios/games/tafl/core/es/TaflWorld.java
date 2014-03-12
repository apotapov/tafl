package com.captstudios.games.tafl.core.es;

import com.artemis.World;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.EntitySystem;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.captstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.captstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.captstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.captstudios.games.tafl.core.es.systems.passive.EntityPieceSystem;
import com.captstudios.games.tafl.core.es.systems.passive.SoundSystem;
import com.captstudios.games.tafl.core.level.TaflLevel;
import com.captstudios.games.tafl.core.utils.HighlightManager;
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
        world.setManager(new HighlightManager());
        SystemFactory.initSystems(this, activeSystems);
        world.initialize();

        match.initialize(
                world.getSystem(EntityPieceSystem.class),
                world.getSystem(CellHighlightSystem.class),
                world.getSystem(SoundSystem.class),
                game.preferenceService);

        createSingletonEntities();

        lifecycle = LifeCycle.PLAY;

        if (match.turn == match.computerTeam) {
            AiTurnEvent aiTurn = world.createEvent(AiTurnEvent.class);
            world.postEvent(null, aiTurn);
        }
    }

    public void render(float delta) {
        switch (lifecycle) {
        case QUIT:
            game.setScreen(game.mainMenuScreen);
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
        float ratioDifference = ((float)Constants.GameConstants.GAME_HEIGHT / Constants.GameConstants.GAME_WIDTH) / (((float) height) / width);

        float gameWidth = Constants.GameConstants.GAME_WIDTH;
        float gameHeight = Constants.GameConstants.GAME_HEIGHT / ratioDifference;

        this.camera.viewportWidth = gameWidth;
        this.camera.viewportHeight = gameHeight;

        float cameraX = 0;
        float cameraY = (Constants.GameConstants.GAME_HEIGHT - gameHeight) / 2;

        this.camera.position.set(cameraX, cameraY, 0);

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
        level = game.levelService.getLevel(game.preferenceService.getLevelIndex());
        match = game.levelService.createNewMatch(level);
        return match != null;
    }

    public boolean loadExistingMatch() {
        TaflMatch match = game.preferenceService.loadMatch();
        if (match != null) {
            this.match = match;
            return true;
        }
        return false;
    }

    protected void createSingletonEntities() {
        EntityFactorySystem efs = world.getSystem(EntityFactorySystem.class);
        efs.createMatch(match);
        efs.createHud(match);
        efs.createRenderers(this, match);
    }
}
