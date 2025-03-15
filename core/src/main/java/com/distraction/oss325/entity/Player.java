package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

public class Player extends Entity {

    public static final float GRAVITY = 500;

    private final TextureRegion sprite;

    public boolean kicked;
    public boolean stopped;

    private float ceil;
    private float floor;

    private float rad;

    public Player(Context context) {
        sprite = context.getImage("poko");
        w = sprite.getRegionWidth();
        h = sprite.getRegionHeight();
    }

    public void setBounds(float ceil, float floor) {
        this.ceil = ceil;
        this.floor = floor;
        y = floor + h / 2;
    }

    public void kick(float rad, float speed) {
        if (kicked) return;
        kicked = true;
        dx = MathUtils.cos(rad) * speed;
        dy = MathUtils.sin(rad) * speed;

        System.out.println("kick at " + rad + ", " + speed);
    }

    public void reset() {
        kicked = false;
        stopped = false;
        rad = 0;
    }

    @Override
    public void update(float dt) {
        if (kicked && !stopped) {
            // gravity
            dy -= GRAVITY * dt;

            // move
            x += dx * dt;
            y += dy * dt;

            // hit ceil
            if (y + h / 2f > ceil) {
                y = ceil - h / 2f;
            }
            if (dy > 0 && y + h / 2f >= ceil) {
                // slow down and stop dy
                dx *= 0.8f;
                dy = 0;
            }

            // hit floor
            if (y - h / 2f < floor) {
                y = floor + h / 2f;
            }
            if (dy < 0 && y - h / 2f <= floor) {
                // slow down and bounce
                dx *= 0.8f;
                dy = -dy * 0.7f;
            }

            // stop
            if (dx < 1f) {
                stopped = true;
                dx = 0;
                dy = 0;
            }

            rad -= Math.min(4 * dt, dx / 2000);

        } else if (stopped) {
            dy -= GRAVITY * dt;
            y += dy * dt;
            if (y - h / 2f < floor) {
                y = floor + h / 2f;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawRotated(sb, sprite, x, y, rad);
    }
}
