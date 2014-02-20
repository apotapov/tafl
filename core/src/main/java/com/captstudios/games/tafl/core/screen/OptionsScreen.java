package com.captstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class OptionsScreen extends AbstractScreen<TaflGame> {

    public OptionsScreen(final TaflGame game) {
        super(game);
    }

    @Override
    public void initialize() {

        Image background = new Image(game.graphicsService.getSprite(
                Assets.Graphics.MENU_ATLAS, Assets.Graphics.MENU));
        background.setFillParent(true);
        stage.addActor(background);

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        Label label = new Label(game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_TITLE), skin, game.deviceType.menuStyle);
        table.add(label).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        String text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_SOUND_EFFECTS);
        final CheckBox soundEffectsCheckbox = new CheckBox(text, skin, game.deviceType.menuStyle);
        soundEffectsCheckbox.setChecked(game.preferenceService.isSoundEnabled());
        soundEffectsCheckbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setSoundEnabled(soundEffectsCheckbox.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(soundEffectsCheckbox).align(BaseTableLayout.LEFT).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_MUSIC);
        final CheckBox musicCheckbox = new CheckBox(text, skin, game.deviceType.menuStyle);
        musicCheckbox.setChecked(game.preferenceService.isMusicEnabled());
        musicCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setMusicEnabled(musicCheckbox.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(musicCheckbox).align(BaseTableLayout.LEFT).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_VIBRATE);
        final CheckBox vibrationCheckbox = new CheckBox(text, skin, game.deviceType.menuStyle);
        vibrationCheckbox.setChecked(game.preferenceService.isVibrateEnabled());
        vibrationCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setVibrateEnabled(vibrationCheckbox.isChecked());
                game.soundService.vibrate(Constants.GameConstants.VIBRATION_LENGTH);
            }
        });
        table.add(vibrationCheckbox).align(BaseTableLayout.LEFT).spaceBottom(game.deviceType.menuSpacing);
        table.row();

        Button mainMenuButton = game.getMainMenuButton();
        table.add(mainMenuButton).size(game.deviceType.menuButtonWidth, game.deviceType.menuButtonHeight);
        table.row();

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        stage.addActor(table);
    }
}
