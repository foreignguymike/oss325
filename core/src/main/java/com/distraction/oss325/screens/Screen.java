package com.distraction.oss325.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;

public abstract class Screen {

    protected Context context;

    public boolean transparent = false;

    protected OrthographicCamera cam;
    protected OrthographicCamera uiCam;

    protected OrthographicCamera debugCamera;

    protected SpriteBatch sb;

    protected Screen(Context context) {
        this.context = context;
        this.sb = context.sb;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        uiCam = new OrthographicCamera();
        uiCam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Constants.SWIDTH, Constants.SHEIGHT);
    }

    public abstract void input();

    public abstract void update(float dt);

    public abstract void render();

}
