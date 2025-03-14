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

    private float floor;

    public Player(Context context) {
        sprite = context.getImage("poko");
        w = sprite.getRegionWidth();
        h = sprite.getRegionHeight();
    }

    public void setFloor(float floor) {
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

    public void bomb() {
        dx *= 1.5f;
        dy = Math.abs(dy);
        dy *= 2f;
    }

    public void reset() {
        kicked = false;
        stopped = false;
    }

    @Override
    public void update(float dt) {
        if (kicked && !stopped) {
            // gravity
            dy -= GRAVITY * dt;

            // move
            x += dx * dt;
            y += dy * dt;

            if (y - h / 2 < floor) {
                y = floor + h / 2;
            }

            // hit floor
            if (dy < 0 && y - h / 2 <= floor) {
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
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, sprite, x, y);
    }
}
