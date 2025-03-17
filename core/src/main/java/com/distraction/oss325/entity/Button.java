package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Utils;

public class Button extends Entity {

    private final TextureRegion image;

    public Button(TextureRegion image, float x, float y) {
        this.image = image;
        this.x = x;
        this.y = y;

        w = image.getRegionWidth();
        h = image.getRegionHeight();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, a);
        Utils.drawCentered(sb, image, x, y);
    }
}
