package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.entity.FontEntity;

public class ScoreScreen extends Screen {

    private final TextureRegion bg;
    private float dim;

    private final FontEntity topScoresFont;

    public ScoreScreen(Context context) {
        super(context);
        bg = context.getImage("scoresbg");

        transparent = true;

        ignoreInput = true;
        in = new Transition(
            context,
            Transition.Type.PAN, cam,
            new Vector2(bg.getRegionWidth() + Constants.WIDTH / 2f, 0),
            new Vector2(Constants.WIDTH / 2f, 0),
            0.2f,
            () -> ignoreInput = false
        );
        in.start();
        out = new Transition(
            context,
            Transition.Type.PAN, cam,
            new Vector2(Constants.WIDTH / 2f, 0),
            new Vector2(bg.getRegionWidth() + Constants.WIDTH / 2f, 0),
            0.2f,
            () -> {
                context.sm.pop();
                context.sm.peek().ignoreInput = false;
            }
        );
        cam.position.x = bg.getRegionWidth() + Constants.WIDTH / 2f;
        cam.update();

        topScoresFont = new FontEntity(context.getFont(Context.FONT_NAME_VCR20, 1.5f), "Top Scores", 56, Constants.HEIGHT - 38, FontEntity.Alignment.LEFT);
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ignoreInput = true;
            out.start();
        }
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);
        if (out.started()) {
            dim -= 5 * dt;
            if (dim < 0f) dim = 0f;
        } else if (in.started()) {
            dim += 5 * dt;
            if (dim > 0.8f || in.isFinished()) dim = 0.8f;
        }

    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(0, 0, 0, dim);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(1, 1, 1, 1);
        sb.draw(bg, 0, 0);

        topScoresFont.render(sb);

        sb.end();
    }

}
