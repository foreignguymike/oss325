package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;

public class DistanceBanner extends Entity {

    private final TextureRegion pixel;

    private final FontEntity fontEntity;

    private float time;

    public DistanceBanner(Context context, FontEntity fontEntity) {
        pixel = context.getPixel();
        this.fontEntity = fontEntity;
    }

    public void start() {
        time = 0;
    }

    public void setText(String text) {
        fontEntity.setText(text);
    }

    @Override
    public void update(float dt) {
        time += dt;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (time < 1f) {
            sb.setColor(0, 0, 0, 0.5f);
            sb.draw(pixel, 0, fontEntity.y - 11, Constants.WIDTH, 31);
            sb.setColor(1, 1, 1, 1);
            fontEntity.render(sb);
        }
    }
}
