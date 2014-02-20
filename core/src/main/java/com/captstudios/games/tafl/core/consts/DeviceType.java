package com.captstudios.games.tafl.core.consts;

import com.badlogic.gdx.math.Vector2;
import com.roundtriangles.games.zaria.services.utils.FontDefinition;

public enum DeviceType {
    DESKTOP (Assets.Fonts.FONT_DESTKOP_MENU,
            Assets.Fonts.FONT_DESTKOP_SCREEN_TITLE,
            Assets.Fonts.FONT_DESTKOP_GAME,
            Assets.Fonts.FONT_DESTKOP_DIALOG,
            Assets.Fonts.FONT_DESTKOP_DEBUG,
            Constants.ScreenConstants.DESKTOP_MIN_WIDTH,
            Constants.ScreenConstants.DESKTOP_BUTTON_WIDTH,
            Constants.ScreenConstants.DESKTOP_BUTTON_HEIGHT,
            Constants.ScreenConstants.DESKTOP_LIST_WIDTH,
            Constants.ScreenConstants.DESKTOP_LIST_HEIGHT,
            Constants.ScreenConstants.DESKTOP_SPACING,
            Constants.ScreenConstants.DESKTOP_DIALOG_BUTTON_WIDTH,
            Constants.ScreenConstants.DESKTOP_DIALOG_BUTTON_HEIGHT,
            Constants.ScreenConstants.DESKTOP_DIALOG_BUTTON_SPACING,
            Constants.ScreenConstants.DESKTOP_HUD_BUTTON_WIDTH,
            Constants.ScreenConstants.DESKTOP_HUD_BUTTON_HEIGHT,
            Constants.ScreenConstants.DESKTOP_DRAG_POSITION_OFFSET),

            LDPI (Assets.Fonts.FONT_LDPI_MENU,
                    Assets.Fonts.FONT_LDPI_SCREEN_TITLE,
                    Assets.Fonts.FONT_LDPI_GAME,
                    Assets.Fonts.FONT_LDPI_DIALOG,
                    Assets.Fonts.FONT_LDPI_DEBUG,
                    Constants.ScreenConstants.LDPI_MIN_WIDTH,
                    Constants.ScreenConstants.LDPI_BUTTON_WIDTH,
                    Constants.ScreenConstants.LDPI_BUTTON_HEIGHT,
                    Constants.ScreenConstants.LDPI_LIST_WIDTH,
                    Constants.ScreenConstants.LDPI_LIST_HEIGHT,
                    Constants.ScreenConstants.LDPI_SPACING,
                    Constants.ScreenConstants.LDPI_DIALOG_BUTTON_WIDTH,
                    Constants.ScreenConstants.LDPI_DIALOG_BUTTON_HEIGHT,
                    Constants.ScreenConstants.LDPI_DIALOG_BUTTON_SPACING,
                    Constants.ScreenConstants.LDPI_HUD_BUTTON_WIDTH,
                    Constants.ScreenConstants.LDPI_HUD_BUTTON_HEIGHT,
                    Constants.ScreenConstants.LDPI_DRAG_POSITION_OFFSET),

                    MDPI (Assets.Fonts.FONT_MDPI_MENU,
                            Assets.Fonts.FONT_MDPI_SCREEN_TITLE,
                            Assets.Fonts.FONT_MDPI_GAME,
                            Assets.Fonts.FONT_MDPI_DIALOG,
                            Assets.Fonts.FONT_MDPI_DEBUG,
                            Constants.ScreenConstants.MDPI_MIN_WIDTH,
                            Constants.ScreenConstants.MDPI_BUTTON_WIDTH,
                            Constants.ScreenConstants.MDPI_BUTTON_HEIGHT,
                            Constants.ScreenConstants.MDPI_LIST_WIDTH,
                            Constants.ScreenConstants.MDPI_LIST_HEIGHT,
                            Constants.ScreenConstants.MDPI_SPACING,
                            Constants.ScreenConstants.MDPI_DIALOG_BUTTON_WIDTH,
                            Constants.ScreenConstants.MDPI_DIALOG_BUTTON_HEIGHT,
                            Constants.ScreenConstants.MDPI_DIALOG_BUTTON_SPACING,
                            Constants.ScreenConstants.MDPI_HUD_BUTTON_WIDTH,
                            Constants.ScreenConstants.MDPI_HUD_BUTTON_HEIGHT,
                            Constants.ScreenConstants.MDPI_DRAG_POSITION_OFFSET),

