package com.pactstudios.games.tafl.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SerializationException;

/**
 * Custom skin loader that allows to load TTF fonts.
 * @author apotapov
 *
 */
public class CustomSkinLoader extends SkinLoader {

    public static class CustomFontSerializer extends ReadOnlySerializer<BitmapFont> {

        FileHandle skinFile;
        ObjectMap<String, FreeTypeFontGenerator> fontGenerators;

        public CustomFontSerializer(FileHandle skinFile) {
            this.skinFile = skinFile;
            this.fontGenerators = new ObjectMap<String, FreeTypeFontGenerator>();
        }

        @SuppressWarnings("rawtypes")
        @Override
        public BitmapFont read(Json json, JsonValue jsonData, Class type) {
            String path = json.readValue("file", String.class, jsonData);
            int size = json.readValue("size", Integer.class, jsonData);

            try {
                FreeTypeFontGenerator generator;
                if (fontGenerators.containsKey(path)) {
                    generator = fontGenerators.get(path);
                } else {
                    FileHandle fontFile = skinFile.parent().child(path);
                    if (!fontFile.exists()) {
                        fontFile = Gdx.files.internal(path);
                    }
                    if (!fontFile.exists()) {
                        throw new SerializationException("Font file not found: " + fontFile);
                    }
                    generator = new FreeTypeFontGenerator(fontFile);
                    fontGenerators.put(path, generator);
                }
                return generator.generateFont(size);
            } catch (RuntimeException ex) {
                throw new SerializationException("Error loading bitmap font: " + path, ex);
            }
        }

    }

    public static class CustomSkin extends Skin {

        CustomFontSerializer fontSerializer;

        public CustomSkin(FileHandle file, TextureAtlas atlas) {
            super(file, atlas);
        }

        @Override
        protected Json getJsonLoader (final FileHandle skinFile) {
            Json json = super.getJsonLoader(skinFile);
            fontSerializer = new CustomFontSerializer(skinFile);
            json.setSerializer(BitmapFont.class, fontSerializer);
            return json;
        }

        @Override
        public void dispose() {
            super.dispose();
            if (fontSerializer != null) {
                for (FreeTypeFontGenerator generator : fontSerializer.fontGenerators.values()) {
                    generator.dispose();
                }
            }
        }

    }

    public CustomSkinLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Skin loadSync (AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
        String textureAtlasPath;
        if (parameter == null) {
            textureAtlasPath = file.pathWithoutExtension() + ".atlas";
        } else {
            textureAtlasPath = parameter.textureAtlasPath;
        }
        TextureAtlas atlas = manager.get(textureAtlasPath, TextureAtlas.class);
        return new CustomSkin(file, atlas);
    }

}
