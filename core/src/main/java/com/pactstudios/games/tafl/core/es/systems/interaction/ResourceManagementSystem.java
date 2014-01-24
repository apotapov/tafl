package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.events.MouseMoveEvent;
import com.pactstudios.games.tafl.core.es.systems.input.InputUtil;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;

public class ResourceManagementSystem extends EntityProcessingSystem {

    ComponentMapper<HudComponent> hudMapper;

    EntityFactorySystem efs;
    SingletonComponentManager manager;
    Array<MouseMoveEvent> mouseMoves;

    @SuppressWarnings("unchecked")
    public ResourceManagementSystem() {
        super( Aspect.getAspectForAll(HudComponent.class));
        mouseMoves = new Array<MouseMoveEvent>();
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
            world.getEvents(this, MouseMoveEvent.class, mouseMoves);
            if (mouseMoves.size > 0) {
                MouseMoveEvent last = mouseMoves.get(mouseMoves.size - 1);
                infoComponent.mouseLocation.set(InputUtil.translateTouchPoint(
                        manager.getSingletonComponent(MapRenderingComponent.class).camera, last.x, last.y));
            }
            infoComponent.fps = Gdx.graphics.getFramesPerSecond();


        }
    }
}
