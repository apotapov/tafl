package com.pactstudios.games.tafl.core.es.components.singleton;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MapRenderingComponent implements RenderingComponent {

    public OrthographicCamera camera;
    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;

    @Override
    public void reset() {
        camera = null;
        spriteBatch.dispose();
        spriteBatch = null;
        shapeRenderer.dispose();
        shapeRenderer = null;
        font = null;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

}
