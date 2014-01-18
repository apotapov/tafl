package com.pactstudios.games.tafl.core.es.systems.render.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HudBackground extends Actor {

    ShapeRenderer renderer = new ShapeRenderer();

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeType.Filled);
        renderer.setColor(this.getColor());
        renderer.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        renderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
        batch.begin();
    }

}
