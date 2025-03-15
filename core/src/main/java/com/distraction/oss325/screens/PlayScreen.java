package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;
import com.distraction.oss325.entity.Background;
import com.distraction.oss325.entity.Bomb;
import com.distraction.oss325.entity.Interactable;
import com.distraction.oss325.entity.LaunchAngle;
import com.distraction.oss325.entity.LaunchPower;
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

    private final BitmapFont font = new BitmapFont();

    private final float ceil = Constants.HEIGHT - 20;
    private final float floor = 20;

    private final List<Interactable> interactables;

    private LaunchAngle launchAngle;
    private LaunchPower launchPower;
    private float rad;
    private float power;

    private final List<Background> bgs;

    private final List<Particle> particles;

    public PlayScreen(Context context) {
        super(context);
        pixel = context.getPixel();

        player = new Player(context);
        interactables = new ArrayList<>();

        bgs = new ArrayList<>();
        bgs.add(new Background(context.getImage("floor"), cam));
        bgs.add(new Background(context.getImage("ceil"), cam));
        bgs.get(1).y = Constants.HEIGHT - 20;

        particles = new ArrayList<>();

        reset();

        in = new Transition(context, Transition.Type.CHECKERED_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.CHECKERED_OUT, 0.5f, () -> context.sm.replace(new PlayScreen(context)));
    }

    private void reset() {
        state = State.RAD;

        player.x = 0;
        player.y = 25;
        player.setBounds(ceil, floor);
        player.reset();

        cam.zoom = 0.5f;

        interactables.clear();

        launchAngle = new LaunchAngle(context);
        launchAngle.x = player.x - 8;
        launchAngle.y = player.y + 37;

        launchPower = new LaunchPower(context);
        launchPower.x = player.x;
        launchPower.y = player.y + 25;
    }

    /**
     * Get the next item in the list. Rules:
     * - Every INTERVAL is a new item.
     * - Every 10 items is a STOP.
     * - Every 3 items is a SLOW.
     * - Everything else is a BOMB.
     */
    private List<Interactable> nextInteractables(int x) {
        List<Interactable> list = new ArrayList<>();
        int index = x / INTERVAL;
        if (index % 10 == 0) {
            list.add(new Stop(context));
        } else if (index % 3 == 0) {
            list.add(new SlowSign(context));
        } else {
            list.add(new Bomb(context));
        }
        for (Interactable i : list) {
            i.x = x;
            i.y = floor + i.h / 2;
        }
        return list;
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
            if (state == State.RAD) {
                rad = launchAngle.rad;
                state = State.POWER;
            } else if (state == State.POWER) {
                power = launchPower.getPower();
                state = State.GO;
                player.kick(rad, power);

                launchAngle = null;
                launchPower = null;
            }
        }
    }

    @Override
    public void update(float dt) {
        // update transitions
        in.update(dt);
        out.update(dt);

        // update player
        player.update(dt);

        // update camera
        cam.position.x = player.x;
        cam.zoom = 0.5f + (cam.position.x - 50) / 500;
        if (cam.zoom > 1f) cam.zoom = 1f;
        cam.position.y = Constants.HEIGHT / (2f / cam.zoom);
        cam.update();

        // update background
        for (Background bg : bgs) bg.update(dt);

        // add interactables
        int nextItem = Utils.ceilTo((int) (player.x + Constants.WIDTH / 2f), INTERVAL);
        if (interactables.isEmpty() || interactables.getLast().x < nextItem) {
            interactables.addAll(nextInteractables(nextItem));
        }

        // update bombs and check collision
        for (int i = 0; i < interactables.size(); i++) {
            Interactable e = interactables.get(i);
            e.update(dt);
            if (player.intersects(e)) {
                e.interact(context, player, particles);
            }
            if (e.remove || e.x < player.x - Constants.WIDTH) interactables.remove(i--);
        }

        // update particles
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.update(dt);
            if (p.remove) particles.remove(i--);
        }

        // update launcher
        if (state == State.RAD) launchAngle.update(dt);
        if (state == State.POWER) launchPower.update(dt);
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

        font.setColor(Constants.BLACK);
        sb.setProjectionMatrix(debugCamera.combined);
        font.draw(sb, "player pos: " + (int) player.x + ", " + (int) player.y, 10, Constants.SHEIGHT - 60);
        font.draw(sb, "player speed: " + player.dx, 10, Constants.SHEIGHT - 75);
        font.draw(sb, "player stopped: " + player.stopped, 10, Constants.SHEIGHT - 90);

        sb.setProjectionMatrix(cam.combined);

        sb.setColor(1, 1, 1, 1);
        for (Interactable e : interactables) e.render(sb);

        sb.setColor(1, 1, 1, 1);
        player.render(sb);

        sb.setColor(1, 1, 1, 1);
        for (Particle p : particles) p.render(sb);

        sb.setColor(1, 1, 1, 1);
        if (launchAngle != null) launchAngle.render(sb);
        if (launchPower != null) launchPower.render(sb);

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);

        sb.end();

    }

}
