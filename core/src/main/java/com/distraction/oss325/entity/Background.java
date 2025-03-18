package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;

public class Background extends Entity {

    private final TextureRegion image;
    private final Camera cam;
    private final float parallax;
    private final int count;

    public float xOffset;

    public Background(TextureRegion image, Camera cam) {
        this(image, cam, 1f, 0f);
    }

    public Background(TextureRegion image, Camera cam, float parallax, float y) {
        this.image = image;
        this.cam = cam;
        this.parallax = parallax;
        this.y = y;
        w = image.getRegionWidth();
        h = image.getRegionHeight();
        count = (int) (Constants.WIDTH / w) + 3;
    }

    @Override
    public void update(float dt) {
        x = (-cam.position.x / parallax) % w + xOffset;
    }

    @Override
    public void render(SpriteBatch sb) {
        for (int i = 0; i < count; i++) {
            sb.draw(image, x + i * w, y);
        }
    }
}
