package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.entity.FontEntity;

import de.golfgl.gdxgamesvcs.leaderboard.ILeaderBoardEntry;

public class ScoreScreen extends Screen {

    private final TextureRegion bg;
    private float dim;

    private final FontEntity topScoresFont;
    private final FontEntity[][] scoreFonts;

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


        topScoresFont = new FontEntity(context.getFont(Context.FONT_NAME_VCR20, 1.5f), "Top Scores", 56, Constants.HEIGHT - 38);

        BitmapFont font = context.getFont(Context.FONT_NAME_M5X716, 2f);
        scoreFonts = new FontEntity[Context.MAX_SCORES][3];
        for (int i = 0; i < scoreFonts.length; i++) {
            scoreFonts[i][0] = new FontEntity(font, (i + 1) + "", 10 + i * 12.5f, Constants.HEIGHT - 100 - i * 25);
            scoreFonts[i][1] = new FontEntity(font, "", 45 + i * 12.5f, scoreFonts[i][0].y);
            scoreFonts[i][2] = new FontEntity(font, "", 250 + i * 12.5f, scoreFonts[i][0].y);
        }

        updateLeaderboards();
    }

    private void updateLeaderboards() {
        for (int i = 0; i < Context.MAX_SCORES; i++) {
            if (i < context.entries.size()) {
                ILeaderBoardEntry entry = context.entries.get(i);
                scoreFonts[i][1].setText(entry.getUserDisplayName());
                scoreFonts[i][2].setText(entry.getFormattedValue() + "m");
            } else {
                scoreFonts[i][1].setText("-");
                scoreFonts[i][2].setText("-");
            }
        }
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
        for (FontEntity[] scoreFont : scoreFonts) {
            scoreFont[0].render(sb);
            scoreFont[1].render(sb);
            scoreFont[2].render(sb);
        }

        sb.end();
    }

}
