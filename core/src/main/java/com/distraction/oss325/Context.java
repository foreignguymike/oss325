package com.distraction.oss325;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.screens.PlayScreen;
import com.distraction.oss325.screens.ScreenManager;

public class Context {

    private static final String ATLAS = "oss325.atlas";

    public AssetManager assets;

    public ScreenManager sm;
    public SpriteBatch sb;

    public Context() {
        assets = new AssetManager();
        assets.load(ATLAS, TextureAtlas.class);
        assets.finishLoading();

        sb = new SpriteBatch();

        sm = new ScreenManager(new PlayScreen(this));
    }

    public TextureRegion getImage(String key) {
        TextureRegion region = assets.get(ATLAS, TextureAtlas.class).findRegion(key);
        if (region == null) throw new IllegalArgumentException("image " + key + " not found");
        return region;
    }

    public TextureRegion getPixel() {
        return getImage("pixel");
    }

    public void dispose() {
        sb.dispose();
    }

}
