package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;
import com.distraction.oss325.entity.Bomb;
import com.distraction.oss325.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen extends Screen {

    private final int BOMB_INTERVAL = 400;

    private final TextureRegion pixel;
    private final TextureRegion stripe;

    private final Player player;

    private final Affine2 affine = new Affine2();
    private final BitmapFont font = new BitmapFont();

    private final float floor = 15;

    private final List<Bomb> bombs;

    public PlayScreen(Context context) {
        super(context);
        pixel = context.getPixel();
        stripe = context.getImage("stripe");

        player = new Player(context);
        bombs = new ArrayList<>();

        reset();
    }

    private void reset() {
        player.x = 50;
        player.y = 25;
        player.setFloor(floor);
        player.reset();
        bombs.clear();
    }

    /**
     * We should keep a list of bombs every BOMB_INTERVAL
     *
     * @param dist current poko distance
     */
    private void checkBombs(float dist) {
        // add new bombs if necessary
        int nextBombX = Utils.ceilTo((int) (dist + Constants.WIDTH / 2f), BOMB_INTERVAL);
        if (bombs.isEmpty() || bombs.getLast().x < nextBombX) {
            Bomb bomb = new Bomb(context);
            bomb.x = nextBombX;
            bomb.y = floor + bomb.h / 2;
            bombs.add(bomb);
        }
        // remove bombs if necessary
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.remove || bomb.x < dist - Constants.WIDTH) {
                bombs.remove(i--);
            }
        }
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            reset();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//            player.kick(3.1415f / 4, 1000f);
            player.kick(0.4f, 1000f);
        }

        // update player
        player.update(dt);

        // update camera
        cam.position.x = player.x;
        cam.update();

        // check bombs
        checkBombs(player.x);
    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.PEACH);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        font.setColor(Color.BLACK);
        sb.setProjectionMatrix(debugCamera.combined);
        font.draw(sb, "player pos: " + (int) player.x + ", " + (int) player.y, 10, Constants.SHEIGHT - 10);
        font.draw(sb, "player speed: " + player.dx, 10, Constants.SHEIGHT - 25);
        font.draw(sb, "player stopped: " + player.stopped, 10, Constants.SHEIGHT - 40);

        float horizon = 50;
        int stripeWidth = 100;
        int stripeHeight = 50;
        int left = Utils.roundNearestFloor(cam.position.x - Constants.WIDTH / 2f, stripeWidth * 2);
        int count = Constants.WIDTH / stripeWidth + 1;

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.GREEN);
        sb.draw(pixel, 0, 0, Constants.WIDTH, stripeHeight);

        sb.setProjectionMatrix(cam.combined);
        for (int i = 0; i < count; i++) {
            float x1 = left + i * stripeWidth * 2;
            float x2 = x1 + stripeWidth;
            affine.idt();
            affine.translate(x1, stripeHeight);
            affine.scale(1f, 1f * stripeHeight / stripe.getRegionHeight());
            affine.shear((cam.position.x - x1) / horizon, 0f);
            sb.setColor(Constants.DARK_GREEN);
            sb.draw(stripe, x2 - x1, -stripe.getRegionHeight(), affine);
            affine.idt();
            affine.translate(x1, stripeHeight);
            affine.scale(1f, 1f * stripeHeight / stripe.getRegionHeight());
            affine.shear((cam.position.x - x2) / horizon, 0f);
            sb.draw(stripe, x2 - x1, -stripe.getRegionHeight(), affine);
        }

        sb.setColor(1, 1, 1, 1);
        for (Bomb bomb : bombs) {
            bomb.render(sb);
        }

        sb.setColor(1, 1, 1, 1);
        player.render(sb);

        sb.end();

    }

}
