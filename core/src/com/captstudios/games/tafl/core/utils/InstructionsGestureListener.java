package com.captstudios.games.tafl.core.utils;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.captstudios.games.tafl.core.screen.InstructionScreen;

public class InstructionsGestureListener implements GestureListener {

    InstructionScreen screen;

    public InstructionsGestureListener(InstructionScreen instructionScreen) {
        this.screen = instructionScreen;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (velocityX > 0) {
            screen.previousPage();
        } else {
            screen.nextPage();
        }
        return true;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return screen.stage.touchDown((int)x, (int)y, count, button) &&
                screen.stage.touchUp((int)x, (int)y, count, button);
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        screen.pan(deltaX);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        screen.panStop();
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
