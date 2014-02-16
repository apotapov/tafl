package com.pactstudios.games.tafl.core.consts;

import com.roundtriangles.games.zaria.services.utils.FontDefinition;

public enum DeviceType {
    DESKTOP (Assets.Fonts.GOTHAM_MEDIUM_PHONE,
            Constants.GameConstants.DESKTOP_MIN_RESOLUTION,
            Assets.Skin.SKIN_STYLE_PHONE_DEFAULT,
            Assets.Skin.SKIN_STYLE_PHONE_MENU,
            Assets.Skin.SKIN_STYLE_PHONE_DIALOG,
            Assets.Skin.SKIN_STYLE_PHONE_PLAYER,
            Constants.ScreenConstants.PHONE_BUTTON_WIDTH,
            Constants.ScreenConstants.PHONE_BUTTON_HEIGHT,
            Constants.ScreenConstants.PHONE_LIST_WIDTH,
            Constants.ScreenConstants.PHONE_LIST_HEIGHT,
            Constants.ScreenConstants.PHONE_SPACING),


            PHONE (Assets.Fonts.GOTHAM_MEDIUM_PHONE,
                    Constants.GameConstants.PHONE_MIN_RESOLUTION,
                    Assets.Skin.SKIN_STYLE_PHONE_DEFAULT,
                    Assets.Skin.SKIN_STYLE_PHONE_MENU,
                    Assets.Skin.SKIN_STYLE_PHONE_DIALOG,
                    Assets.Skin.SKIN_STYLE_PHONE_PLAYER,
                    Constants.ScreenConstants.PHONE_BUTTON_WIDTH,
                    Constants.ScreenConstants.PHONE_BUTTON_HEIGHT,
                    Constants.ScreenConstants.PHONE_LIST_WIDTH,
                    Constants.ScreenConstants.PHONE_LIST_HEIGHT,
                    Constants.ScreenConstants.PHONE_SPACING),


                    TABLET (Assets.Fonts.GOTHAM_MEDIUM_TABLET,
                            Constants.GameConstants.TABLET_MIN_RESOLUTION,
                            Assets.Skin.SKIN_STYLE_TABLET_DEFAULT,
                            Assets.Skin.SKIN_STYLE_TABLET_MENU,
                            Assets.Skin.SKIN_STYLE_TABLET_DIALOG,
                            Assets.Skin.SKIN_STYLE_TABLET_PLAYER,
                            Constants.ScreenConstants.TABLET_BUTTON_WIDTH,
                            Constants.ScreenConstants.TABLET_BUTTON_HEIGHT,
                            Constants.ScreenConstants.TABLET_LIST_WIDTH,
                            Constants.ScreenConstants.TABLET_LIST_HEIGHT,
                            Constants.ScreenConstants.TABLET_SPACING);

    public FontDefinition font;
    public int minResolution;

    public String defaultStyle;
    public String menuStyle;
    public String dialogStyle;
    public String playerTagStyle;

    public int menuButtonWidth;
    public int menuButtonHeight;
    public int menuListWidth;
    public int menuListHeight;
    public int menuSpacing;

    private DeviceType(FontDefinition font,
            int minResolution,
            String defaultStyle,
            String menuStyle,
            String dialogStyle,
            String playerTagStyle,
            int menuButtonWidth,
            int menuButtonHeight,
            int menuListWidth,
            int menuListHeight,
            int menuSpacing) {

        this.font = font;
        this.minResolution = minResolution;

        this.defaultStyle = defaultStyle;
        this.menuStyle = menuStyle;
        this.dialogStyle = dialogStyle;
        this.playerTagStyle = playerTagStyle;

        this.menuButtonWidth = menuButtonWidth;
        this.menuButtonHeight = menuButtonHeight;
        this.menuListWidth = menuListWidth;
        this.menuListHeight = menuListHeight;
        this.menuSpacing = menuSpacing;
    }

    public static DeviceType getDeviceType(int width, int height) {
        int resolution = width * height;
        if (resolution > TABLET.minResolution) {
            return TABLET;
        } else if (resolution > PHONE.minResolution) {
            return PHONE;
        } else {
            return DESKTOP;
        }
    }
}
