package com.captstudios.games.tafl.core.utils;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.screen.AboutScreen;

public class ChangeScreenGestureListener implements GestureListener {

    AboutScreen screen;

    public ChangeScreenGestureListener(AboutScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        screen.game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
        return screen.back();
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
