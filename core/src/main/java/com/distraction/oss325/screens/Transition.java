package com.distraction.oss325.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.SimpleCallback;

public class Transition {

    public enum Type {
        CHECKERED_IN,
        CHECKERED_OUT,
        FLASH_IN,
        FLASH_OUT
    }

    private final Type type;
    private final float duration;
    private SimpleCallback callback;

    private final TextureRegion pixel;

    private boolean start;
    private float time;
    private boolean done;

    public Transition(Context context, Type type, float duration, SimpleCallback callback) {
        this.type = type;
        this.duration = duration;
        this.callback = callback;

        pixel = context.getPixel();
    }

    public void setCallback(SimpleCallback callback) {
        this.callback = callback;
    }

    public void start() {
        start = true;
    }

    public boolean isFinished() {
        return done;
    }

    public void reset() {
        start = false;
        time = 0;
        done = false;
    }

    public void update(float dt) {
        if (!start) return;
        time += dt;
        if (time > duration) {
            if (!done) {
                done = true;
                callback.callback();
                reset();
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!start) return;
        sb.setColor(Color.BLACK);
        if (type == Type.CHECKERED_IN) {
            float squareSize = Constants.WIDTH / 16f;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 16; col++) {
                    float size;
                    float ttime = time - ((9 - row + col) / 40f) * (duration);
                    size = squareSize - squareSize * (ttime / (duration / 3));
                    size = MathUtils.clamp(size, 0, squareSize);
                    sb.draw(pixel, squareSize * 0.5f + squareSize * col - size / 2, squareSize * 0.5f + squareSize * row - size / 2, size, size);
                }
            }
        } else if (type == Type.CHECKERED_OUT) {
            float squareSize = Constants.WIDTH / 16f;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 16; col++) {
                    float size;
                    float ttime = time - ((9 - row + col) / 40f) * (duration);
                    size = squareSize * (ttime / (duration / 3));
                    size = MathUtils.clamp(size, 0, squareSize);
                    sb.draw(pixel, squareSize * 0.5f + squareSize * col - size / 2, squareSize * 0.5f + squareSize * row - size / 2, size, size);
                }
            }
        } else if (type == Type.FLASH_IN) {
            sb.setColor(1, 1, 1, 1f - (time / duration));
            sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        } else if (type == Type.FLASH_OUT) {
            sb.setColor(1, 1, 1, time / duration);
            sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        }
    }

}
