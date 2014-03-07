package com.captstudios.games.tafl.core.es.systems.interaction;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.systems.event.EventProcessingSystem;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.DrawReasonEnum;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.captstudios.games.tafl.core.es.systems.events.ChangeTurnEvent;
import com.captstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.captstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;

public class ChangeTurnSystem extends EventProcessingSystem<ChangeTurnEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    CellHighlightSystem highlightSystem;

    @SuppressWarnings("unchecked")
    public ChangeTurnSystem() {
        super(Filter.allComponents(MatchComponent.class), ChangeTurnEvent.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        matchMapper = world.getMapper(MatchComponent.class);
        highlightSystem = world.getSystem(CellHighlightSystem.class);
    }

    @Override
    protected void processEvent(Entity e, ChangeTurnEvent event) {
        MatchComponent matchComponent = matchMapper.get(e);
        TaflMatch match = matchComponent.match;

        //TODO MOVE THIS TO RULES ENGINE???

        // Check if the game is over before we do anything.
        if (!checkEndGame(match)) {
            match.changeTurn();

            // Need the turn switched and moves calculated before checking
            // for draw
            if (!checkDraw(match) && match.turn == match.computerTeam) {
                AiTurnEvent aiTurn = world.createEvent(AiTurnEvent.class);
                world.postEvent(this, aiTurn);
            }
        }
        matchComponent.animationInProgress = false;
    }

    private boolean checkEndGame(TaflMatch match) {
        int winner = match.board.rules.checkWinner();
        if (winner != Constants.BoardConstants.NO_TEAM) {
            LifeCycle lifecycle = LifeCycle.WIN;
            if (match.computerTeam == winner) {
                lifecycle = LifeCycle.LOSS;
            }
            LifeCycleEvent event = world.createEvent(LifeCycleEvent.class);
            event.lifecycle = lifecycle;
            event.winner = winner;
            world.postEvent(this, event);
            return true;
        }
        return false;
    }

    private boolean checkDraw(TaflMatch match) {
        DrawReasonEnum drawReason = match.board.rules.checkDraw(match.turn);
        if (drawReason != null) {
            LifeCycleEvent event = world.createEvent(LifeCycleEvent.class);
            event.lifecycle = LifeCycle.DRAW;
            event.drawReason = drawReason;
            world.postEvent(this, event);
            return true;
        }
        return false;
    }
}
