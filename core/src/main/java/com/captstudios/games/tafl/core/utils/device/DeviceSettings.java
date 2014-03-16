package com.captstudios.games.tafl.core.utils.device;

import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.roundtriangles.games.zaria.services.utils.FontDefinition;


public class DeviceSettings {
    public TaflGameConfig config;

    public int width;
    public int height;

    public FontDefinition menuFont;
    public FontDefinition screenTitleFont;
    public FontDefinition gameFont;
    public FontDefinition hudFont;
    public FontDefinition rulesFont;
    public FontDefinition rulesTitleFont;

    public int menuLabelHeight;
    public int menuSelectorHeight;
    public int menuButtonHeight;
    public int menuSpacing;

    public int dialogButtonWidth;
    public int dialogButtonHeight;
    public int dialogSpacing;

    public int hudButtonWidth;
    public int hudButtonHeight;
    public int hudTablePadding;
    public int hudPlayerLablePad;

    public DeviceSettings(TaflGameConfig config) {
        this.config = config;
    }

    public void initialize(int width, int height) {
        this.width = width;
        this.height = height;

        if (config.deviceType == DeviceType.DESKTOP) {
            setParameters(Assets.Fonts.FONT_DESTKOP_MENU,
                    Assets.Fonts.FONT_DESKTOP_SCREEN_TITLE,
                    Assets.Fonts.FONT_DESKTOP_GAME,
                    Assets.Fonts.FONT_DESKTOP_HUD,
                    Assets.Fonts.FONT_DESKTOP_RULES,
                    Assets.Fonts.FONT_DESKTOP_RULES_TITLE,
                    Constants.ScreenConstants.DESKTOP_LABEL_HEIGHT,
                    Constants.ScreenConstants.DESKTOP_SELECTOR_HEIGHT,
                    Constants.ScreenConstants.DESKTOP_BUTTON_HEIGHT,
                    Constants.ScreenConstants.DESKTOP_SPACING,
                    Constants.ScreenConstants.DESKTOP_DIALOG_BUTTON_WIDTH,
                    Constants.ScreenConstants.DESKTOP_DIALOG_BUTTON_HEIGHT,
                    Constants.ScreenConstants.DESKTOP_DIALOG_BUTTON_SPACING,
                    Constants.ScreenConstants.DESKTOP_HUD_BUTTON_WIDTH,
                    Constants.ScreenConstants.DESKTOP_HUD_BUTTON_HEIGHT,
                    Constants.ScreenConstants.DESKTOP_HUD_TABLE_PADDING,
                    Constants.ScreenConstants.DESKTOP_PLAYER_LABEL_PAD_TOP);
        }

        if (width > Constants.ScreenConstants.XHDPI_MIN_WIDTH) {
            setParameters(Assets.Fonts.FONT_XHDPI_MENU,
                    Assets.Fonts.FONT_XHDPI_SCREEN_TITLE,
                    Assets.Fonts.FONT_XHDPI_GAME,
                    Assets.Fonts.FONT_XHDPI_HUD,
                    Assets.Fonts.FONT_XHDPI_RULES,
                    Assets.Fonts.FONT_XHDPI_RULES_TITLE,
                    Constants.ScreenConstants.XHDPI_LABEL_HEIGHT,
                    Constants.ScreenConstants.XHDPI_SELECTOR_HEIGHT,
                    Constants.ScreenConstants.XHDPI_BUTTON_HEIGHT,
                    Constants.ScreenConstants.XHDPI_SPACING,
                    Constants.ScreenConstants.XHDPI_DIALOG_BUTTON_WIDTH,
                    Constants.ScreenConstants.XHDPI_DIALOG_BUTTON_HEIGHT,
                    Constants.ScreenConstants.XHDPI_DIALOG_BUTTON_SPACING,
                    Constants.ScreenConstants.XHDPI_HUD_BUTTON_WIDTH,
                    Constants.ScreenConstants.XHDPI_HUD_BUTTON_HEIGHT,
                    Constants.ScreenConstants.XHDPI_HUD_TABLE_PADDING,
                    Constants.ScreenConstants.XHDPI_PLAYER_LABEL_PAD_TOP);
        } else if (width > Constants.ScreenConstants.HDPI_MIN_WIDTH) {
            setParameters(Assets.Fonts.FONT_HDPI_MENU,
                    Assets.Fonts.FONT_HDPI_SCREEN_TITLE,
                    Assets.Fonts.FONT_HDPI_GAME,
                    Assets.Fonts.FONT_HDPI_HUD,
                    Assets.Fonts.FONT_HDPI_RULES,
                    Assets.Fonts.FONT_HDPI_RULES_TITLE,
                    Constants.ScreenConstants.HDPI_LABEL_HEIGHT,
                    Constants.ScreenConstants.HDPI_SELECTOR_HEIGHT,
                    Constants.ScreenConstants.HDPI_BUTTON_HEIGHT,
                    Constants.ScreenConstants.HDPI_SPACING,
                    Constants.ScreenConstants.HDPI_DIALOG_BUTTON_WIDTH,
                    Constants.ScreenConstants.HDPI_DIALOG_BUTTON_HEIGHT,
                    Constants.ScreenConstants.HDPI_DIALOG_BUTTON_SPACING,
                    Constants.ScreenConstants.HDPI_HUD_BUTTON_WIDTH,
                    Constants.ScreenConstants.HDPI_HUD_BUTTON_HEIGHT,
                    Constants.ScreenConstants.HDPI_HUD_TABLE_PADDING,
                    Constants.ScreenConstants.HDPI_PLAYER_LABEL_PAD_TOP);
        } else if (width > Constants.ScreenConstants.MDPI_MIN_WIDTH) {
            setParameters(Assets.Fonts.FONT_MDPI_MENU,
                    Assets.Fonts.FONT_MDPI_SCREEN_TITLE,
                    Assets.Fonts.FONT_MDPI_GAME,
                    Assets.Fonts.FONT_MDPI_HUD,
                    Assets.Fonts.FONT_MDPI_RULES,
                    Assets.Fonts.FONT_MDPI_RULES_TITLE,
                    Constants.ScreenConstants.MDPI_LABEL_HEIGHT,
                    Constants.ScreenConstants.MDPI_SELECTOR_HEIGHT,
                    Constants.ScreenConstants.MDPI_BUTTON_HEIGHT,
                    Constants.ScreenConstants.MDPI_SPACING,
                    Constants.ScreenConstants.MDPI_DIALOG_BUTTON_WIDTH,
                    Constants.ScreenConstants.MDPI_DIALOG_BUTTON_HEIGHT,
                    Constants.ScreenConstants.MDPI_DIALOG_BUTTON_SPACING,
                    Constants.ScreenConstants.MDPI_HUD_BUTTON_WIDTH,
                    Constants.ScreenConstants.MDPI_HUD_BUTTON_HEIGHT,
                    Constants.ScreenConstants.MDPI_HUD_TABLE_PADDING,
                    Constants.ScreenConstants.MDPI_PLAYER_LABEL_PAD_TOP);
        } else {
            setParameters(Assets.Fonts.FONT_LDPI_MENU,
                    Assets.Fonts.FONT_LDPI_SCREEN_TITLE,
                    Assets.Fonts.FONT_LDPI_GAME,
                    Assets.Fonts.FONT_LDPI_HUD,
                    Assets.Fonts.FONT_LDPI_RULES,
                    Assets.Fonts.FONT_LDPI_RULES_TITLE,
                    Constants.ScreenConstants.LDPI_LABEL_HEIGHT,
                    Constants.ScreenConstants.LDPI_SELECTOR_HEIGHT,
                    Constants.ScreenConstants.LDPI_BUTTON_HEIGHT,
                    Constants.ScreenConstants.LDPI_SPACING,
                    Constants.ScreenConstants.LDPI_DIALOG_BUTTON_WIDTH,
                    Constants.ScreenConstants.LDPI_DIALOG_BUTTON_HEIGHT,
                    Constants.ScreenConstants.LDPI_DIALOG_BUTTON_SPACING,
                    Constants.ScreenConstants.LDPI_HUD_BUTTON_WIDTH,
                    Constants.ScreenConstants.LDPI_HUD_BUTTON_HEIGHT,
                    Constants.ScreenConstants.LDPI_HUD_TABLE_PADDING,
                    Constants.ScreenConstants.LDPI_PLAYER_LABEL_PAD_TOP);
        }
    }