                            HDPI (Assets.Fonts.FONT_HDPI_MENU,
                                    Assets.Fonts.FONT_HDPI_SCREEN_TITLE,
                                    Assets.Fonts.FONT_HDPI_GAME,
                                    Assets.Fonts.FONT_HDPI_DIALOG,
                                    Assets.Fonts.FONT_HDPI_DEBUG,
                                    Constants.ScreenConstants.HDPI_MIN_WIDTH,
                                    Constants.ScreenConstants.HDPI_BUTTON_WIDTH,
                                    Constants.ScreenConstants.HDPI_BUTTON_HEIGHT,
                                    Constants.ScreenConstants.HDPI_LIST_WIDTH,
                                    Constants.ScreenConstants.HDPI_LIST_HEIGHT,
                                    Constants.ScreenConstants.HDPI_SPACING,
                                    Constants.ScreenConstants.HDPI_DIALOG_BUTTON_WIDTH,
                                    Constants.ScreenConstants.HDPI_DIALOG_BUTTON_HEIGHT,
                                    Constants.ScreenConstants.HDPI_DIALOG_BUTTON_SPACING,
                                    Constants.ScreenConstants.HDPI_HUD_BUTTON_WIDTH,
                                    Constants.ScreenConstants.HDPI_HUD_BUTTON_HEIGHT,
                                    Constants.ScreenConstants.HDPI_DRAG_POSITION_OFFSET),


                                    XHDPI (Assets.Fonts.FONT_XHDPI_MENU,
                                            Assets.Fonts.FONT_XHDPI_SCREEN_TITLE,
                                            Assets.Fonts.FONT_XHDPI_GAME,
                                            Assets.Fonts.FONT_XHDPI_DIALOG,
                                            Assets.Fonts.FONT_XHDPI_DEBUG,
                                            Constants.ScreenConstants.XHDPI_MIN_WIDTH,
                                            Constants.ScreenConstants.XHDPI_BUTTON_WIDTH,
                                            Constants.ScreenConstants.XHDPI_BUTTON_HEIGHT,
                                            Constants.ScreenConstants.XHDPI_LIST_WIDTH,
                                            Constants.ScreenConstants.XHDPI_LIST_HEIGHT,
                                            Constants.ScreenConstants.XHDPI_SPACING,
                                            Constants.ScreenConstants.XHDPI_DIALOG_BUTTON_WIDTH,
                                            Constants.ScreenConstants.XHDPI_DIALOG_BUTTON_HEIGHT,
                                            Constants.ScreenConstants.XHDPI_DIALOG_BUTTON_SPACING,
                                            Constants.ScreenConstants.XHDPI_HUD_BUTTON_WIDTH,
                                            Constants.ScreenConstants.XHDPI_HUD_BUTTON_HEIGHT,
                                            Constants.ScreenConstants.XHDPI_DRAG_POSITION_OFFSET);

    public FontDefinition menuFont;
    public FontDefinition screenTitleFont;
    public FontDefinition gameFont;
    public FontDefinition dialogFont;
    public FontDefinition debugFont;

    public int minWidth;

    public int menuButtonWidth;
    public int menuButtonHeight;
    public int menuListWidth;
    public int menuListHeight;
    public int menuSpacing;

    public int dialogButtonWidth;
    public int dialogButtonHeight;
    public int dialogSpacing;

    public int hudButtonWidth;
    public int hudButtonHeight;

    public Vector2 dragOffset;

    private DeviceType(FontDefinition menuFont,
            FontDefinition screenTitleFont,
            FontDefinition gameFont,
            FontDefinition dialogFont,
            FontDefinition debugFont,
            int minWidth,
            int menuButtonWidth,
            int menuButtonHeight,
            int menuListWidth,
            int menuListHeight,
            int menuSpacing,
            int dialogButtonWidth,
            int dialogButtonHeight,
            int dialogSpacing,
            int hudButtonWidth,
            int hudButtonHeight,
            Vector2 dragOffset) {

        this.menuFont = menuFont;
        this.screenTitleFont = screenTitleFont;
        this.gameFont = gameFont;
        this.dialogFont = dialogFont;
        this.debugFont = debugFont;

        this.minWidth = minWidth;

        this.menuButtonWidth = menuButtonWidth;
        this.menuButtonHeight = menuButtonHeight;
        this.menuListWidth = menuListWidth;
        this.menuListHeight = menuListHeight;
        this.menuSpacing = menuSpacing;

        this.dialogButtonWidth = dialogButtonWidth;
        this.dialogButtonHeight = dialogButtonHeight;
        this.dialogSpacing = dialogSpacing;

        this.hudButtonWidth = hudButtonWidth;
        this.hudButtonHeight = hudButtonHeight;

        this.dragOffset = dragOffset;
    }

    public static DeviceType getDeviceType(int width, int height) {
        if (width > XHDPI.minWidth) {
            return XHDPI;
        } else if (width > HDPI.minWidth) {
            return HDPI;
        } else if (width > MDPI.minWidth) {
            return MDPI;
        } else if (width > LDPI.minWidth) {
            return LDPI;
        } else {
            return DESKTOP;
        }
    }
}
