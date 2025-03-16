package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;

import java.util.List;

public class Mini extends Entity {

    private final OrthographicCamera cam;
    private final List<Interactable> interactables;

    private final TextureRegion pixel;
    private final TextureRegion bomb;
    private final TextureRegion slow;
    private final TextureRegion stop;

    private final float ratio;

    public Mini(
        Context context,
        OrthographicCamera cam,
        List<Interactable> interactables,
        float x,
        float y
    ) {
        this.cam = cam;
        this.interactables = interactables;
        this.x = x;
        this.y = y;

        pixel = context.getPixel();
        bomb = context.getImage("bombmini");
        slow = context.getImage("slowmini");
        stop = context.getImage("stopmini");

        w = bomb.getRegionWidth();
        h = bomb.getRegionHeight();

        ratio = 0.805f * Constants.WIDTH / w;
    }

    @Override
    public void render(SpriteBatch sb) {
        for (int i = 0; i < interactables.size(); i++) {
            Interactable e = interactables.get(i);
            TextureRegion sprite;
            if (e instanceof Bomb) sprite = bomb;
            else if (e instanceof SlowSign) sprite = slow;
            else sprite = stop;
            sb.draw(sprite, x + (e.x - cam.position.x) / ratio - w / 2f, y);
        }

        sb.setColor(Constants.WHITE);
        sb.draw(pixel, x, y - 5, 1, h + 10);
        sb.draw(pixel, x - w * 5 + 10, y - 1, 1, h + 2);
        sb.draw(pixel, x + w * 5 - 11, y - 1, 1, h + 2);
        sb.draw(pixel, x - w * 5 + 10, y + h, w * 10 - 20, 1);
        sb.draw(pixel, x - w * 5 + 10, y - 1, w * 10 - 20, 1);

        sb.setColor(Constants.BLUE);
        sb.draw(pixel, x - w * 5 + 10, y, -w * 2, h);
        sb.draw(pixel, x + w * 5 - 10, y, w * 2, h);
    }
}
