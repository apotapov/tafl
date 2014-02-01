package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.Team;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.events.LifeCycleEvent;

public class LifeCycleSystem extends EntityProcessingSystem {

    ComponentMapper<HudRenderingComponent> hudRendMapper;

    TaflWorld gameWorld;
    Array<LifeCycleEvent> lifecycleEvents;

    @SuppressWarnings("unchecked")
    public LifeCycleSystem(TaflWorld gameWorld) {
        super(Aspect.getAspectForAll(HudRenderingComponent.class));
        this.gameWorld = gameWorld;
        lifecycleEvents = new Array<LifeCycleEvent>();
    }

    @Override
    public void initialize() {
        super.initialize();
        hudRendMapper = world.getMapper(HudRenderingComponent.class);
    }

    @Override
    protected void process(Entity e) {
        HudRenderingComponent component = hudRendMapper.get(e);

        world.getEvents(this, LifeCycleEvent.class, lifecycleEvents);
        for (LifeCycleEvent event : lifecycleEvents) {
            gameWorld.lifecycle = event.lifecycle;
            switch (event.lifecycle) {
            case MENU:
                displayMenu(component);
                break;
            case WIN:
                win(component, event.winner);
                break;
            case LOSS:
                loss(component);
                break;
            case DRAW:
                draw(component, event.drawReason);
                break;
            case PLAY:
                play();
                break;
            default:
                continue;
            }
        }
    }

    private void displayMenu(HudRenderingComponent component) {
        if (gameWorld.lifecycle != LifeCycle.WIN &&
                gameWorld.lifecycle != LifeCycle.LOSS &&
                gameWorld.lifecycle != LifeCycle.DRAW) {
            gameWorld.pauseSystems();
            component.menu.show(component.hubStage);
        }
    }

    private void loss(HudRenderingComponent component) {
        gameWorld.pauseSystems();

        gameWorld.match.status = LifeCycle.LOSS;
        gameWorld.game.databaseService.updateMatch(gameWorld.match);

        component.lossDialog.show(component.hubStage);
    }

    private void win(HudRenderingComponent component, Team winner) {
        gameWorld.pauseSystems();

        gameWorld.match.status = LifeCycle.WIN;
        gameWorld.game.databaseService.updateMatch(gameWorld.match);

        if (winner == Team.WHITE) {
            component.whiteWinDialog.show(component.hubStage);
        } else {
            component.blackWinDialog.show(component.hubStage);
        }
    }


    private void draw(HudRenderingComponent component, DrawReasonEnum drawReason) {
        gameWorld.pauseSystems();

        gameWorld.match.status = LifeCycle.DRAW;
        gameWorld.game.databaseService.updateMatch(gameWorld.match);

        component.drawDialogText.setText(gameWorld.game.localeService.get(drawReason.text));
        component.drawDialog.show(component.hubStage);
    }

    private void play() {
        gameWorld.resumeSystems();
    }
}
