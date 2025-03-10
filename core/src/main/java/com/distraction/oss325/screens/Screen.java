package com.distraction.oss325.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;

public abstract class Screen {

    protected Context context;

    public boolean transparent = false;

    protected OrthographicCamera cam;
    protected OrthographicCamera uiCam;

    protected Screen(Context context) {
        this.context = context;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        uiCam = new OrthographicCamera();
        uiCam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
    }

    public abstract void update(float dt);

    public abstract void render();

}
