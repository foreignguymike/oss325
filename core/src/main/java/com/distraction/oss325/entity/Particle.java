package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Animation;
import com.distraction.oss325.Utils;

public class Particle extends Entity {

    private Animation animation;

    public Particle(TextureRegion[] images, float interval, float x, float y) {
        animation = new Animation(images, interval);
        this.x = x;
        this.y = y;
        w = images[0].getRegionWidth();
        h = images[0].getRegionHeight();
    }

    @Override
    public void update(float dt) {
        animation.update(dt);
        if (animation.getPlayCount() > 1) remove = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, animation.getImage(), x, y);
    }
}
