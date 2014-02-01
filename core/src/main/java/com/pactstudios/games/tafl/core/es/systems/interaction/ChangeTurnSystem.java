package com.pactstudios.games.tafl.core.es.systems.interaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.Team;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.systems.events.AiTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.ChangeTurnEvent;
import com.pactstudios.games.tafl.core.es.systems.events.EventProcessingSystem;
import com.pactstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

public class ChangeTurnSystem extends EventProcessingSystem<ChangeTurnEvent> {

    ComponentMapper<MatchComponent> matchMapper;

    CellHighlightSystem highlightSystem;

    TaflDatabaseService dbService;

    @SuppressWarnings("unchecked")
    public ChangeTurnSystem(TaflDatabaseService dbService) {
        super(Aspect.getAspectForAll(MatchComponent.class), ChangeTurnEvent.class);
        this.dbService = dbService;
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

        // Check if the game is over before we do anything.
        if (!checkEndGame(match)) {
            match.rulesEngine.changeTurn();
            dbService.updateMatch(match);

            match.rulesEngine.calculateLegalMoves();

            // Need the turn switched and moves calculated before checking
            // for draw
            if (!checkDraw(match)) {
                if (match.versusComputer &&
                        match.turn == match.computerTeam) {
                    AiTurnEvent aiTurn = world.createEvent(AiTurnEvent.class);
                    world.postEvent(this, aiTurn);
                }
                highlightSystem.highlightTeam(match.turn);
            }
        }
        matchComponent.animationInProgress = false;
    }

    private boolean checkEndGame(TaflMatch match) {
        Team winner = match.rulesEngine.checkWinner();
        if (winner != null) {
            LifeCycle lifecycle = LifeCycle.WIN;
            if (match.versusComputer && match.computerTeam == winner) {
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
        DrawReasonEnum drawReason = match.rulesEngine.checkDraw();
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
