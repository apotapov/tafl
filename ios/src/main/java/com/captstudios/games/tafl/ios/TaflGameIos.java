package com.captstudios.games.tafl.ios;

import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.DeviceType;
import com.captstudios.games.tafl.core.utils.TaflGameConfig;

public class TaflGameIos extends IOSApplication.Delegate {

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = false;
        config.orientationPortrait = true;

        TaflGameConfig gameConfig = new TaflGameConfig();
        gameConfig.deviceType = DeviceType.LDPI;

        return new IOSApplication(new TaflGame(gameConfig), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, TaflGameIos.class);
        pool.drain();
    }
}
