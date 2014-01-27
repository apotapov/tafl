package com.pactstudios.games.tafl.core.es;

import com.artemis.systems.EntitySystem;
import com.artemis.systems.event.BasicEventSystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.systems.input.HudInputSystem;
import com.pactstudios.games.tafl.core.es.systems.input.MatchInputSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.MovementSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.PieceCaptureSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.PieceMovementSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.ResourceManagementSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.UndoRedoSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.ComponentFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.LifecycleSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.SoundSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.UserInputSystem;
import com.pactstudios.games.tafl.core.es.systems.render.AnimationRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.CellHighlightRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.GameBoardRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.HudRenderingSystem;
import com.pactstudios.games.tafl.core.es.systems.render.PreRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.SpriteRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.debug.VelocityRenderSystem;


public class SystemFactory {

    public static Array<EntitySystem> initSystems(TaflWorld gameWorld, Array<EntitySystem> activeSystems) {
        initEventSystem(gameWorld);
        initInputSystems(gameWorld);
        initActiveSystems(gameWorld, activeSystems);
        initRenderingSystems(gameWorld);
        initDebugRenderingSystems(gameWorld);
        initHudRenderingSystems(gameWorld);
        initPassiveSystems(gameWorld);
        return activeSystems;
    }

    protected static void initInputSystems(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new UserInputSystem());
        gameWorld.world.setSystem(new HudInputSystem());
        gameWorld.world.setSystem(new MatchInputSystem());
    }

    protected static void initEventSystem(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new BasicEventSystem());
    }

    protected static void initActiveSystems(TaflWorld gameWorld, Array<EntitySystem> activeSystems) {
        activeSystems.add(new MovementSystem());
        activeSystems.add(new ResourceManagementSystem());

        for (EntitySystem system : activeSystems) {
            gameWorld.world.setSystem(system);
        }
    }

    protected static void initPassiveSystems(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new UndoRedoSystem(gameWorld.game.databaseService));
        gameWorld.world.setSystem(new PieceMovementSystem(gameWorld.game.databaseService));
        gameWorld.world.setSystem(new PieceCaptureSystem(gameWorld.game.databaseService));
        gameWorld.world.setSystem(new LifecycleSystem(gameWorld));
        gameWorld.world.setSystem(new ComponentFactorySystem(gameWorld.game.graphicsService));
        gameWorld.world.setSystem(new EntityFactorySystem());
        gameWorld.world.setSystem(new SoundSystem(gameWorld.game.soundService));
        gameWorld.world.setSystem(new CellHighlightSystem());
    }

    protected static void initRenderingSystems(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new PreRenderSystem());
        gameWorld.world.setSystem(new GameBoardRenderSystem());
        gameWorld.world.setSystem(new CellHighlightRenderSystem());
        gameWorld.world.setSystem(new SpriteRenderSystem());
        gameWorld.world.setSystem(new AnimationRenderSystem());
    }

    protected static void initDebugRenderingSystems(TaflWorld gameWorld) {
        //        gameWorld.world.setSystem(new DebugRenderSystem());
        //gameWorld.world.setSystem(new CellIdRendererSystem());
        gameWorld.world.setSystem(new VelocityRenderSystem());
    }

    protected static void initHudRenderingSystems(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new HudRenderingSystem(gameWorld.game.localeService,
                gameWorld.game.graphicsService));
    }

}
