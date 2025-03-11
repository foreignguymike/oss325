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

public class PlayScreen extends Screen {

    private final TextureRegion pixel;
    private final TextureRegion stripe;

    private final Affine2 affine = new Affine2();
    private final BitmapFont font = new BitmapFont();

    public PlayScreen(Context context) {
        super(context);
        pixel = context.getPixel();
        stripe = context.getImage("stripe");
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= 500 * dt;
            cam.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += 500 * dt;
            cam.update();
        }

    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.WHITE);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(1, 1, 1, 1);
        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.TEAL);
        sb.draw(pixel, 0, 0, Constants.WIDTH, 50);

        font.setColor(Color.BLACK);
        sb.setProjectionMatrix(debugCamera.combined);
        font.draw(sb, "camera pos: " + (int) cam.position.x + ", " + (int) cam.position.y, 10, Constants.SHEIGHT - 10);

        sb.setProjectionMatrix(cam.combined);

        int stripeWidth = 50;
        int left = Utils.roundNearestFloor(cam.position.x - Constants.WIDTH / 2f, 100);
        int count = Constants.WIDTH / stripeWidth + 1;

        for (int i = 0; i < count; i++) {
            float x1 = left + i * stripeWidth * 2;
            float x2 = x1 + stripeWidth;
            affine.idt();
            affine.translate(x1, 50);
            affine.shear((cam.position.x - x1) / 50f, 0f);
            sb.setColor(Constants.PURPLE);
            sb.draw(stripe, x2 - x1, -stripe.getRegionHeight(), affine);
            affine.idt();
            affine.translate(x1, 50);
            affine.shear((cam.position.x - x2) / 50f, 0f);
            sb.draw(stripe, x2 - x1, -stripe.getRegionHeight(), affine);
        }

        sb.end();

    }

}
