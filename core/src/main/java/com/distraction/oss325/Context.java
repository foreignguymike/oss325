package com.distraction.oss325;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.oss325.screens.ScreenManager;
import com.distraction.oss325.screens.TitleScreen;

public class Context {

    public ScreenManager sm;
    public SpriteBatch sb;

    public Context() {
        sm = new ScreenManager(new TitleScreen(this));
        sb = new SpriteBatch();
    }

    public void dispose() {
        sb.dispose();
    }

}
