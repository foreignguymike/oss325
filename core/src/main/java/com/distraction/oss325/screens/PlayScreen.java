package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;
import com.distraction.oss325.entity.Background;
import com.distraction.oss325.entity.Bomb;
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
        GO
    }

    private State state = State.RAD;

    private final TextureRegion pixel;

    private final Player player;
    private final TextureRegion[] explosionSprites;

    private final FontEntity distanceFont;
    private final FontEntity speedFont;
    private final FontEntity boosterFont;

    private final float floor = 20;

    private final List<Interactable> interactables;
    private final Mini mini;

    private LaunchAngle launchAngle;
    private LaunchPower launchPower;
    private float rad;

    private final List<Background> bgs;

    private final List<Particle> particles;

    private int boosterCount = 3;

    public PlayScreen(Context context) {
        super(context);
        pixel = context.getPixel();
        explosionSprites = context.getImage("explosion").split(34, 36)[0];

        player = new Player(context);
        interactables = new ArrayList<>();
        mini = new Mini(context, cam, interactables, Constants.WIDTH / 2f, Constants.HEIGHT - 100);

        bgs = new ArrayList<>();
        bgs.add(new Background(context.getImage("floor"), cam));

        particles = new ArrayList<>();

        reset();

        in = new Transition(context, Transition.Type.CHECKERED_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.CHECKERED_OUT, 0.5f, () -> context.sm.replace(new PlayScreen(context)));

        BitmapFont font = context.getFont(Context.FONT_NAME_M5X716, 4f);
        distanceFont = new FontEntity(
            font,
            getDistanceString(),
            Constants.WIDTH / 2f,
            Constants.HEIGHT - 30,
            FontEntity.Alignment.CENTER
        );
        distanceFont.setColor(Constants.BLACK);

        font = context.getFont(Context.FONT_NAME_M5X716, 2f);
        speedFont = new FontEntity(
            font,
            getSpeedString(),
            10,
            Constants.HEIGHT - 18,
            FontEntity.Alignment.LEFT
        );
        speedFont.setColor(Constants.BLACK);

        boosterFont = new FontEntity(
            font,
            "Booster Available! [SPACE]",
            Constants.WIDTH / 2f,
            8,
            FontEntity.Alignment.CENTER
        );
        boosterFont.setColor(Constants.WHITE);

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
     * - Every 10 items is a STOP.
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
            if (index % 10 == 0) {
                temp.add(new Stop(context));
            } else if (index % 3 == 0) {
                temp.add(new SlowSign(context));
            } else {
                temp.add(new Bomb(context));
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

    @Override
    public void input() {
        if (ignoreInput) return;
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            ignoreInput = true;
            out.setCallback(() -> context.sm.replace(new PlayScreen(context)));
            out.start();
        }

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
    }

    @Override
    public void update(float dt) {
        // update transitions
        in.update(dt);
        out.update(dt);

        // update player
        if (state == State.GO) {
            player.update(dt);
        }

        // update camera
        cam.position.x = player.x;
        calculateZoom(dt);
        cam.position.y = Constants.HEIGHT / (2f / cam.zoom) + player.y / 2 * (1 - cam.zoom);
        cam.update();

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
    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.BLUE);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(cam.combined);
        for (Background bg : bgs) bg.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        distanceFont.render(sb);
        speedFont.render(sb);
        if (boosterCount > 0 && player.launched) boosterFont.render(sb);

        sb.setProjectionMatrix(cam.combined);

        sb.setColor(1, 1, 1, 1);
        for (Interactable e : interactables) e.render(sb);

        sb.setProjectionMatrix(uiCam.combined);
        mini.render(sb);

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(1, 1, 1, 1);
        player.render(sb);
        if (player.dx > 1500) particles.add(
            new Particle(
                explosionSprites,
                2 / 60f,
                MathUtils.random(-10, 10) + player.x,
                MathUtils.random(-10, 10) + player.y
            )
        );

        sb.setColor(1, 1, 1, 1);
        for (Particle p : particles) p.render(sb);

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(cam.combined);
        if (state == State.RAD || state == State.POWER) {
            launchAngle.render(sb);
            launchPower.render(sb);
        }

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();

    }

}
