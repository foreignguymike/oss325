package com.distraction.oss325;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.screens.PlayScreen;
import com.distraction.oss325.screens.ScreenManager;

public class Context {

    public static final String FONT_NAME_VCR20 = "fonts/vcr20.fnt";
    public static final String FONT_NAME_M5X716 = "fonts/m5x716.fnt";

    private static final String ATLAS = "oss325.atlas";

    public AssetManager assets;

    public ScreenManager sm;
    public SpriteBatch sb;

    public Context() {
        assets = new AssetManager();
        assets.load(ATLAS, TextureAtlas.class);
        assets.load(FONT_NAME_M5X716, BitmapFont.class);
        assets.load(FONT_NAME_VCR20, BitmapFont.class);
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

    public BitmapFont getFont(String name) {
        return getFont(name, 1f);
    }

    public BitmapFont getFont(String name, float scale) {
        BitmapFont originalFont = assets.get(name, BitmapFont.class);
        BitmapFont scaledFont = new BitmapFont(originalFont.getData().getFontFile(), originalFont.getRegion(), false);
        scaledFont.getData().setScale(scale);
        return scaledFont;
    }

    public void dispose() {
        sb.dispose();
    }

}
