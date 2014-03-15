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

        createMusicCheckBox(table);
        createDifficultySelector(table);

        if (game.deviceSettings.config.vibrationSupported) {
            //createVibrationCheckbox(skin, table);
        }

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }

        stage.addActor(table);

        createButtons();
    }

    //    private void createVibrationCheckbox(Skin skin, Table table) {
    //        String text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_VIBRATE);
    //        final CheckBox vibrationCheckbox = new CheckBox(text, skin, Assets.Skin.SKIN_STYLE_MENU);
    //        vibrationCheckbox.setChecked(game.preferenceService.isVibrateEnabled());
    //        vibrationCheckbox.addListener(new ChangeListener() {
    //            @Override
    //            public void changed(ChangeEvent event, Actor actor) {
    //                game.preferenceService.setVibrateEnabled(vibrationCheckbox.isChecked());
    //                game.soundService.vibrate(Constants.GameConstants.CAPTURE_VIBRATION_LENGTH);
    //            }
    //        });
    //        table.add(vibrationCheckbox).align(BaseTableLayout.LEFT).spaceBottom(game.deviceSettings.menuSpacing);
    //        table.row();
    //    }

    private void createButtons() {
        Table buttonTable = new Table();
        buttonTable.right().bottom().setFillParent(true);
        Sprite icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.BACK);
        Button button = new ImageButton(new TextureRegionDrawable(new TextureRegion(icon)));

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
                back();
            }
        });
        buttonTable.add(button).pad(Constants.HudConstants.PLAYER_LABEL_PAD_TOP / 4).expandX().left();

        buttonTable.right().bottom().setFillParent(true);
        icon = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.ABOUT);
        button = game.createSwitchScreenButton(icon, this, game.aboutScreen);
        buttonTable.add(button).pad(Constants.HudConstants.PLAYER_LABEL_PAD_TOP / 4);

        if (Constants.GameConstants.DEBUG) {
            buttonTable.debug();
        }

        stage.addActor(buttonTable);
    }

    private void createMusicCheckBox(Table table) {
        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.MUSIC);
        Image imageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));
        table.add(imageLabel).spaceBottom(game.deviceSettings.menuSpacing);

        Sprite on = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.ON);
        Sprite off = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.OFF);
        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.ON_OFF_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.ON_OFF_PRESSED);

        final ImageButton musicCheckbox = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(off)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(on)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(on)));

        musicCheckbox.setChecked(game.preferenceService.isMusicEnabled());

        musicCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setMusicEnabled(musicCheckbox.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(musicCheckbox).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();
    }

    private void createDifficultySelector(Table table) {
        Sprite labelSprite = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.DIFFICULTY);
        Image imageLabel = new Image(new TextureRegionDrawable(new TextureRegion(labelSprite)));
        table.add(imageLabel).spaceBottom(game.deviceSettings.menuSpacing);

        final Sprite[] text = new Sprite[] {
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.NOVICE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.BEGINNER),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.INTERMEDIATE),
                game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.TextGraphics.ADVANCED)
        };

        Sprite up = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAYERS_BLANK);
        Sprite down = game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.PLAYERS_PRESSED);

        final AiType initialType = game.preferenceService.getAiType();
        Sprite selectorTest = text[initialType.ordinal()];

        final ImageButton selector = new ImageButton(
                new DoubleTextureDrawable(new TextureRegion(up), new TextureRegion(selectorTest)),
                new DoubleTextureDrawable(new TextureRegion(down), new TextureRegion(selectorTest)));


        selector.addListener(new ChangeListener() {

            int selected = initialType.ordinal();

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected = (selected + 1) % text.length;
                ((DoubleTextureDrawable)selector.getStyle().imageDown).setInnerRegion(text[selected]);
                ((DoubleTextureDrawable)selector.getStyle().imageUp).setInnerRegion(text[selected]);
                game.preferenceService.setAiType(AiType.values()[selected]);
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(selector).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();
    }

    @Override
    public boolean back() {
        boolean result = super.back();
        this.parentScreen = game.mainMenuScreen;
        return result;
    }
}
