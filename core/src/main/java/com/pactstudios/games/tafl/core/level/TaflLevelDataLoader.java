package com.pactstudios.games.tafl.core.level;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

@SuppressWarnings("rawtypes")
public class TaflLevelDataLoader extends AsynchronousAssetLoader<TaflLevel, AssetLoaderParameters<TaflLevel>>{

    Json json;
    Array<AssetDescriptor> dependencies;


    public TaflLevelDataLoader(FileHandleResolver resolver) {
        super(resolver);
        this.json = new Json();
        this.dependencies = new Array<AssetDescriptor>();
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName,
            FileHandle file, AssetLoaderParameters<TaflLevel> parameter) {
        return dependencies;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName,
            FileHandle file, AssetLoaderParameters<TaflLevel> parameter) {
    }

    @Override
    public TaflLevel loadSync(AssetManager manager, String fileName,
            FileHandle file, AssetLoaderParameters<TaflLevel> parameter) {

        TaflLevel level = json.fromJson(TaflLevel.class, file);
        level.file = fileName;
        return level;
    }


}
