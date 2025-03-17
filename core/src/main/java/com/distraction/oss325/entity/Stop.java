package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

import java.util.List;

public class Stop extends Interactable {

    private final TextureRegion sprite;

    private boolean hit;

    public Stop(Context context) {
        sprite = context.getImage("stop");
        w = sprite.getRegionWidth() / 2f;
        h = sprite.getRegionHeight();
    }

    @Override
    public void interact(Context context, Player player, List<Particle> particles) {
        if (!hit) {
            hit = true;
            player.dx = 0;
            context.audio.playSound("stop", 0.5f);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, sprite, x, y);
    }

}
