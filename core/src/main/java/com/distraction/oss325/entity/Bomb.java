package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;

import java.util.List;

public class Bomb extends Interactable {

    private final TextureRegion sprite;

    public Bomb(Context context) {
        sprite = context.getImage("bomb");
        w = sprite.getRegionWidth();
        h = sprite.getRegionHeight();
    }

    @Override
    public void interact(Context context, Player player, List<Particle> particles) {
        remove = true;
        player.dx *= 1.5f;
        player.dy = Math.abs(player.dy);
        player.dy *= 2f;
        if (player.dy < 150f) player.dy = 150f;
        player.limit();

        for (int j = 0; j < 5; j++) {
            float ex = MathUtils.random(x - 20, x + 20);
            float ey = MathUtils.random(y - 20, y + 20);
            particles.add(
                new Particle(
                    context.getImage("explosion").split(34, 36)[0],
                    2 / 60f,
                    ex,
                    ey
                )
            );
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawCentered(sb, sprite, x, y);
    }
}
