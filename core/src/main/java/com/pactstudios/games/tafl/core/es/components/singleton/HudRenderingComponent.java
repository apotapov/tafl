package com.pactstudios.games.tafl.core.es.components.singleton;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class HudRenderingComponent implements RenderingComponent {

    public Stage hubStage;

    public Dialog menu;
    public Dialog winDialog;
    public Dialog lossDialog;

    // upper hud
    public Label score;
    public Label gameTime;

    // debug
    public Label mouseLocation;
    public Label fps;

    @Override
    public void reset() {
        menu = null;
        winDialog = null;
        lossDialog = null;
        score = null;
        gameTime = null;
    }

    @Override
    public Camera getCamera() {
        return hubStage.getCamera();
    }
}
