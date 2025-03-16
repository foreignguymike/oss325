package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

public class Player extends Entity {

    public static final float GRAVITY = 500;

    private final TextureRegion sprite;

    public boolean launched;
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

    public void launch(float rad, float speed) {
        if (launched) return;
        launched = true;
        dx = MathUtils.cos(rad) * speed;
        dy = MathUtils.sin(rad) * speed;
    }

    public void boost(float rad, float speed) {
        // get current speed
        float s = (float) Math.sqrt(dx * dx + dy * dy) + speed;
        dx = MathUtils.cos(rad) * s;
        dy = MathUtils.sin(rad) * s;
        limit();
    }

    /**
     * Limits the player's velocity to 3000.
     */
    public void limit() {
        float s = (float) Math.sqrt(dx * dx + dy * dy);
        float rad = MathUtils.atan2(dy, dx);
        if (s > 2000) {
            dx = MathUtils.cos(rad) * 2100;
            dy = MathUtils.sin(rad) * 2100;
        }
    }

    public void reset() {
        launched = false;
        stopped = false;
        rad = 0;
    }

    @Override
    public void update(float dt) {
        if (launched && !stopped) {
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
