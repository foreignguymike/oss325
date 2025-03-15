package com.distraction.oss325;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

    private final TextureRegion[] images;
    private final float interval;

    private float timer;
    private int frame;
    private int playCount;

    public Animation(TextureRegion[] images, float interval) {
        this.images = images;
        this.interval = interval;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void update(float dt) {
        if (interval <= 0) return;
        timer += dt;
        if (timer >= interval) {
            timer -= interval;
            frame++;
            if (frame >= images.length) {
                frame = 0;
                playCount++;
            }
        }
    }

    public TextureRegion getImage() {
        return images[frame];
    }

}
