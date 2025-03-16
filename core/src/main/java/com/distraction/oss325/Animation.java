package com.distraction.oss325;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

    private final TextureRegion[] images;
    private final float interval;

    private float timer;
    private int frame;
    private int finishCount = 0;

    public Animation(TextureRegion[] images, float interval) {
        this.images = images;
        this.interval = interval;
    }

    public int getFinishCount() {
        return finishCount;
    }

    public void update(float dt) {
        if (interval <= 0) return;
        timer += dt;
        if (timer >= interval) {
            timer -= interval;
            frame++;
            if (frame == images.length - 1) {
                finishCount++;
            }
            if (frame >= images.length) {
                frame = 0;
            }
        }
    }

    public TextureRegion getImage() {
        return images[frame];
    }

}
