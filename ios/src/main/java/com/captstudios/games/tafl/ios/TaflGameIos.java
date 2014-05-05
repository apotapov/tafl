package com.captstudios.games.tafl.ios;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.utils.device.DeviceType;
import com.captstudios.games.tafl.core.utils.device.TaflGameConfig;

public class TaflGameIos extends IOSApplication.Delegate {

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = false;
        config.orientationPortrait = true;

        TaflGameConfig gameConfig = new TaflGameConfig();
        gameConfig.deviceType = DeviceType.IOS;

        return new IOSApplication(new TaflGame(gameConfig), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = null;
        try {
            pool = new NSAutoreleasePool();
            UIApplication.main(argv, null, TaflGameIos.class);
        } finally {
            if (pool != null) {
                pool.close();
            }
        }
    }
}