    private void setParameters(FontDefinition menuFont,
            FontDefinition screenTitleFont,
            FontDefinition gameFont,
            FontDefinition hudFont,
            FontDefinition rulesFont,
            FontDefinition rulesTitleFont,
            int menuLabelHeight,
            int menuSelectorHeight,
            int menuButtonHeight,
            int menuSpacing,
            int dialogButtonWidth,
            int dialogButtonHeight,
            int dialogSpacing,
            int hudButtonWidth,
            int hudButtonHeight,
            int hudTablePadding,
            int hudPlayerLablePad) {

        this.menuFont = menuFont;
        this.screenTitleFont = screenTitleFont;
        this.gameFont = gameFont;
        this.hudFont = hudFont;
        this.rulesFont = rulesFont;
        this.rulesTitleFont = rulesTitleFont;

        this.menuLabelHeight = menuLabelHeight;
        this.menuSelectorHeight = menuSelectorHeight;
        this.menuButtonHeight = menuButtonHeight;
        this.menuSpacing = menuSpacing;

        this.dialogButtonWidth = dialogButtonWidth;
        this.dialogButtonHeight = dialogButtonHeight;
        this.dialogSpacing = dialogSpacing;

        this.hudButtonWidth = hudButtonWidth;
        this.hudButtonHeight = hudButtonHeight;
        this.hudTablePadding = hudTablePadding;
        this.hudPlayerLablePad = hudPlayerLablePad;
    }
}
