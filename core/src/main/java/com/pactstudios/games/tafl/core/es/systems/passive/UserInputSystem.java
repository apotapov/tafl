package com.pactstudios.games.tafl.core.es.systems.passive;

import com.artemis.systems.PassiveEntitySystem;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.pactstudios.games.tafl.core.enums.InputType;
import com.pactstudios.games.tafl.core.es.systems.events.InputEvent;

public class UserInputSystem extends PassiveEntitySystem implements InputProcessor {


    @Override
    public void initialize() {
        super.initialize();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return addTouchEvent(InputType.TOUCH_DOWN, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return addTouchEvent(InputType.TOUCH_UP, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return addTouchEvent(InputType.TOUCH_DRAG, screenX, screenY, pointer, 0);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    protected boolean addTouchEvent(InputType type, int x, int y, int pointer, int button) {
        InputEvent event = SystemEvent.createEvent(InputEvent.class);
        event.type = type;
        event.x = x;
        event.y = y;
        event.pointer = pointer;
        event.button = button;
        world.postEvent(this, event);
        return true;
    }
}
