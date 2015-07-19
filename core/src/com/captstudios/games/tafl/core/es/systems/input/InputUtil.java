package com.captstudios.games.tafl.core.es.systems.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputUtil {
    private static Vector3 screenTouchPoint = new Vector3();
    private static Vector2 gameTouchPoint = new Vector2();

    public static Vector2 translateTouchPoint(Camera camera, int x, int y) {
        camera.unproject(screenTouchPoint.set(x, y, 0));
        gameTouchPoint.x = screenTouchPoint.x;
        gameTouchPoint.y = screenTouchPoint.y;
        return gameTouchPoint;
    }
}
