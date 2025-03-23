package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;

public abstract class Screen {

    protected Context context;

    protected final TextureRegion pixel;

    public boolean transparent = false;

    protected OrthographicCamera cam;
    protected OrthographicCamera uiCam;
    protected final Vector3 m;

    protected SpriteBatch sb;

    protected boolean ignoreInput;

    public Transition in = null;
    public Transition out = null;

    protected Screen(Context context) {
        this.context = context;
        this.sb = context.sb;

        pixel = context.getPixel();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        uiCam = new OrthographicCamera();
        uiCam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        m = new Vector3();
    }

    protected void unproject() {
        m.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        uiCam.unproject(m);
    }

    public void resume() {}

    public abstract void input();

    public abstract void update(float dt);

    public abstract void render();

}
