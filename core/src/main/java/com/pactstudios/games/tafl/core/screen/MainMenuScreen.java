package com.pactstudios.games.tafl.core.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pactstudios.games.tafl.core.TaflGame;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.roundtriangles.games.zaria.AbstractScreen;
import com.roundtriangles.games.zaria.services.LocaleService;
import com.roundtriangles.games.zaria.services.SoundService;

public class MainMenuScreen extends AbstractScreen<TaflGame> {

    public MainMenuScreen(final TaflGame game) {
        super(game);

        Skin skin = game.getGraphicsService().getSkin(Assets.Skin.UI_SKIN);
        Table table = new Table(skin);
        table.setFillParent(true);

        LocaleService localeService = game.getLocaleService();
        Label label = new Label(localeService._(LocalizedStrings.MainMenu.GAME_TITLE), skin, Assets.Skin.MENU_STYLE_NAME);
        table.add(label).spaceBottom(20);
        table.row();

        String buttonText = localeService._(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_START);
        TextButton button = createButton(buttonText,
                skin, game.getLevelSelectionScreen());
        table.add(button).size(Constants.Screens.BUTTON_WIDTH, Constants.Screens.BUTTON_HEIGHT).uniform().spaceBottom(10);
        table.row();

        buttonText = localeService._(LocalizedStrings.MainMenu.MAIN_MENU_BUTTON_OPTIONS);
        button = createButton(buttonText,
                skin, game.getOptionsScreen());
        table.add(button);
        table.row();

        stage.addActor(table);
    }

    @Override
    public void show() {
        super.show();
        game.getSoundService().playMusic(Assets.Sounds.MENU_MUSIC);
    }

    private TextButton createButton(String text, Skin skin, final Screen screen) {
        final SoundService soundService = game.getSoundService();

        TextButton button = new TextButton(text, skin, Assets.Skin.MENU_STYLE_NAME);
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundService.playSound(Assets.Sounds.CLICK_SOUND);
                game.setScreen(screen);
            }
        });
        return button;
    }
}
