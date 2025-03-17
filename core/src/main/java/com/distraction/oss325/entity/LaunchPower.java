package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

public class LaunchPower extends Entity {

    private static final int MAX_POWER = 1000;

    private final TextureRegion bar;
    private final TextureRegion downArrow;

    private float timer;
    private float power;

    public LaunchPower(Context context) {
        bar = context.getImage("launchbarbg");
        downArrow = context.getImage("launchbardownarrow");

        w = bar.getRegionWidth();
        h = bar.getRegionHeight();
    }

    public void reset() {
        timer = 0;
    }

    public float getPower() {
        return power * MAX_POWER;
    }

    @Override
    public void update(float dt) {
        timer += dt;
        power = 1 - Math.abs(MathUtils.cos(3 * timer));
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, bar, x, y);
        Utils.drawCentered(sb, downArrow, x - w / 2 + 50 * power, y + 5);
    }
}
