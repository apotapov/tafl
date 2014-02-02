package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem2;
import com.pactstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.PlayerWarningEvent;

public class LifeCycleSystem extends EventProcessingSystem2<LifeCycleEvent, PlayerWarningEvent> {

    ComponentMapper<HudRenderingComponent> hudRendMapper;

    TaflWorld gameWorld;
    Array<LifeCycleEvent> lifecycleEvents;

    @SuppressWarnings("unchecked")
    public LifeCycleSystem(TaflWorld gameWorld) {
        super(Aspect.getAspectForAll(HudRenderingComponent.class),
                LifeCycleEvent.class, PlayerWarningEvent.class);
        this.gameWorld = gameWorld;
        lifecycleEvents = new Array<LifeCycleEvent>();
    }

    @Override
    public void initialize() {
        super.initialize();
        hudRendMapper = world.getMapper(HudRenderingComponent.class);
    }

    @Override
    protected void processEvent2(Entity e, PlayerWarningEvent event) {
        HudRenderingComponent component = hudRendMapper.get(e);

        gameWorld.pauseSystems();

        component.playerWarningText.setText(gameWorld.game.localeService.get(event.playerWarning.text));
        component.playerWarningDialog.show(component.hubStage);
    }

    @Override
    protected void processEvent(Entity e, LifeCycleEvent event) {
        HudRenderingComponent component = hudRendMapper.get(e);
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

    private void win(HudRenderingComponent component, int winner) {
        gameWorld.pauseSystems();

        gameWorld.match.status = LifeCycle.WIN;
        gameWorld.game.databaseService.updateMatch(gameWorld.match);

        if (winner == Constants.BoardConstants.WHITE_TEAM) {
            component.winText.setText(
                    gameWorld.game.localeService.get(LocalizedStrings.GameMenu.WHITE_WIN_TEXT));
        } else {
            component.winText.setText(
                    gameWorld.game.localeService.get(LocalizedStrings.GameMenu.BLACK_WIN_TEXT));
        }
        component.winDialog.show(component.hubStage);
    }


    private void draw(HudRenderingComponent component, DrawReasonEnum drawReason) {
        gameWorld.pauseSystems();

        gameWorld.match.status = LifeCycle.DRAW;
        gameWorld.game.databaseService.updateMatch(gameWorld.match);

        component.drawText.setText(gameWorld.game.localeService.get(drawReason.text));
        component.drawDialog.show(component.hubStage);
    }

    private void play() {
        gameWorld.resumeSystems();
    }
}
