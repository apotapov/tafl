package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.utils.InstructionsGestureListener;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class InstructionScreen extends AbstractScreen<TaflGame> {

    private static final int TOP_SCREEN_Z_ORDER = 3;
    private static final int NEXT_SCREEN_Z_ORDER = 2;

    public Image[] instructions;
    public ImageButton closeButton;
    public int currentInstruction;

    public GestureDetector gestureDetector;

    public InstructionScreen(final TaflGame game, AbstractScreen<TaflGame> returnScreen) {
        super(game, returnScreen, Constants.ScreenConstants.FADE_TIME);
    }

    public void nextPage() {
        if (currentInstruction < instructions.length - 1) {
            createPageTransition(currentInstruction++, currentInstruction);
        } else {
            back();
        }
    }
    public void previousPage() {
        if (currentInstruction > 0) {
            createPageTransition(currentInstruction--, currentInstruction);
        }
    }

    private void createPageTransition(int current, int next) {
        final Image instruction = instructions[current];
        final Image nextInstruciton = instructions[next];

        float newX = -instruction.getWidth();
        if (next < current) {
            newX = instruction.getHeight();
        }

        instruction.clearActions();
        instruction.addAction(Actions.sequence(Actions.moveTo(
                newX, instruction.getY(),
                Constants.ScreenConstants.INSTRUCTION_SLIDE_DURATION),
                new Action() {
            @Override
            public boolean act(float delta) {
                nextInstruciton.setZIndex(TOP_SCREEN_Z_ORDER);
                hide(instruction);
                return true;
            }
        }));
    }

    @Override
    public boolean back() {
        stage.addAction(new Action() {

            @Override
            public boolean act(float delta) {
                if (InstructionScreen.super.back()) {
                    game.preferenceService.setShowHelpOnStart(false);
                }
                return true;
            }
        });

        return false;
    }

    @Override
    public void initialize() {
        initInstructions();
        initControls();

        gestureDetector = new GestureDetector(new InstructionsGestureListener(this));
    }

    private void initControls() {
        TextureRegion textureRegion = new TextureRegion(
                game.graphicsService.getSprite(
                        Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.CLOSE));
        Drawable imageUp = new TextureRegionDrawable(textureRegion);
        closeButton = new ImageButton(imageUp);
        closeButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                back();
            }
        });

        float scalingFactor = Gdx.graphics.getWidth() / (float) Constants.GameConstants.GAME_WIDTH;

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table controls = new Table(skin);
        controls.setFillParent(true);
        controls.add(closeButton).expand().bottom().right().pad(game.deviceSettings.menuSpacing).size(closeButton.getWidth() * scalingFactor);
        stage.addActor(controls);
    }

    private void initInstructions() {
        instructions = new Image[] {
                new Image(game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.INSTRUCTIONS_1)),
                new Image(game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.INSTRUCTIONS_2)),
                new Image(game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.INSTRUCTIONS_3)),
                new Image(game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.INSTRUCTIONS_4)),
        };

        for (Image instruction : instructions) {
            resetSizeAndPosition(instruction);
            stage.addActor(instruction);
        }
    }

    private void resetSizeAndPosition(Image instruction) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float ratioDifference = (instruction.getHeight() / instruction.getWidth()) / ((height) / width);

        float imageWidth = width;
        float imageHeight = height * ratioDifference;
        float x = 0;
        float y = (height - imageHeight) / 2;
        instruction.setWidth(imageWidth);
        instruction.setHeight(imageHeight);
        instruction.setPosition(x, y);
    }

    @Override
    public void show() {
        super.show();
        currentInstruction = 0;
        if (instructions.length > 1) {
            queueUpNextPage(1);
        }
        instructions[currentInstruction].setX(0);
        instructions[currentInstruction].addAction(Actions.show());

        closeButton.toFront();

        Gdx.input.setInputProcessor(gestureDetector);
    }

    private void queueUpNextPage(int nextPage) {
        queueUpNextPage(instructions[nextPage]);
    }

    private void queueUpNextPage(Image nextInstruction) {

        Image current = instructions[currentInstruction];
        for (Image instruction : instructions) {
            if (instruction != current && instruction != nextInstruction) {
                hide(instruction);
            }
        }

        current.setZIndex(TOP_SCREEN_Z_ORDER);
        nextInstruction.setZIndex(NEXT_SCREEN_Z_ORDER);
        nextInstruction.setX(0);
        nextInstruction.clearActions();
        nextInstruction.addAction(Actions.show());
    }

    private void hide(Image instruction) {
        instruction.toBack();
        instruction.addAction(Actions.hide());
    }

    public void pan(float deltaX) {
        Image instruction = instructions[currentInstruction];
        Image nextInstruciton = null;
        float currentX = instruction.getX();
        if (deltaX > 0 && (currentInstruction > 0 || currentX + deltaX < 0)) {
            if (currentX >= 0) {
                nextInstruciton = instructions[currentInstruction - 1];
            } else if (currentInstruction < instructions.length - 1) {
                nextInstruciton = instructions[currentInstruction + 1];
            }
        } else if (deltaX < 0 && (currentInstruction < instructions.length - 1 || currentX + deltaX > 0)){
            if (currentX <= 0) {
                nextInstruciton = instructions[currentInstruction + 1];
            } else if (currentInstruction > 0) {
                nextInstruciton = instructions[currentInstruction - 1];
            }
        }

        if (nextInstruciton != null) {
            queueUpNextPage(nextInstruciton);
            instruction.setX(instruction.getX() + deltaX);
        }
    }

    public void panStop() {
        Image instruction = instructions[currentInstruction];
        instruction.addAction(Actions.moveTo(
                0, instruction.getY(),
                Constants.ScreenConstants.INSTRUCTION_SLIDE_BACK_DURATION));
    }
}
