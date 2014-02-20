package com.captstudios.games.tafl.core.es.systems.passive;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.systems.event.EventProcessingSystem2;
import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.enums.DrawReasonEnum;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.TaflWorld;
import com.captstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.captstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.captstudios.games.tafl.core.es.systems.events.PlayerWarningEvent;

public class LifeCycleSystem extends EventProcessingSystem2<LifeCycleEvent, PlayerWarningEvent> {

    ComponentMapper<HudRenderingComponent> hudRendMapper;

    TaflWorld gameWorld;
    Array<LifeCycleEvent> lifecycleEvents;

    @SuppressWarnings("unchecked")
    public LifeCycleSystem(TaflWorld gameWorld) {
        super(Filter.allComponents(HudRenderingComponent.class),
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

        if (event.lifecycle == LifeCycle.PLAY) {
            gameWorld.resumeSystems();
        } else {
            gameWorld.pauseSystems();
            if (event.lifecycle == LifeCycle.MENU) {
                displayMenu(component);
            } else {
                gameWorld.match.gameOver(event.lifecycle);

                switch (event.lifecycle) {
                case WIN:
                    win(component, event.winner);
                    break;
                case LOSS:
                    loss(component);
                    break;
                case DRAW:
                    draw(component, event.drawReason);
                    break;
                case SURRENDER:
                    surrender(component);
                    break;
                default:
                }
            }
        }
    }

    private void displayMenu(HudRenderingComponent component) {
        if (!LifeCycle.GAME_OVER.contains(gameWorld.lifecycle)) {
            component.menu.show(component.hubStage);
        }
    }

    private void loss(HudRenderingComponent component) {
        component.lossDialog.show(component.hubStage);
    }

    private void win(HudRenderingComponent component, int winner) {
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
        component.drawText.setText(gameWorld.game.localeService.get(drawReason.text));
        component.drawDialog.show(component.hubStage);
    }

    private void surrender(HudRenderingComponent component) {
        component.surrenderDialog.show(component.hubStage);
    }

}
