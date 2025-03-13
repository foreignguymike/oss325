package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

public class Bomb extends Entity {

    private final TextureRegion sprite;

    public Bomb(Context context) {
        sprite = context.getImage("bomb");
        w = sprite.getRegionWidth();
        h = sprite.getRegionHeight();
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, sprite, x, y);
    }
}
