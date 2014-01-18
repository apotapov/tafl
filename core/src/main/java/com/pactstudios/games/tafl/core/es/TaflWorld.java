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
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.pactstudios.games.tafl.core.profile.TaflProfile;
import com.pactstudios.games.tafl.core.profile.TaflProfileService;
import com.roundtriangles.games.zaria.camera.Bounded2DCamera;

public class TaflWorld implements Disposable {

    public TaflGame game;
    public World world;

    public OrthographicCamera camera;
    public Stage stage;

    public TaflLevel level;

    private Array<EntitySystem> activeSystems;

    public Lifecycle lifecycle;

    public TaflWorld(TaflGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }

    public void setLevel(TaflLevel level) {
        this.level = level;
    }

    public void levelComplete() {
        TaflLevel nextLevel = level.nextLevel;
        if (nextLevel != null) {
            TaflProfileService profileService = game.getProfileService();
            TaflProfile profile = profileService.retrieveProfile();
            profile.currentLevel = nextLevel.name;
            profileService.persist();
        }
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

        game.getLevelService().initializeLevel(level, this);

        lifecycle = Lifecycle.PLAY;
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
        float centerX = Constants.Map.MAP_SIZE / 2.0f;
        float centerY = Constants.Map.MAP_SIZE / 2.0f;

        this.camera.position.set(centerX, centerY, 0);
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.zoom = (float)Constants.Map.MAP_SIZE / width;
        this.stage.setViewport(width, height);
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
}
