package com.captstudios.games.tafl.core.es.components.singleton;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MatchRenderingComponent implements RenderingComponent {

    public OrthographicCamera camera;
    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public BitmapFont debugFont;
    public Sprite backgroundTexture;
    public Sprite gridTexture;
    public Sprite braid;

    @Override
    public void reset() {
        camera = null;
        spriteBatch.dispose();
        spriteBatch = null;
        shapeRenderer.dispose();
        shapeRenderer = null;
        font = null;
        debugFont = null;
        backgroundTexture = null;
        gridTexture = null;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

}
