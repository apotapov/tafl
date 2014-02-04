package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.roundtriangles.games.zaria.screen.AbstractScreen;

public class OptionsScreen extends AbstractScreen<TaflGame> {

    public OptionsScreen(final TaflGame game) {
        super(game);
    }

    @Override
    public void initialize() {

        stage.addActor(new Image(game.graphicsService.getTexture(Assets.Graphics.BACKGROUND_IMAGE)));

        Skin skin = game.graphicsService.getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        Label label = new Label(game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_TITLE), skin, Assets.Skin.IN_GAME_STYLE_NAME);
        table.add(label).spaceBottom(20);
        table.row();

        String text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_SOUND_EFFECTS);
        final CheckBox soundEffectsCheckbox = new CheckBox(text, skin, Assets.Skin.IN_GAME_STYLE_NAME);
        soundEffectsCheckbox.setChecked(game.preferenceService.isSoundEnabled());
        soundEffectsCheckbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setSoundEnabled(soundEffectsCheckbox.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(soundEffectsCheckbox).align(BaseTableLayout.LEFT).uniform().spaceBottom(10);
        table.row();

        text = game.localeService.get(LocalizedStrings.OptionsMenu.OPTIONS_MUSIC);
        final CheckBox musicCheckbox = new CheckBox(text, skin, Assets.Skin.IN_GAME_STYLE_NAME);
        musicCheckbox.setChecked(game.preferenceService.isMusicEnabled());
        musicCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.preferenceService.setMusicEnabled(musicCheckbox.isChecked());
                game.soundService.playSound(Assets.Sounds.CLICK_SOUND);
            }
        });
        table.add(musicCheckbox).align(BaseTableLayout.LEFT).uniform().spaceBottom(20);
        table.row();

        Button mainMenuButton = game.getMainMenuButton();
        table.add(mainMenuButton).size(Constants.ScreenConstants.BUTTON_WIDTH, Constants.ScreenConstants.BUTTON_HEIGHT);
        table.row();

        stage.addActor(table);
    }
}
