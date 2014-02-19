package com.pactstudios.games.tafl.core.es;

import com.artemis.systems.EntitySystem;
import com.artemis.systems.event.BasicEventDeliverySystem;
import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.es.systems.input.HudInputSystem;
import com.pactstudios.games.tafl.core.es.systems.input.MatchInputSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.AiSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.ChangeTurnSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.PieceCaptureSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.PieceMoveAnimationSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.PieceMovementSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.ResourceManagementSystem;
import com.pactstudios.games.tafl.core.es.systems.interaction.UndoSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.CellHighlightSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.ComponentFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityFactorySystem;
import com.pactstudios.games.tafl.core.es.systems.passive.EntityPieceSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.LifeCycleSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.SoundSystem;
import com.pactstudios.games.tafl.core.es.systems.passive.UserInputSystem;
import com.pactstudios.games.tafl.core.es.systems.render.AiProcessingRendererSystem;
import com.pactstudios.games.tafl.core.es.systems.render.AnimationRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.CellHighlightRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.GameBoardImageRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.GridRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.HudRenderingSystem;
import com.pactstudios.games.tafl.core.es.systems.render.PieceMovementRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.PreRenderSystem;
import com.pactstudios.games.tafl.core.es.systems.render.SpriteRenderSystem;


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
        gameWorld.world.setSystem(new MatchInputSystem(gameWorld.game));
    }

    protected static void initEventSystem(TaflWorld gameWorld) {
        gameWorld.world.setEventDeliverySystem(new BasicEventDeliverySystem());
    }

    protected static void initActiveSystems(TaflWorld gameWorld, Array<EntitySystem> activeSystems) {
        activeSystems.add(new PieceMoveAnimationSystem());
        activeSystems.add(new ResourceManagementSystem());

        for (EntitySystem system : activeSystems) {
            gameWorld.world.setSystem(system);
        }
    }

    protected static void initPassiveSystems(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new ChangeTurnSystem());

        String text = gameWorld.game.localeService.get(LocalizedStrings.Game.AI_PROCESSING);

        gameWorld.world.setSystem(new AiSystem(text));
        gameWorld.world.setSystem(new UndoSystem());
        gameWorld.world.setSystem(new PieceMovementSystem());
        gameWorld.world.setSystem(new PieceCaptureSystem());
        gameWorld.world.setSystem(new LifeCycleSystem(gameWorld));
        gameWorld.world.setSystem(new ComponentFactorySystem(gameWorld.game.graphicsService));
        gameWorld.world.setSystem(new EntityFactorySystem());
        gameWorld.world.setSystem(new SoundSystem(gameWorld.game.soundService));
        gameWorld.world.setSystem(new CellHighlightSystem());
        gameWorld.world.setSystem(new EntityPieceSystem());
    }

    protected static void initRenderingSystems(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new PreRenderSystem());
        gameWorld.world.setSystem(new GameBoardImageRenderSystem());
        gameWorld.world.setSystem(new PieceMovementRenderSystem());
        gameWorld.world.setSystem(new CellHighlightRenderSystem());
        gameWorld.world.setSystem(new GridRenderSystem());
        gameWorld.world.setSystem(new SpriteRenderSystem());
        gameWorld.world.setSystem(new AnimationRenderSystem());
        gameWorld.world.setSystem(new AiProcessingRendererSystem());
    }

    protected static void initDebugRenderingSystems(TaflWorld gameWorld) {
        //        gameWorld.world.setSystem(new DebugRenderSystem());
        //        gameWorld.world.setSystem(new DebugCellIdRendererSystem());
    }

    protected static void initHudRenderingSystems(TaflWorld gameWorld) {
        gameWorld.world.setSystem(new HudRenderingSystem());
    }

}
