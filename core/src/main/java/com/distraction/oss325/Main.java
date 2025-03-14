package com.distraction.oss325;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class Main extends ApplicationAdapter {

    private static final float TICK = 1f / 60f;

    private Context context;

    private float accum;

    @Override
    public void create() {
        context = new Context();
    }

    @Override
    public void render() {
        context.sm.input();
        // lock step, physics based game, don't want any random frame spikes to ruin things
        accum += Gdx.graphics.getDeltaTime();
        while (accum > TICK) {
            accum -= TICK;
            context.sm.update(TICK);
        }
        context.sm.render();
    }

    @Override
    public void dispose() {
        context.dispose();
    }
}
