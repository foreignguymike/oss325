package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;
import com.distraction.oss325.entity.Background;
import com.distraction.oss325.entity.Bomb;
import com.distraction.oss325.entity.Button;
import com.distraction.oss325.entity.FontEntity;
import com.distraction.oss325.entity.Interactable;
import com.distraction.oss325.entity.LaunchAngle;
import com.distraction.oss325.entity.LaunchPower;
import com.distraction.oss325.entity.Mini;
import com.distraction.oss325.entity.Particle;
import com.distraction.oss325.entity.Player;
import com.distraction.oss325.entity.SlowSign;
import com.distraction.oss325.entity.Stop;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen extends Screen {

    private final int INTERVAL = 500;

    private enum State {
        RAD,
        POWER,
        GO,
        DONE
    }

    private State state = State.RAD;

    private final Player player;
    private final TextureRegion[] explosion;
    private final TextureRegion[] blueExplosion;

    private final FontEntity distanceFont;
    private final FontEntity speedFont;

    private final Button restartButton;
    private final Button backButton;

    private final float floor = 20;

    private final List<Interactable> interactables;
    private final Mini mini;

    private LaunchAngle launchAngle;
    private LaunchPower launchPower;
    private float rad;

    private final List<Background> bgs;
    private final OrthographicCamera bgCam; // super hacky, too lazy

    private final List<Particle> particles;

    private final TextureRegion booster;
    private int boosterCount = 3;

    private float doneAlpha;
    private final FontEntity doneFont;
    private final FontEntity submittedFont;
    private final Button submitButton;
    private final Button scoresButton;
    private boolean loading;
    private float time;

    public PlayScreen(Context context) {
        super(context);
        explosion = context.getImage("explosion").split(34, 36)[0];
        blueExplosion = context.getImage("blueexplosion").split(15, 15)[0];

        player = new Player(context);
        interactables = new ArrayList<>();
        mini = new Mini(context, cam, interactables, Constants.WIDTH / 2f, Constants.HEIGHT - 40);

        bgs = new ArrayList<>();
        bgs.add(new Background(context.getImage("bg2"), cam, 20));
        bgs.add(new Background(context.getImage("bg1"), cam, 15));
        bgs.add(new Background(context.getImage("floor"), cam));
        bgs.get(0).y = 30;
        bgs.get(1).y = 0;

        bgCam = new OrthographicCamera();
        bgCam.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        particles = new ArrayList<>();

        reset();

        in = new Transition(context, Transition.Type.CHECKERED_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.CHECKERED_OUT, 0.5f, () -> context.sm.replace(new PlayScreen(context)));

        BitmapFont font = context.getFont(Context.FONT_NAME_M5X716, 4f);
        distanceFont = new FontEntity(
            font,
            getDistanceString(),
            10,
            Constants.HEIGHT - 26,
            FontEntity.Alignment.LEFT
        );
        distanceFont.setColor(Constants.BLACK);

        font = context.getFont(Context.FONT_NAME_M5X716, 2f);
        speedFont = new FontEntity(
            font,
            getSpeedString(),
            10,
            distanceFont.y - 26,
            FontEntity.Alignment.LEFT
        );
        speedFont.setColor(Constants.BLACK);

        booster = context.getImage("boost");

        backButton = new Button(context.getImage("back"), Constants.WIDTH - 24, Constants.HEIGHT - 24);
        restartButton = new Button(context.getImage("restart"), Constants.WIDTH - 60, Constants.HEIGHT - 24);

        doneFont = new FontEntity(context.getFont(Context.FONT_NAME_VCR20), "", Constants.WIDTH / 2f, Constants.HEIGHT / 2f, FontEntity.Alignment.CENTER);
        submitButton = new Button(context.getImage("submit"), Constants.WIDTH / 2f, Constants.HEIGHT / 4f);
        submittedFont = new FontEntity(context.getFont(Context.FONT_NAME_M5X716, 2f), "Submitted!", submitButton.x, submitButton.y - 4, FontEntity.Alignment.CENTER);
        scoresButton = new Button(context.getImage("scores"), Constants.WIDTH / 2f, Constants.HEIGHT / 4f - 50);

    }

    private int getSpeed() {
        return (int) (player.dx / 100);
    }

    private String getSpeedString() {
        return getSpeed() + "m/s";
    }

    private int getDistance() {
        return (int) (player.x / 100);
    }

    private String getDistanceString() {
        return getDistance() + "m";
    }

    private void reset() {
        state = State.RAD;

        player.x = 0;
        player.y = 25;
        player.setBounds(Constants.HEIGHT, floor);
        player.reset();

        cam.zoom = 0.5f;

        interactables.clear();

        launchAngle = new LaunchAngle(context);
        launchAngle.x = player.x - 8;
        launchAngle.y = player.y + 37;

        launchPower = new LaunchPower(context);
        launchPower.x = launchAngle.x + 8;
        launchPower.y = launchAngle.y - 12;
    }

    /**
     * Get the next item in the list. Rules:
     * - Every INTERVAL is a new item.
     * - Every 13 items is a STOP.
     * - Every 3 items is a SLOW.
     * - Everything else is a BOMB.
     */
    private List<Interactable> nextInteractables(int x) {
        int cx;
        if (interactables.isEmpty()) cx = INTERVAL;
        else cx = (int) interactables.getLast().x + INTERVAL;

        List<Interactable> list = new ArrayList<>();
        while (cx <= x) {
            List<Interactable> temp = new ArrayList<>();
            int index = cx / INTERVAL;
            if (index % 3 == 0) {
                temp.add(new SlowSign(context));
            } else {
                temp.add(new Bomb(context));
            }
            if (index / 13 > 30) {
                if (index % 10 == 0) {
                    temp.clear();
                    temp.add(new Stop(context));
                }
            } else {
                if (index % 13 == 0) {
                    temp.clear();
                    temp.add(new Stop(context));
                }
            }
            for (Interactable i : temp) {
                i.x = cx;
                i.y = floor + i.h / 2;
            }
            list.addAll(temp);
            temp.clear();
            cx = (int) list.getLast().x + INTERVAL;
        }
        return list;
    }

    private void calculateZoom(float dt) {
        if (state == State.GO) {
            cam.zoom += dt;
            if (cam.zoom > 1f) cam.zoom = 1f;
        } else {
            cam.zoom -= dt;
            if (cam.zoom < 0.5f) cam.zoom = 0.5f;
        }
    }

    private void submit() {
        if (context.data.name.isEmpty() || !context.leaderboardsInitialized) return;
        if (context.data.submitted) return;
        if (loading) return;
        loading = true;
        context.audio.playSound("submit");
        context.submitScore(context.data.name, context.data.score, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String res = httpResponse.getResultAsString();
                // throwing an exception with SubmitScoreResponse here for some reason
                // just doing a sus true check instead
                if (res.contains("true")) {
                    context.data.submitted = true;
                    context.fetchLeaderboard((_) -> {});
                } else {
                    failed(null);
                }
                loading = false;
            }

            @Override
            public void failed(Throwable t) {
                ignoreInput = false;
                loading = false;
            }

            @Override
            public void cancelled() {
                failed(null);
            }
        });
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (state == State.GO && !player.stopped) {
                if (boosterCount > 0) {
                    boosterCount--;
                    state = State.RAD;
                }
            } else if (state == State.RAD) {
                rad = launchAngle.rad;
                state = State.POWER;
            } else if (state == State.POWER) {
                float power = launchPower.getPower();
                if (player.launched) {
                    player.boost(rad, power);
                } else {
                    player.launch(rad, power);
                }
                state = State.GO;
                launchAngle.reset();
                launchPower.reset();
            }
        }

        if (Gdx.input.justTouched()) {
            unproject();
            if (state == State.DONE) {
                if (submitButton.contains(m.x, m.y, 2, 2)) {
                    submit();
                }
            }
            if (restartButton.contains(m.x, m.y, 2, 2)) {
                ignoreInput = true;
                out.setCallback(() -> context.sm.replace(new PlayScreen(context)));
                out.start();
            }
            if (backButton.contains(m.x, m.y, 2, 2)) {
                ignoreInput = true;
                out = new Transition(context, Transition.Type.FLASH_OUT, 0.5f, () -> context.sm.replace(new TitleScreen(context)));
                out.start();
            }
            if (state == State.DONE && scoresButton.contains(m.x, m.y, 2, 2)) {
                ignoreInput = true;
                context.sm.push(new ScoreScreen(context));
            }
        }
    }

    @Override
    public void update(float dt) {
        time += dt;

        // update transitions
        in.update(dt);
        out.update(dt);

        // update player
        int boosterInterval = 1000;
        int currentBoosterInterval = getDistance() / boosterInterval;
        if (state == State.GO) {
            player.update(dt);
        }
        int newBoosterInterval = getDistance() / boosterInterval;
        if (currentBoosterInterval != newBoosterInterval) {
            boosterCount++;
            if (boosterCount > 3) boosterCount = 3;
        }

        // update camera
        cam.position.x = player.x;
        calculateZoom(dt);
        float effectiveHeight = Constants.HEIGHT * cam.zoom;
        float lower = effectiveHeight / 2f;
        float upper = Constants.HEIGHT - effectiveHeight / 2f;
        cam.position.y = MathUtils.clamp(player.y, lower, upper);
        cam.update();
        bgCam.position.y = cam.position.y;
        bgCam.zoom = cam.zoom;
        bgCam.update();

        // update backgrounds
        for (Background bg : bgs) bg.update(dt);

        // add interactables
        int px = Utils.floorTo((int) player.x, INTERVAL);
        int nextItem;
        if (interactables.isEmpty()) nextItem = INTERVAL * 6;
        else nextItem = px + INTERVAL * 6;
        if (interactables.isEmpty() || interactables.getLast().x < nextItem) {
            interactables.addAll(nextInteractables(nextItem));
        }

        // update interactables and check collision
        for (int i = 0; i < interactables.size(); i++) {
            Interactable e = interactables.get(i);
            e.update(dt);
            if (player.intersects(e)) {
                e.interact(context, player, particles);
            }
            if (e.remove || e.x < player.x - INTERVAL * 6) interactables.remove(i--);
        }
        mini.update(dt);

        // update particles
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.update(dt);
            if (p.remove) particles.remove(i--);
        }

        // update launcher
        if (player.launched) {
            launchAngle.x = player.x - 50;
            launchAngle.y = player.y;
            launchPower.x = launchAngle.x + 8;
            launchPower.y = launchAngle.y - 12;
        }
        if (state == State.RAD) launchAngle.update(dt);
        if (state == State.POWER) launchPower.update(dt);

        // update distance
        distanceFont.setText(getDistanceString());
        speedFont.setText(getSpeedString());

        if (player.stopped && state != State.DONE) {
            state = State.DONE;
            if (context.isHighscore(context.data.name, getDistance())) {
                doneFont.setText("HIGH SCORE! " + getDistanceString());
                context.data.score = getDistance();
            } else {
                doneFont.setText("Try again!");
            }
        }
        if (state == State.DONE) {
            doneAlpha = MathUtils.clamp(doneAlpha + dt, 0, 0.8f);
        }

    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.BLUE);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(bgCam.combined);
        for (Background bg : bgs) bg.render(sb);

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(cam.combined);
        for (Interactable e : interactables) e.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        mini.render(sb);

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(1, 1, 1, 1);
        player.render(sb);
        if (state == State.GO) {
            if (player.dx > 3000) {
                for (int i = 0; i < 2; i++) {
                    particles.add(
                        new Particle(
                            blueExplosion,
                            2.5f / 60f,
                            MathUtils.random(-60, 0) + player.x,
                            MathUtils.random(-20, 20) + player.y
                        )
                    );
                }
            } else if (player.dx > 1500) {
                particles.add(
                    new Particle(
                        explosion,
                        3 / 60f,
                        MathUtils.random(-10, 10) + player.x,
                        MathUtils.random(-10, 10) + player.y
                    )
                );
            }
        }

        sb.setColor(1, 1, 1, 1);
        for (Particle p : particles) p.render(sb);

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(cam.combined);
        if (state == State.RAD || state == State.POWER) {
            launchAngle.render(sb);
            launchPower.render(sb);
        }

        sb.setProjectionMatrix(uiCam.combined);
        distanceFont.render(sb);
        speedFont.render(sb);
        for (int i = 0; i < boosterCount; i++) {
            sb.draw(booster, 10 + i * 25, Constants.HEIGHT - 90);
        }
        restartButton.render(sb);
        backButton.render(sb);

        if (state == State.DONE) {
            sb.setColor(0, 0, 0, doneAlpha);
            sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

            doneFont.render(sb);

            if (context.isHighscore(context.data.name, getDistance())) {
                if (!context.data.submitted) {
                    submitButton.render(sb);
                }
            }
            if (context.data.submitted) {
                submittedFont.render(sb);
            }
            if (loading) {
                for (int i = 0; i < 5; i++) {
                    float x = submitButton.x + submitButton.w / 2f + 10 * MathUtils.cos(-6f * time + i * 0.1f) - 5;
                    float y = submitButton.y + 10 * MathUtils.sin(-6f * time + i * 0.1f) - 5;
                    sb.draw(pixel, x, y, 2, 2);
                }
            }
            restartButton.render(sb);
            backButton.render(sb);
            scoresButton.render(sb);
        }

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();

    }

}
