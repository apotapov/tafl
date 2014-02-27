package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

    public Image[] instructions;
    public int currentInstruction;

    Table controls;

    public GestureDetector gestureDetector;

    public InstructionScreen(final TaflGame game, AbstractScreen<TaflGame> returnScreen) {
        super(game, game.mainMenuScreen, Constants.ScreenConstants.FADE_TIME);
    }

    public void nextPage() {
        currentInstruction++;
        if (currentInstruction < instructions.length) {
            setBackgroundImage(instructions[currentInstruction]);
        } else {
            back();
        }
    }

    public void previousPage() {
        currentInstruction--;
        if (currentInstruction > -1) {
            setBackgroundImage(instructions[currentInstruction]);
        } else {
            currentInstruction = 0;
        }
    }

    @Override
    public void initialize() {
        instructions = new Image[] {
                new Image(game.graphicsService.getSprite(game.deviceSettings.backgroundAtlas, Assets.Graphics.INSTRUCTIONS_1)),
                new Image(game.graphicsService.getSprite(game.deviceSettings.backgroundAtlas, Assets.Graphics.INSTRUCTIONS_2)),
                new Image(game.graphicsService.getSprite(game.deviceSettings.backgroundAtlas, Assets.Graphics.INSTRUCTIONS_3)),
                new Image(game.graphicsService.getSprite(game.deviceSettings.backgroundAtlas, Assets.Graphics.INSTRUCTIONS_4)),
                new Image(game.graphicsService.getSprite(game.deviceSettings.backgroundAtlas, Assets.Graphics.INSTRUCTIONS_5)),
                new Image(game.graphicsService.getSprite(game.deviceSettings.backgroundAtlas, Assets.Graphics.INSTRUCTIONS_6))
        };

        gestureDetector = new GestureDetector(new InstructionsGestureListener(this));

        TextureRegion textureRegion = new TextureRegion(
                game.graphicsService.getSprite(
                        Assets.Graphics.ATLAS_PIECES, Assets.Graphics.CLOSE_ICON));
        Drawable imageUp = new TextureRegionDrawable(textureRegion);
        ImageButton closeButton = new ImageButton(imageUp);
        closeButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                back();
            }
        });

        float scalingFactor = Gdx.graphics.getWidth() / (float) Constants.GameConstants.GAME_WIDTH;

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        controls = new Table(skin);
        controls.setFillParent(true);
        controls.add(closeButton).expand().bottom().right().pad(game.deviceSettings.menuSpacing).size(closeButton.getWidth() * scalingFactor);
        stage.addActor(controls);
    }

    @Override
    public void show() {
        setBackgroundImage(instructions[0]);
        currentInstruction = 0;
        super.show();
        Gdx.input.setInputProcessor(gestureDetector);
        game.preferenceService.setShowHelpOnStart(false);
    }

    @Override
    public void setBackgroundImage(Image backgroundImage) {
        super.setBackgroundImage(backgroundImage);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (controls != null) {
            controls.toFront();
        }
    }
}
