package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.utils.InstructionsGestureListener;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class InstructionScreen extends AbstractScreen<TaflGame> {

    public AbstractScreen<TaflGame> returnScreen;

    public Image[] instructions;
    public int currentInstruction;

    public GestureDetector gestureDetector;

    public InstructionScreen(final TaflGame game, AbstractScreen<TaflGame> returnScreen) {
        super(game);
        this.returnScreen = returnScreen;
    }

    @Override
    public void initialize() {
        instructions = new Image[] {
                new Image(game.graphicsService.getSprite(Assets.Graphics.SPLASH_ATLAS, Assets.Graphics.SPLASH)),
                new Image(game.graphicsService.getSprite(Assets.Graphics.MENU_ATLAS, Assets.Graphics.MENU)),
        };

        for (Image instruction : instructions) {
            instruction.setFillParent(true);
        }

        gestureDetector = new GestureDetector(new InstructionsGestureListener(this));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gestureDetector);
        stage.addActor(instructions[0]);
        currentInstruction = 0;
    }
}
