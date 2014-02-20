package com.captstudios.games.tafl.core.screen;

import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.es.TaflWorld;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class GamePlayScreen extends AbstractScreen<TaflGame> {

    TaflWorld world;

    public GamePlayScreen(TaflGame game) {
        super(game);
        world = new TaflWorld(game, stage);
    }

    public void createNewMatch() {
        world.createNewMatch();
    }

    public boolean loadExistingMatch() {
        return world.loadExistingMatch();
    }

    @Override
    public void resize(int width, int height) {
        world.resize(width, height);
    }

    @Override
    public void show() {
        world.initialize();
    }

    @Override
    public void hide() {
        world.dispose();
    }

    @Override
    public void pause() {
        world.pause();
    }

    @Override
    public void resume() {
    }

    @Override
    public void render(float delta) {
        world.render(delta);
    }

    @Override
    public void initialize() {
    }
}
