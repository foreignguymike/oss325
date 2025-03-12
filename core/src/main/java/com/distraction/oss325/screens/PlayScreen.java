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
import com.distraction.oss325.entity.Player;

public class PlayScreen extends Screen {

    private final TextureRegion pixel;
    private final TextureRegion stripe;

    private final Player player;

    private final Affine2 affine = new Affine2();
    private final BitmapFont font = new BitmapFont();

    public PlayScreen(Context context) {
        super(context);
        pixel = context.getPixel();
        stripe = context.getImage("stripe");

        player = new Player(context);
        player.x = 50;
        player.y = 25;
        player.setFloor(25);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.kick(3.1415f / 4, 100f);
        }

        player.update(dt);

        cam.position.x = player.x;
        cam.update();
    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.WHITE);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        font.setColor(Color.BLACK);
        sb.setProjectionMatrix(debugCamera.combined);
        font.draw(sb, "player pos: " + (int) player.x + ", " + (int) player.y, 10, Constants.SHEIGHT - 10);
        font.draw(sb, "camera pos: " + (int) cam.position.x + ", " + (int) cam.position.y, 10, Constants.SHEIGHT - 20);

        float horizon = 50;
        int stripeWidth = 100;
        int stripeHeight = 50;
        int left = Utils.roundNearestFloor(cam.position.x - Constants.WIDTH / 2f, stripeWidth * 2);
        int count = Constants.WIDTH / stripeWidth + 1;

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.TEAL);
        sb.draw(pixel, 0, 0, Constants.WIDTH, stripeHeight);

        sb.setProjectionMatrix(cam.combined);
        for (int i = 0; i < count; i++) {
            float x1 = left + i * stripeWidth * 2;
            float x2 = x1 + stripeWidth;
            affine.idt();
            affine.translate(x1, stripeHeight);
            affine.scale(1f, 1f * stripeHeight / stripe.getRegionHeight());
            affine.shear((cam.position.x - x1) / horizon, 0f);
            sb.setColor(Constants.PURPLE);
            sb.draw(stripe, x2 - x1, -stripe.getRegionHeight(), affine);
            affine.idt();
            affine.translate(x1, stripeHeight);
            affine.scale(1f, 1f * stripeHeight / stripe.getRegionHeight());
            affine.shear((cam.position.x - x2) / horizon, 0f);
            sb.draw(stripe, x2 - x1, -stripe.getRegionHeight(), affine);
        }

        sb.setColor(1, 1, 1, 1);
        player.render(sb);

        sb.end();

    }

}
