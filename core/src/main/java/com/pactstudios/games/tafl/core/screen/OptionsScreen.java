package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.roundtriangles.games.zaria.screen.AbstractScreen;
import com.roundtriangles.games.zaria.services.LocaleService;
import com.roundtriangles.games.zaria.services.PreferenceService;
import com.roundtriangles.games.zaria.services.SoundService;

public class OptionsScreen extends AbstractScreen<TaflGame> {

    public OptionsScreen(final TaflGame game) {
        super(game);

        Skin skin = game.getGraphicsService().getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        final PreferenceService preferenceService = game.getPreferenceService();
        final SoundService soundService = game.getSoundService();
        LocaleService localeService = game.getLocaleService();

        Label label = new Label(localeService._(LocalizedStrings.OptionsMenu.OPTIONS_TITLE), skin, Assets.Skin.MENU_STYLE_NAME);
        table.add(label).spaceBottom(20);
        table.row();

        String text = localeService._(LocalizedStrings.OptionsMenu.OPTIONS_SOUND_EFFECTS);
        final CheckBox soundEffectsCheckbox = new CheckBox(text, skin, Assets.Skin.MENU_STYLE_NAME);
        soundEffectsCheckbox.setChecked(preferenceService.isSoundEnabled());
        soundEffectsCheckbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                preferenceService.setSoundEnabled(enabled);
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(soundEffectsCheckbox).uniform().spaceBottom(10);
        table.row();

        text = localeService._(LocalizedStrings.OptionsMenu.OPTIONS_MUSIC);
        final CheckBox musicCheckbox = new CheckBox(text, skin, Assets.Skin.MENU_STYLE_NAME);
        musicCheckbox.setChecked(preferenceService.isMusicEnabled());
        musicCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = musicCheckbox.isChecked();
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                preferenceService.setMusicEnabled(enabled);
            }
        });
        table.add(musicCheckbox).uniform().spaceBottom(20);
        table.row();

        Button mainMenuButton = game.getMainMenuButton();
        table.add(mainMenuButton).uniform().fill();
        table.row();

        stage.addActor(table);
    }
}
