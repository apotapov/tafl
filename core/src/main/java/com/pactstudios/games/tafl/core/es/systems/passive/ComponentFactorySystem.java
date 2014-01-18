package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.Component;
import com.artemis.systems.PassiveEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.HudFactory;
import com.pactstudios.games.tafl.core.es.TaflWorld;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.pactstudios.games.tafl.core.es.components.render.DirectionComponent;
import com.pactstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.pactstudios.games.tafl.core.es.components.render.HighlightComponent;
import com.pactstudios.games.tafl.core.es.components.render.OffsetComponent;
import com.pactstudios.games.tafl.core.es.components.render.ScallingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.model.map.TaflMap;
import com.pactstudios.games.tafl.core.es.model.map.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.map.objects.Team;
import com.pactstudios.games.tafl.core.level.TaflLevel;
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

    public VelocityComponent createVelocityComponent(Vector2 velocity, float maxAngleChange) {
        VelocityComponent component = createComponent(VelocityComponent.class);
        component.velocity.set(velocity);
        component.maxAngleChange = maxAngleChange;
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
        component.animation = graphics.getAntimaion(atlasName, animationName, playType, duration);
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

    public DirectionComponent createDirectionComponent() {
        DirectionComponent component = createComponent(DirectionComponent.class);
        return component;
    }

    public MapComponent createMapComponent(TaflMap map) {
        MapComponent component = createComponent(MapComponent.class);
        component.map = map;
        component.turn = Team.BLACK;
        return component;
    }

    public HudComponent createHudComponent(TaflLevel level) {
        HudComponent component = createComponent(HudComponent.class);
        return component;
    }

    public MapRenderingComponent createMapRenderingComponent(TaflWorld world) {
        MapRenderingComponent component = createComponent(MapRenderingComponent.class);
        component.camera = world.camera;

        component.spriteBatch = new SpriteBatch(Constants.Game.BATCH_SIZE);
        component.shapeRenderer = new ShapeRenderer();
        component.font = graphics.getFont(Assets.Fonts.BLOWHOLE_FONT_GAME);

        return component;
    }

    public HudRenderingComponent createHudRenderingComponent(TaflWorld world) {
        HudRenderingComponent component = createComponent(HudRenderingComponent.class);
        HudFactory.populateHudRenderingComponent(component, world);
        return component;
    }

    public HighlightComponent createHilightComponent(ModelCell cell, Color color) {
        HighlightComponent component = createComponent(HighlightComponent.class);
        component.cell = cell;
        component.color = color;
        return component;
    }
}
