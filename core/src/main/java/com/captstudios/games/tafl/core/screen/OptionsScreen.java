package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class OptionsScreen extends AbstractScreen<TaflGame> {

    public OptionsScreen(final TaflGame game) {
        super(game, game.mainMenuScreen, Constants.ScreenConstants.FADE_TIME);
    }

    @Override
    public void initialize() {
        Sprite background = game.graphicsService.getSprite(
                Assets.Graphics.ATLAS_BACKGROUNDS, Assets.Graphics.MENU);
        setBackgroundImage(new Image(background));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        String text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_TITLE);
        Label label = new Label(text, skin, Assets.Skin.SKIN_STYLE_SCREEN_TITLE);
        table.add(label).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_SOUND_EFFECTS);
        final CheckBox soundEffectsCheckbox = new CheckBox(text, skin, Assets.Skin.SKIN_STYLE_MENU);
        soundEffectsCheckbox.setChecked(game.preferenceService.isSoundEnabled());
        soundEffectsCheckbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setSoundEnabled(soundEffectsCheckbox.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(soundEffectsCheckbox).align(BaseTableLayout.LEFT).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_MUSIC);
        final CheckBox musicCheckbox = new CheckBox(text, skin, Assets.Skin.SKIN_STYLE_MENU);
        musicCheckbox.setChecked(game.preferenceService.isMusicEnabled());
        musicCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setMusicEnabled(musicCheckbox.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(musicCheckbox).align(BaseTableLayout.LEFT).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        if (game.config.vibrationSupported) {
            text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_VIBRATE);
            final CheckBox vibrationCheckbox = new CheckBox(text, skin, Assets.Skin.SKIN_STYLE_MENU);
            vibrationCheckbox.setChecked(game.preferenceService.isVibrateEnabled());
            vibrationCheckbox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.preferenceService.setVibrateEnabled(vibrationCheckbox.isChecked());
                    game.soundService.vibrate(Constants.GameConstants.CAPTURE_VIBRATION_LENGTH);
                }
            });
            table.add(vibrationCheckbox).align(BaseTableLayout.LEFT).spaceBottom(game.deviceSettings.menuSpacing);
            table.row();
        }

        Button mainMenuButton = game.getMainMenuButton();
        table.add(mainMenuButton).size(game.deviceSettings.menuButtonWidth,
                game.deviceSettings.menuButtonHeight).spaceBottom(game.deviceSettings.menuSpacing);
        table.row();

        text = game.localeService.get(LocalizedStrings.AboutInfo.ABOUT);
        Button about = game.createSwitchScreenButton(text, game.aboutScreen);
        table.add(about).size(game.deviceSettings.menuButtonWidth, game.deviceSettings.menuButtonHeight);
        table.row();

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }
}
