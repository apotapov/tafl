package com.pactstudios.games.tafl.core.es.systems.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.roundtriangles.games.zaria.services.GraphicsService;
import com.roundtriangles.games.zaria.services.resources.LocaleService;
import com.roundtriangles.games.zaria.utils.TimeCharSequence;

public class HudRenderingSystem extends RenderingSystem<HudRenderingComponent> {

    ComponentMapper<HudComponent> hudMapper;

    LocaleService localeService;
    GraphicsService graphicService;

    TimeCharSequence time;

    @SuppressWarnings("unchecked")
    public HudRenderingSystem(LocaleService localeService, GraphicsService graphicService) {
        super(Aspect.getAspectForAll(HudComponent.class), HudRenderingComponent.class);
        this.localeService = localeService;
        this.graphicService = graphicService;

        time = new TimeCharSequence();
    }

    @Override
    public void initialize() {
        super.initialize();
        hudMapper = world.getMapper(HudComponent.class);
    }

    @Override
    protected void process(Entity e, HudRenderingComponent rendComponent) {
        HudComponent hudComponent = hudMapper.get(e);
        updateTurn(rendComponent, hudComponent);
        updateTime(rendComponent, hudComponent);
        updateDebugInfo(rendComponent, hudComponent);

        rendComponent.hubStage.act(world.getDelta());
        rendComponent.hubStage.draw();

    }

    private void updateDebugInfo(HudRenderingComponent rendComponent,
            HudComponent hudComponent) {
        if (Constants.GameConstants.DEBUG) {
            rendComponent.fps.setText("FPS: " + hudComponent.fps);
        }
    }

    private void updateTime(HudRenderingComponent rendComponent,
            HudComponent hudComponent) {
        time.setTime((int) hudComponent.match.timer);
        rendComponent.timer.setText(time);
    }

    private void updateTurn(HudRenderingComponent rendComponent,
            HudComponent hudComponent) {
        String text;
        if (!hudComponent.match.versusComputer || hudComponent.match.computerTeam != hudComponent.match.turn) {
            if (hudComponent.match.turn == Team.WHITE) {
                text = localeService.get(LocalizedStrings.Hud.WHITE_TURN_LABEL);
            } else {
                text = localeService.get(LocalizedStrings.Hud.BLACK_TURN_LABEL);
            }
        } else {
            text = localeService.get(LocalizedStrings.Hud.COMPUTER_TURN_LABEL);
        }
        rendComponent.turn.setText(text);
    }



    @Override
    protected void begin(HudRenderingComponent rendComponent) {
    }

    @Override
    protected void end(HudRenderingComponent rendComponent) {
    }
}
