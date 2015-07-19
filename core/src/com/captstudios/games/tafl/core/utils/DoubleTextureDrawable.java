package com.captstudios.games.tafl.core.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class DoubleTextureDrawable extends TextureRegionDrawable {

    TextureRegion inner;

    public DoubleTextureDrawable(TextureRegion region, TextureRegion inner) {
        super(region);
        setInnerRegion(inner);
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        super.draw(batch, x, y, width, height);

        float innerWidth = inner.getRegionWidth() * width / getMinWidth();
        float innerHeight = inner.getRegionHeight() * height / getMinHeight();
        if (inner.getRegionWidth() / getMinWidth() > 0.9) {
            innerWidth *= 0.9f;
            innerHeight *= 0.9f;
        }

        float innerX = x + (width - innerWidth) / 2;
        float innerY = y + (height - innerHeight) / 2;

        batch.draw(inner, innerX, innerY, innerWidth, innerHeight);
    }

    @Override
    public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX,
            float scaleY, float rotation) {
        super.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);

        float innerWidth = inner.getRegionWidth() * width / getMinWidth();
        float innerHeight = inner.getRegionHeight() * height / getMinHeight();

        float innerX = x;
        float innerY = y;

        batch.draw(inner, innerX, innerY, originX, originY, innerWidth, innerHeight, scaleX, scaleY, rotation);
    }

    public void setInnerRegion(TextureRegion inner) {
        this.inner = inner;
    }

    public TextureRegion getInnerRegion() {
        return inner;
    }
}
