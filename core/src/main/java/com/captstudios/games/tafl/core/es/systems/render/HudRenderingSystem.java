package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.Filter;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.captstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.roundtriangles.games.zaria.utils.TimeModifiableString;

public class HudRenderingSystem extends RenderingSystem<HudRenderingComponent> {

    ComponentMapper<HudComponent> hudMapper;

    TimeModifiableString time;

    @SuppressWarnings("unchecked")
    public HudRenderingSystem() {
        super(Filter.allComponents(HudComponent.class), HudRenderingComponent.class);

        time = new TimeModifiableString();
    }

    @Override
    public void initialize() {
        super.initialize();
        hudMapper = world.getMapper(HudComponent.class);
    }

    @Override
    protected void process(Entity e, HudRenderingComponent rendComponent) {
        HudComponent hudComponent = hudMapper.get(e);

        time.setTime((int) hudComponent.match.timer);
        rendComponent.timer.setText(time.toString());

        if (Constants.GameConstants.DEBUG) {
            rendComponent.fps.setText("FPS: " + hudComponent.fps);
        }

        rendComponent.hubStage.act(world.getDelta());
        rendComponent.hubStage.draw();
    }


    @Override
    protected void begin(HudRenderingComponent rendComponent) {
    }

    @Override
    protected void end(HudRenderingComponent rendComponent) {
    }
}