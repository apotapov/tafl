package com.captstudios.games.tafl.core.utils.services;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.roundtriangles.games.zaria.services.GraphicsService;

public class TaflGraphicsService extends GraphicsService {

    TaflGame game;

    public TaflGraphicsService(TaflGame game) {
        this.game = game;
    }


    @Override
    public void onFinishLoading() {
        Skin skin = getSkin(Assets.Skin.UI_SKIN);
        BitmapFont menuFont = getFont(game.deviceSettings.menuFont);
        BitmapFont screenTitle = getFont(game.deviceSettings.screenTitleFont);
        BitmapFont hudFont = getFont(game.deviceSettings.hudFont);
        BitmapFont rulesFont = getFont(game.deviceSettings.rulesFont);

        skin.get(Assets.Skin.SKIN_STYLE_MENU, TextButtonStyle.class).font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_GAME, TextButtonStyle.class).font = hudFont;
        skin.get(Assets.Skin.SKIN_STYLE_DIALOG, TextButtonStyle.class).font = hudFont;

        skin.get(Assets.Skin.SKIN_STYLE_MENU, ImageTextButtonStyle.class).font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_GAME, ImageTextButtonStyle.class).font = hudFont;

        skin.get(Assets.Skin.SKIN_STYLE_MENU, SelectBoxStyle.class).font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_MENU, SelectBoxStyle.class).listStyle.font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_GAME, SelectBoxStyle.class).font = hudFont;
        skin.get(Assets.Skin.SKIN_STYLE_GAME, SelectBoxStyle.class).listStyle.font = hudFont;

        skin.get(Assets.Skin.SKIN_STYLE_DIALOG, WindowStyle.class).titleFont = hudFont;

        skin.get(Assets.Skin.SKIN_STYLE_MENU, LabelStyle.class).font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_SCREEN_TITLE, LabelStyle.class).font = screenTitle;

        skin.get(Assets.Skin.SKIN_STYLE_GAME, LabelStyle.class).font = hudFont;
        skin.get(Assets.Skin.SKIN_STYLE_DIALOG, LabelStyle.class).font = hudFont;
        skin.get(Assets.Skin.SKIN_STYLE_PLAYER_TAG, LabelStyle.class).font = hudFont;
        skin.get(Assets.Skin.SKIN_STYLE_RULES, LabelStyle.class).font = rulesFont;

        skin.get(Assets.Skin.SKIN_STYLE_MENU, TextFieldStyle.class).font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_GAME, TextFieldStyle.class).font = hudFont;

        skin.get(Assets.Skin.SKIN_STYLE_MENU, CheckBoxStyle.class).font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_GAME, CheckBoxStyle.class).font = hudFont;

        skin.get(Assets.Skin.SKIN_STYLE_MENU, ListStyle.class).font = menuFont;
        skin.get(Assets.Skin.SKIN_STYLE_GAME, ListStyle.class).font = hudFont;
    }

}
