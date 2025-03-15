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
import com.distraction.oss325.entity.LaunchAngle;
import com.distraction.oss325.entity.LaunchPower;
import com.distraction.oss325.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen extends Screen {

    private final int BOMB_INTERVAL = 1000;

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

    private final List<Bomb> bombs;

    private LaunchAngle launchAngle;
    private LaunchPower launchPower;
    private float rad;
    private float power;

    private final List<Background> bgs;

    public PlayScreen(Context context) {
        super(context);
        pixel = context.getPixel();

        player = new Player(context);
        bombs = new ArrayList<>();

        bgs = new ArrayList<>();
        bgs.add(new Background(context.getImage("floor"), cam));
        bgs.add(new Background(context.getImage("ceil"), cam));
        bgs.get(1).y = Constants.HEIGHT - 20;

        reset();
    }

    private void reset() {
        state = State.RAD;

        player.x = 0;
        player.y = 25;
        player.setBounds(ceil, floor);
        player.reset();

        cam.zoom = 0.5f;

        bombs.clear();

        launchAngle = new LaunchAngle(context);
        launchAngle.x = player.x -8;
        launchAngle.y = player.y + 37;

        launchPower = new LaunchPower(context);
        launchPower.x = player.x;
        launchPower.y = player.y + 30;
    }

    /**
     * We should keep a list of bombs every BOMB_INTERVAL
     *
     * @param dist current poko distance
     */
    private void addBombs(float dist) {
        // add new bombs if necessary
        int nextBombX = Utils.ceilTo((int) (dist + Constants.WIDTH / 2f), BOMB_INTERVAL);
        if (bombs.isEmpty() || bombs.getLast().x < nextBombX) {
            Bomb bomb = new Bomb(context);
            bomb.x = nextBombX;
            bomb.y = floor + bomb.h / 2;
            bombs.add(bomb);
        }
    }

    @Override
    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            reset();
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

        // add bombs
        addBombs(player.x);

        // update bombs and check collision
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            bomb.update(dt);
            if (player.intersects(bomb)) {
                bomb.remove = true;
                player.bomb();
            }
            if (bomb.remove || bomb.x < player.x - Constants.WIDTH) {
                bombs.remove(i--);
            }
        }

        // update launcher
        if (state == State.RAD) launchAngle.update(dt);
        if (state == State.POWER) launchPower.update(dt);
    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.OLIVE);
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
        for (Bomb bomb : bombs) {
            bomb.render(sb);
        }

        sb.setColor(1, 1, 1, 1);
        player.render(sb);

        sb.setColor(1, 1, 1, 1);
        if (launchAngle != null) launchAngle.render(sb);
        if (launchPower != null) launchPower.render(sb);

        sb.end();

    }

}
