package com.captstudios.games.tafl.core.es.components.singleton;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class HudRenderingComponent implements RenderingComponent {

    public Stage hubStage;

    public Dialog menu;

    public Dialog winDialog;
    public Label winText;

    public Dialog lossDialog;

    public Dialog drawDialog;
    public Label drawText;

    public Dialog surrenderDialog;

    // debug
    public Label fps;

    @Override
    public void reset() {
        menu = null;
        winDialog = null;
        winText = null;
        lossDialog = null;
        drawDialog = null;
        drawText = null;
        surrenderDialog = null;
    }

    @Override
    public Camera getCamera() {
        return hubStage.getCamera();
    }
}
