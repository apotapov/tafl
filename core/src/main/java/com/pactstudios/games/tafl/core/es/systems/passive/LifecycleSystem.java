package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;

public class LifecycleSystem extends EntityProcessingSystem {

    ComponentMapper<HudRenderingComponent> hudRendMapper;

    TaflWorld gameWorld;
    Array<LifecycleEvent> lifecycleEvents;

    @SuppressWarnings("unchecked")
    public LifecycleSystem(TaflWorld gameWorld) {
        super(Aspect.getAspectForAll(HudRenderingComponent.class));
        this.gameWorld = gameWorld;
        lifecycleEvents = new Array<LifecycleEvent>();
    }

    @Override
    public void initialize() {
        super.initialize();
        hudRendMapper = world.getMapper(HudRenderingComponent.class);
    }

    @Override
    protected void process(Entity e) {
        HudRenderingComponent component = hudRendMapper.get(e);

        world.getEvents(this, LifecycleEvent.class, lifecycleEvents);
        for (LifecycleEvent event : lifecycleEvents) {
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
            case PLAY:
                play();
                break;
            default:
                continue;
            }
        }
    }

    private void displayMenu(HudRenderingComponent component) {
        if (gameWorld.lifecycle != Lifecycle.WIN &&
                gameWorld.lifecycle != Lifecycle.LOSS) {
            gameWorld.pauseSystems();
            component.menu.show(component.hubStage);
        }
    }

    private void loss(HudRenderingComponent component) {
        gameWorld.pauseSystems();

        gameWorld.match.status = Lifecycle.LOSS;
        gameWorld.game.databaseService.updateMatch(gameWorld.match);

        component.lossDialog.show(component.hubStage);
    }

    private void win(HudRenderingComponent component, Team winner) {
        gameWorld.pauseSystems();

        gameWorld.match.status = Lifecycle.WIN;
        gameWorld.game.databaseService.updateMatch(gameWorld.match);

        if (winner == Team.WHITE) {
            component.whiteWinDialog.show(component.hubStage);
        } else {
            component.blackWinDialog.show(component.hubStage);
        }
    }

    private void play() {
        gameWorld.resumeSystems();
    }
}
