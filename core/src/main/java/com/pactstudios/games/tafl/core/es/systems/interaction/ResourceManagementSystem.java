package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;

public class ResourceManagementSystem extends EntityProcessingSystem {

    ComponentMapper<HudComponent> hudMapper;

    EntityFactorySystem efs;
    SingletonComponentManager manager;

    @SuppressWarnings("unchecked")
    public ResourceManagementSystem() {
        super( Aspect.getAspectForAll(HudComponent.class));
    }

    @Override
    public void initialize() {
        super.initialize();
        efs = world.getSystem(EntityFactorySystem.class);
        manager = world.getManager(SingletonComponentManager.class);
        hudMapper = world.getMapper(HudComponent.class);
    }

    @Override
    protected void process(Entity e) {
        HudComponent infoComponent = hudMapper.get(e);

        infoComponent.match.timer += world.getDelta();

        if (Constants.GameConstants.DEBUG) {
            infoComponent.fps = Gdx.graphics.getFramesPerSecond();
        }
    }
}
