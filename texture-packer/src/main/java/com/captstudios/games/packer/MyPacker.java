package com.captstudios.games.packer;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class MyPacker {
    public static void main (String[] args) throws Exception {
        String input = args[0];
        String output = args[1];

        TexturePacker.Settings settings = new Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = settings.maxWidth;
        settings.square= true;
        settings.filterMin = TextureFilter.MipMapLinearLinear;
        settings.filterMag = TextureFilter.MipMapLinearLinear;
        settings.rotation = false;
        settings.format = Format.RGB888;
        settings.outputFormat = "jpg";
        settings.jpegQuality = 0.9f;

        String[] convert = new String[] { "backgrounds" };

        for (String name : convert) {
            TexturePacker.process(settings, input + "/" + name, output, name);
        }

        convert = new String[] { "splash" };

        settings.maxWidth = 1024;
        settings.maxHeight = settings.maxWidth;

        for (String name : convert) {
            TexturePacker.process(settings, input + "/" + name, output, name);
        }

        settings.maxWidth = 2048;
        settings.maxHeight = settings.maxWidth;
        settings.format = Format.RGBA8888;
        settings.outputFormat = "png";

        convert = new String[] { "pieces" };
        for (String name : convert) {
            TexturePacker.process(settings, input + "/" + name, output, name);
        }
    }
}
