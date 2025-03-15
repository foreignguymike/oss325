package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

import java.util.List;

public class Stop extends Interactable {

    private final TextureRegion sprite;

    public Stop(Context context) {
        sprite = context.getImage("stop");
        w = sprite.getRegionWidth() / 2f;
        h = sprite.getRegionHeight();
    }

    @Override
    public void interact(Context context, Player player, List<Particle> particles) {
        player.dx = 0;
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, sprite, x, y);
    }

}
