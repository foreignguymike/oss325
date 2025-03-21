package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Animation;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

public class Player extends Entity {

    public static final float GRAVITY = 500;
    public static final float MAX_GLIDE_TIME = 3f;

    private final Context context;

    private final TextureRegion pixel;

    private final TextureRegion poko;
    private final Animation pokoShock;
    private final Animation dbz;
    private final Animation bluedbz;

    public boolean launched;
    public boolean stopped;

    private float ceil;
    private float floor;

    private float rad;
    private float drad;

    private float bounceTime;

    public boolean up;
    public float glideTime = MAX_GLIDE_TIME;
    private final TextureRegion wing;
    public boolean renderGlide;

    public Player(Context context) {
        this.context = context;
        pixel = context.getPixel();
        poko = context.getImage("poko");
        w = poko.getRegionWidth();
        h = poko.getRegionHeight();

        pokoShock = new Animation(context.getImage("pokoshock").split(36, 25)[0], 2/60f);
        dbz = new Animation(context.getImage("dbz").split(92, 47)[0], 2/60f);
        bluedbz = new Animation(context.getImage("bluedbz").split(92, 47)[0], 2/60f);

        wing = context.getImage("wing");
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
     * Limits the player's velocity.
     */
    public void limit() {
//        float s = (float) Math.sqrt(dx * dx + dy * dy);
//        float rad = MathUtils.atan2(dy, dx);
        if (dx > 4200) {
            dx = 4200;
//            dx = MathUtils.cos(rad) * 4100;
//            dy = MathUtils.sin(rad) * 4100;
        }
    }

    public void reset() {
        launched = false;
        stopped = false;
        rad = 0;
    }

    private boolean isGliding() {
        return up && glideTime > 0f;
    }

    @Override
    public void update(float dt) {

        bounceTime += dt;

        if (!up) {
            glideTime += dt * 0.25f;
            if (glideTime > MAX_GLIDE_TIME) glideTime = MAX_GLIDE_TIME;
        } else {
            glideTime -= dt;
            if (glideTime < 0) glideTime = 0;
        }

        if (launched && !stopped) {
            // gravity
            dy -= GRAVITY * dt * (isGliding() ? 0.33f : 1f);

            // move
            x += dx * dt;
            y += dy * dt;

            // hit ceil
            if (y + h / 2f > ceil) {
                y = ceil - h / 2f;
                context.audio.playSound("bounce");
            }
            if (dy > 0 && y + h / 2f >= ceil) {
                // slow down and stop dy
                dx *= 0.9f;
                dy = 0;
            }

            // hit floor
            if (y - h / 2f < floor) {
                y = floor + h / 2f;
                if (bounceTime > 0.1f) {
                    float vol = Math.abs(dy) / 100;
                    if (vol > 1f) vol = 1f;
                    context.audio.playSound("bounce", vol);
                    bounceTime = 0;
                }
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
        pokoShock.update(dt);
        dbz.update(dt);
        bluedbz.update(dt);
        drad = MathUtils.atan2(dy, dx);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (launched && isGliding()) Utils.drawCentered(sb, wing, x + 20, y + 10, true);

        if (!launched) Utils.drawRotated(sb, poko, x, y, rad);
        else Utils.drawRotated(sb, pokoShock.getImage(), x, y, rad);

        if (launched && isGliding()) Utils.drawCentered(sb, wing, x - 20, y + 10);

        sb.setColor(1, 1, 1, 0.7f);
        if (dx > 3000) Utils.drawRotated(sb, bluedbz.getImage(), x, y, drad);
        else if (dx > 1500) Utils.drawRotated(sb, dbz.getImage(), x, y, drad);

        if (renderGlide) {
            sb.setColor(Constants.BLACK);
            sb.draw(pixel, x - 16, y + 24, 32, 6);
            sb.setColor(Constants.LIME);
            sb.draw(pixel, x - 15, y + 25, 30 * (glideTime / Player.MAX_GLIDE_TIME), 4);
        }
    }
}
