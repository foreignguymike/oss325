package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;

public class Background extends Entity {

    private final TextureRegion image;
    private final Camera cam;
    private final int count;

    public Background(TextureRegion image, Camera cam) {
        this.image = image;
        this.cam = cam;
        w = image.getRegionWidth();
        h = image.getRegionHeight();
        count = (int) (Constants.WIDTH / w) + 2;
    }

    @Override
    public void update(float dt) {
        x = -cam.position.x % w;
    }

    @Override
    public void render(SpriteBatch sb) {
        for (int i = 0; i < count; i++) {
            sb.draw(image, x + i * w, y);
        }
    }
}
