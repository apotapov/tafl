package com.pactstudios.games.tafl.core.utils;

import com.artemis.World;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.systems.events.AiCompleteEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.pactstudios.games.tafl.core.es.systems.interaction.AiSystem;

public class AiThread extends Thread {

    public World world;
    public AiSystem runner;

    public TaflMatch match;

    public AiThread(World world, AiSystem runner) {
        this.world = world;
        this.runner = runner;
    }

    @Override
    public void run() {
        try {
            Move move = match.aiStrategy.search(match);
            if (move != null) {
                AiCompleteEvent completeEvent = world.createEvent(AiCompleteEvent.class);
                completeEvent.move = move;
                world.postEvent(runner, completeEvent);
            } else {
                LifeCycleEvent lce = world.createEvent(LifeCycleEvent.class);
                lce.lifecycle = LifeCycle.SURRENDER;
                world.postEvent(runner, lce);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

            LifeCycleEvent lce = world.createEvent(LifeCycleEvent.class);
            lce.lifecycle = LifeCycle.SURRENDER;
            world.postEvent(runner, lce);
        }
    }

}
