package com.pactstudios.games.tafl.core.es.components.singleton;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;


public class HudRenderingComponent implements RenderingComponent {

    public Stage hubStage;

    public Dialog menu;

    public Dialog winDialog;
    public Label winText;

    public Dialog lossDialog;

    public Dialog drawDialog;
    public Label drawText;

    public Dialog playerWarningDialog;
    public Label playerWarningText;

    public Dialog surrenderDialog;


    // upper hud
    public Label timer;

    // debug
    public Label fps;

    public List log;

    @Override
    public void reset() {
        menu = null;
        winDialog = null;
        winText = null;
        lossDialog = null;
        drawDialog = null;
        drawText = null;
        playerWarningDialog = null;
        playerWarningText = null;
        surrenderDialog = null;
        timer = null;
    }

    @Override
    public Camera getCamera() {
        return hubStage.getCamera();
    }
}
