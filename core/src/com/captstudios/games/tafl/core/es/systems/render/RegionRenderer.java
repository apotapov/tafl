package com.captstudios.games.tafl.core.es.systems.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.captstudios.games.tafl.core.es.components.movement.PositionComponent;
import com.captstudios.games.tafl.core.es.components.render.DrawableComponent;
import com.captstudios.games.tafl.core.es.components.render.OffsetComponent;
import com.captstudios.games.tafl.core.es.components.render.ScallingComponent;
import com.captstudios.games.tafl.core.es.components.singleton.MatchRenderingComponent;

public class RegionRenderer {
    ComponentMapper<PositionComponent> positionMapper;
    ComponentMapper<DrawableComponent> drawableMapper;
    ComponentMapper<OffsetComponent> offsetMapper;
    ComponentMapper<ScallingComponent> scallingMapper;

    public RegionRenderer(World world) {
        positionMapper = world.getMapper(PositionComponent.class);
        offsetMapper = world.getMapper(OffsetComponent.class);
        scallingMapper = world.getMapper(ScallingComponent.class);
    }

    public void drawRegion(MatchRenderingComponent rendComponent, Entity e, TextureRegion region) {
        PositionComponent position = positionMapper.get(e);
        OffsetComponent offset = offsetMapper.get(e);
        ScallingComponent scalling = scallingMapper.get(e);

        int width = region.getRegionWidth();
        int height = region.getRegionHeight();
        float halfWidth = (float) (width / 2.0);
        float halfHeight = (float) (height / 2.0);

        float xScale = 1;
        float yScale = 1;
        if (scalling != null) {
            xScale = scalling.xScale;
            yScale = scalling.yScale;
        }

        float xOffset = -halfWidth;
        float yOffset = -halfHeight;

        if (offset != null) {
            xOffset = offset.xOffset * scalling.xScale;
            yOffset = offset.yOffset * scalling.yScale;
        }

        float angle = 0;
        int flipHorizontal = 1;
        int flipVertical = 1;

        float positionX = position.position.x + xOffset;
        if (flipHorizontal < 0) {
            positionX += width * scalling.xScale;
        }

        float positionY = position.position.y + yOffset;
        if (flipVertical < 0) {
            positionX += height * scalling.yScale;
        }

        rendComponent.spriteBatch.draw(
                region,
                positionX,
                positionY,
                halfWidth,
                halfHeight,
                width * flipHorizontal,
                height * flipVertical,
                xScale,
                yScale,
                angle);
    }

}
