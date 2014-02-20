package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.Filter;
import com.artemis.Entity;
import com.artemis.managers.SingletonComponentManager;
import com.artemis.systems.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.es.components.singleton.RenderingComponent;

public abstract class RenderingSystem<T extends RenderingComponent> extends EntitySystem {

    Class<T> type;

    SingletonComponentManager manager;

    public RenderingSystem(Filter aspect, Class<T> type) {
        super(aspect);
        this.type = type;
    }

    @Override
    public void initialize() {
        super.initialize();
        manager = world.getManager(SingletonComponentManager.class);
    }

    @Override
    protected void processEntities(Array<Entity> entities) {
        T rendComponent = manager.getSingletonComponent(type);
        begin(rendComponent);
        for (Entity entity : entities) {
            process(entity, rendComponent);
        }
        end(rendComponent);
    }

    protected abstract void begin(T rendComponent);
    protected abstract void end(T rendComponent);
    protected abstract void process(Entity e, T rendComponent);

}
