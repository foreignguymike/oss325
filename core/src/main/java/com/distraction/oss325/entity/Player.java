package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

public class Player extends Entity {

    public static final float GRAVITY = 100;

    private final TextureRegion sprite;

    private boolean kicked;
    private boolean stopped;

    private float floor;

    public Player(Context context) {
        sprite = context.getImage("slime");
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
    }

    @Override
    public void update(float dt) {
        if (kicked && !stopped) {
            // gravity
            dy -= GRAVITY * dt;

            // move
            x += dx * dt;
            y += dy * dt;

            // hit floor
            if (dy < 0 && y <= floor) {
                // slow down and bounce
                dx *= 0.9f;
                dy = -dy * 0.7f;
            }

            // stop
            if (dx < 0.0001f) {
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
