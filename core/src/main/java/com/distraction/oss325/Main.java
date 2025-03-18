package com.distraction.oss325;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.distraction.oss325.gj.GameJoltClient;

public class Main extends ApplicationAdapter {

    private static final float TICK = 1f / 60f;

    private Context context;

    private float accum;

    @Override
    public void create() {
        context = new Context();

        GameJoltClient client = new GameJoltClient();
        client.setGjScoreTableMapper(id -> Constants.LEADERBOARD_ID);
        client.initialize(Constants.APP_ID, Constants.API_KEY);
        context.client = client;

        Gdx.input.setCatchKey(Input.Keys.UP, true);
        Gdx.input.setCatchKey(Input.Keys.DOWN, true);
        Gdx.input.setCatchKey(Input.Keys.SPACE, true);
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
