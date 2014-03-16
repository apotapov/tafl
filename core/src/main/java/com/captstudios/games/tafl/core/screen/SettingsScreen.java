package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.AiType;
import com.captstudios.games.tafl.core.utils.DoubleTextureDrawable;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class SettingsScreen extends AbstractScreen<TaflGame> {

    ImageButton musicSelector;

    ImageButton difficultySelector;
    Sprite[] difficultyValues;

    public SettingsScreen(final TaflGame game) {
        super(game, game.mainMenuScreen, Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void initialize() {
        Sprite background = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_BACKGROUNDS, Assets.Background.MENU);
        setBackgroundImage(new Image(background));

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().space(game.deviceSettings.menuSpacing);

        createMusicSelector(table);
        createDifficultySelector(table);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }

        stage.addActor(table);

        createButtons();
    }

    private void createButtons() {
        Table buttonTable = new Table();
        buttonTable.right().bottom().setFillParent(true);
        buttonTable.defaults().pad(game.deviceSettings.menuSpacing).size(
                game.deviceSettings.menuButtonHeight * 1.5f, game.deviceSettings.menuButtonHeight);
        Sprite icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.BACK);
        Button button = new ImageButton(new TextureRegionDrawable(new TextureRegion(icon)));

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                back();
            }
        });
        buttonTable.add(button).expandX().left();

        buttonTable.right().bottom().setFillParent(true);
        icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.ABOUT);
        button = game.createSwitchScreenButton(icon, this, game.aboutScreen);
        buttonTable.add(button);

        if (Constants.GameConstants.DEBUG) {
            buttonTable.debug();
        }

        stage.addActor(buttonTable);
    }

    private void createMusicSelector(Table table) {
        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.MUSIC);
        Image imageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));

        float height = game.deviceSettings.menuLabelHeight;
        float width = height * (imageLabel.getWidth() / imageLabel.getHeight());

        table.add(imageLabel).spaceBottom(game.deviceSettings.menuSpacing).size(width, height).expandX().right();

        Sprite on = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.ON);
        Sprite off = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.OFF);
        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.ON_OFF_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.ON_OFF_PRESSED);

        musicSelector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(off)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(on)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(on)));

        musicSelector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setMusicEnabled(musicSelector.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });

        height = game.deviceSettings.menuButtonHeight;
        width = height * (up.getWidth() / up.getHeight());

        table.add(musicSelector).size(width, height).left();
        table.row();
    }

    private void createDifficultySelector(Table table) {
        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.DIFFICULTY);
        Image difficultyImageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));

        float height = game.deviceSettings.menuLabelHeight * 1.5f;
        float width = height * (difficultyImageLabel.getWidth() / difficultyImageLabel.getHeight());

        table.add(difficultyImageLabel).spaceBottom(game.deviceSettings.menuSpacing).size(width, height).expandX().right();

        difficultyValues = new Sprite[] {
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.NOVICE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.BEGINNER),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.INTERMEDIATE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.ADVANCED)
        };

        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAY_AS_PRESSED);

        final AiType initialType = game.preferenceService.getAiType();
        Sprite selectorTest = difficultyValues[initialType.ordinal()];

        difficultySelector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(selectorTest)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(selectorTest)));

        height = game.deviceSettings.menuButtonHeight;
        width = height * (up.getWidth() / up.getHeight());

        table.add(difficultySelector).size(width, height).expandX().left();
        table.row();
    }

    @Override
    public void show() {
        super.show();

        musicSelector.setChecked(game.preferenceService.isMusicEnabled());
        updateDifficultySelector();
    }

    private void updateDifficultySelector() {
        final AiType initialType = game.preferenceService.getAiType();

        ((DoubleTextureDrawable)difficultySelector.getStyle().imageDown).setInnerRegion(difficultyValues[initialType.ordinal()]);
        ((DoubleTextureDrawable)difficultySelector.getStyle().imageUp).setInnerRegion(difficultyValues[initialType.ordinal()]);

        difficultySelector.addListener(new ChangeListener() {

            int selected = initialType.ordinal();

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected = (selected + 1) % difficultyValues.length;
                ((DoubleTextureDrawable)difficultySelector.getStyle().imageDown).setInnerRegion(difficultyValues[selected]);
                ((DoubleTextureDrawable)difficultySelector.getStyle().imageUp).setInnerRegion(difficultyValues[selected]);
                game.preferenceService.setAiType(AiType.values()[selected]);
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
    }
}
