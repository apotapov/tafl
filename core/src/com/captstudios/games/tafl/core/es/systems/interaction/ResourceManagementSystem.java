package com.captstudios.games.tafl.core.es.systems.interaction;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.captstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;

public class ResourceManagementSystem extends EntityProcessingSystem {

    ComponentMapper<HudComponent> hudMapper;

    EntityFactorySystem efs;
    SingletonComponentManager manager;

    @SuppressWarnings("unchecked")
    public ResourceManagementSystem() {
        super( Filter.allComponents(HudComponent.class));
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

        if (Constants.GameConstants.DEBUG) {
            infoComponent.fps = Gdx.graphics.getFramesPerSecond();
        }
    }
}
