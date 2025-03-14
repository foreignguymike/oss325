package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Context;

public class LaunchAngle extends Entity {

    private static final float MIN_RAD = 0.01f;
    private static final float MAX_RAD = MathUtils.PI / 2f - 0.01f;

    private final TextureRegion bg;
    private final TextureRegion arrow;

    public float rad;

    private float radStep = 1;

    public LaunchAngle(Context context) {
        bg = context.getImage("launchanglebg");
        arrow = context.getImage("launchanglearrow");
    }

    @Override
    public void update(float dt) {
        rad += radStep * dt;
        if (rad > MAX_RAD) {
            rad = MAX_RAD;
            radStep = -radStep;
        }
        if (rad < MIN_RAD) {
            rad = MIN_RAD;
            radStep = -radStep;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(bg, x, y);
        sb.draw(arrow, x, y - 1f, 3f, 5f, arrow.getRegionWidth(), arrow.getRegionHeight(), 1f, 1f, MathUtils.radDeg * rad);
    }
}
