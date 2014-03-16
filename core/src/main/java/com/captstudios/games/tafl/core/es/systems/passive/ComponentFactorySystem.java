package com.captstudios.games.tafl.core.es.systems.passive;

import com.artemis.Component;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.es.HudFactory;
import com.captstudios.games.tafl.core.es.TaflWorld;
import com.captstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.captstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.captstudios.games.tafl.core.es.components.render.AiProcessingComponent;
import com.captstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.captstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.captstudios.games.tafl.core.es.components.render.HighlightComponent;
import com.captstudios.games.tafl.core.es.components.render.OffsetComponent;
import com.captstudios.games.tafl.core.es.components.render.ScallingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.captstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;
import com.captstudios.games.tafl.core.es.model.TaflMatch;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.utils.device.DeviceSettings;
import com.roundtriangles.games.zaria.services.GraphicsService;

public class ComponentFactorySystem extends PassiveEntitySystem {

    GraphicsService graphics;

    public ComponentFactorySystem(GraphicsService graphics) {
        this.graphics = graphics;
    }

    protected <T extends Component> T createComponent(Class<T> type) {
        return world.createComponent(type);
    }

    public PositionComponent createPositionComponent(Vector2 position) {
        PositionComponent component = createComponent(PositionComponent.class);
        component.position.set(position);
        return component;
    }

    public PositionComponent createPositionComponent(float x, float y) {
        PositionComponent component = createComponent(PositionComponent.class);
        component.position.set(x, y);
        return component;
    }

    public VelocityComponent createVelocityComponent(Move move, Vector2 velocity, float distanceRemaining) {
        VelocityComponent component = createComponent(VelocityComponent.class);
        component.move = move;
        component.velocity = velocity;
        component.distanceRemaining = distanceRemaining;
        return component;
    }

    public DrawableComponent createDrawableComponent(String atlasName, String spriteName) {
        DrawableComponent component = createComponent(DrawableComponent.class);
        component.sprite = graphics.getSprite(atlasName, spriteName);
        component.name = spriteName;
        return component;
    }

    public AnimationComponent createAnimationComponent(String atlasName, String animationName, int playType, float duration) {
        AnimationComponent component = createComponent(AnimationComponent.class);
        component.animation = graphics.getAnimation(atlasName, animationName, playType, duration);
        component.name = animationName;
        return component;
    }

    public OffsetComponent createOffsetComponent(float xOffset, float yOffset) {
        OffsetComponent component = createComponent(OffsetComponent.class);
        component.xOffset = xOffset;
        component.yOffset = yOffset;
        return component;
    }

    public ScallingComponent createScalingComponent(float xScale, float yScale) {
        ScallingComponent component = createComponent(ScallingComponent.class);
        component.xScale = xScale;
        component.yScale = yScale;
        return component;
    }

    public MatchComponent createMatchComponent(TaflMatch match) {
        MatchComponent component = createComponent(MatchComponent.class);
        component.match = match;
        return component;
    }

    public HudComponent createHudComponent(TaflMatch match) {
        HudComponent component = createComponent(HudComponent.class);
        component.match = match;
        return component;
    }

    public MatchRenderingComponent createMapRenderingComponent(TaflWorld world, TaflMatch match) {
        MatchRenderingComponent component = createComponent(MatchRenderingComponent.class);
        component.camera = world.camera;

        DeviceSettings ds = world.game.deviceSettings;

        component.spriteBatch = world.spriteBatch;
        component.shapeRenderer = world.shapeRenderer;
        component.font = graphics.getFont(ds.gameFont);
        component.debugFont = graphics.getFont(ds.rulesFont);

        component.backgroundTexture = graphics.getSprite(
                Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.BOARD);
        component.gridTexture = graphics.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, match.board.boardType.grid);

        if (!world.game.deviceSettings.isIpad()) {
            component.braid = graphics.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.Decoration.BRAID);
        }

        return component;
    }

    public HudRenderingComponent createHudRenderingComponent(TaflWorld world) {
        HudRenderingComponent component = createComponent(HudRenderingComponent.class);
        HudFactory.populateHudRenderingComponent(component, world);
        return component;
    }

    public HighlightComponent createHilightComponent(int cellId, Color color) {
        HighlightComponent component = createComponent(HighlightComponent.class);
        component.cellId = cellId;
        component.color = color;
        return component;
    }

    public AiProcessingComponent createAiProcessingComponent() {
        AiProcessingComponent component = createComponent(AiProcessingComponent.class);
        return component;
    }
}
