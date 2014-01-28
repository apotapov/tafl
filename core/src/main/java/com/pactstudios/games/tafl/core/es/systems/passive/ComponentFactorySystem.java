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
import com.pactstudios.games.tafl.core.es.components.interaction.PieceComponent;
import com.pactstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.pactstudios.games.tafl.core.es.components.movement.VelocityComponent;
import com.pactstudios.games.tafl.core.es.components.render.AnimationComponent;
import com.pactstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.pactstudios.games.tafl.core.es.components.render.HighlightComponent;
import com.pactstudios.games.tafl.core.es.components.render.OffsetComponent;
import com.pactstudios.games.tafl.core.es.components.render.ScallingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.HudComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MapRenderingComponent;
import com.pactstudios.games.tafl.core.es.components.singleton.MatchComponent;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.utils.BoardUtils;
import com.roundtriangles.games.zaria.services.GraphicsService;

public class ComponentFactorySystem extends PassiveEntitySystem {

    GraphicsService graphics;
    Vector2 position;
    Vector2 end;

    public ComponentFactorySystem(GraphicsService graphics) {
        this.graphics = graphics;
        position = new Vector2();
        end = new Vector2();
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

    public VelocityComponent createVelocityComponent(Move move) {
        VelocityComponent component = createComponent(VelocityComponent.class);
        component.move = move;
        position.set(BoardUtils.getTilePosition(move.start));
        component.distanceRemaining = position.dst(BoardUtils.getTilePosition(move.end));
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

    public MatchComponent createBoardComponent(TaflMatch match) {
        MatchComponent component = createComponent(MatchComponent.class);
        component.match = match;
        return component;
    }

    public HudComponent createHudComponent(TaflMatch match) {
        HudComponent component = createComponent(HudComponent.class);
        component.match = match;
        return component;
    }

    public MapRenderingComponent createMapRenderingComponent(TaflWorld world) {
        MapRenderingComponent component = createComponent(MapRenderingComponent.class);
        component.camera = world.camera;

        component.spriteBatch = new SpriteBatch(Constants.GameConstants.BATCH_SIZE);
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

    public PieceComponent createPieceComponent(GamePiece piece) {
        PieceComponent component = createComponent(PieceComponent.class);
        component.piece = piece;
        return component;
    }
}
