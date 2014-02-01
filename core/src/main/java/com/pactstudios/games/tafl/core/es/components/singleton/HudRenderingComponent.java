package com.pactstudios.games.tafl.core.es.components.singleton;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;


public class HudRenderingComponent implements RenderingComponent {

    public Stage hubStage;

    public Dialog menu;
    public Dialog whiteWinDialog;
    public Dialog blackWinDialog;
    public Dialog lossDialog;
    public Dialog drawDialog;

    public Label drawDialogText;

    // upper hud
    public Label turn;
    public Label timer;

    // debug
    public Label fps;

    public List log;

    @Override
    public void reset() {
        menu = null;
        whiteWinDialog = null;
        blackWinDialog = null;
        lossDialog = null;
        drawDialog = null;
        turn = null;
        timer = null;
    }

    @Override
    public Camera getCamera() {
        return hubStage.getCamera();
    }
}
