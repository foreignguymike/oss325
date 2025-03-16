package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

import java.util.List;

public class SlowSign extends Interactable {

    private final TextureRegion[] sprites;
    private TextureRegion sprite;

    public SlowSign(Context context) {
        sprites = context.getImage("slow").split(25, 36)[0];
        w = sprites[0].getRegionWidth();
        h = sprites[0].getRegionHeight();

        sprite = sprites[0];
    }

    @Override
    public void interact(Context context, Player player, List<Particle> particles) {
        if (sprite != sprites[1]) {
            sprite = sprites[1];
            if (player.dy > 0) {
                player.dx *= 0.9f;
            } else {
                player.dx *= 0.7f;
            }
            player.dy = Math.abs(player.dy);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, sprite, x, y);
    }

}
