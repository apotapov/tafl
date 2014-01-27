package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.roundtriangles.games.zaria.services.GraphicsService;
import com.roundtriangles.games.zaria.services.resources.LocaleService;

public class HudRenderingSystem extends RenderingSystem<HudRenderingComponent> {

    ComponentMapper<HudComponent> hudMapper;

    LocaleService localeService;
    GraphicsService graphicService;

    @SuppressWarnings("unchecked")
    public HudRenderingSystem(LocaleService localeService, GraphicsService graphicService) {
        super(Aspect.getAspectForAll(HudComponent.class), HudRenderingComponent.class);
        this.localeService = localeService;
        this.graphicService = graphicService;
    }

    @Override
    public void initialize() {
        super.initialize();
        hudMapper = world.getMapper(HudComponent.class);
    }

    @Override
    protected void process(Entity e, HudRenderingComponent rendComponent) {
        HudComponent hudComponent = hudMapper.get(e);
        if (hudComponent != null) {
            String text = localeService.get(hudComponent.match.turn.getLocalizedName());
            text = localeService.get(LocalizedStrings.Hud.TURN_LABEL, text);
            rendComponent.turn.setText(text);

            text = localeService.get(LocalizedStrings.Hud.GAME_TIME_LABEL, floatToTime(hudComponent.match.timer));
            rendComponent.timer.setText(text);

            if (Constants.GameConstants.DEBUG) {
                rendComponent.mouseLocation.setText("Mouse: " +
                        (int)hudComponent.mouseLocation.x +
                        ", " +
                        (int)hudComponent.mouseLocation.y);
                rendComponent.fps.setText("FPS: " + hudComponent.fps);
            }

            //rendComponent.log.setItems(hudComponent.log.log.items);

            rendComponent.hubStage.act(world.getDelta());
            rendComponent.hubStage.draw();
        }
    }

    private String floatToTime(float gameTime) {
        int seconds = (int)(gameTime) % 60;
        int minutes = ((int)(gameTime) / 60) % 60;
        return paddedInt(minutes) + ":" + paddedInt(seconds);
    }

    protected String paddedInt(int intVal) {
        String str;
        if (intVal == 0) {
            str = "00";
        } else if (intVal < 10) {
            str = "0" + intVal;
        } else {
            str = Integer.toString(intVal);
        }
        return str;
    }

    @Override
    protected void begin(HudRenderingComponent rendComponent) {
    }

    @Override
    protected void end(HudRenderingComponent rendComponent) {
    }
}
