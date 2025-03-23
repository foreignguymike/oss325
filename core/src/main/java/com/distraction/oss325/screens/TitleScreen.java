package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;
import com.distraction.oss325.entity.Button;
import com.distraction.oss325.entity.FontEntity;

public class TitleScreen extends Screen {

    private final TextureRegion title;

    private final FontEntity playerFont;
    private final FontEntity versionFont;

    private final FontEntity errorFont;
    private float errorFontTime;

    private final Button playButton;
    private final Button scoresButton;
    private final Button helpButton;

    public TitleScreen(Context context) {
        super(context);

        title = context.getImage("title");

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.CHECKERED_OUT, 0.5f, () -> context.sm.replace(new PlayScreen(context)));

        playerFont = new FontEntity(context.getFont(Context.FONT_NAME_M5X716, 2f), "Player: " + context.data.name, 10, Constants.HEIGHT - 20);
        versionFont = new FontEntity(context.getFont(Context.FONT_NAME_M5X716), Constants.VERSION, Constants.WIDTH - 5, 5, FontEntity.Alignment.RIGHT);

        errorFont = new FontEntity(context.getFont(Context.FONT_NAME_M5X716), "", Constants.WIDTH / 2f, 5, FontEntity.Alignment.CENTER);
        if (!context.leaderboardsInitialized && !context.leaderboardsRequesting) {
            errorFont.setText("Fetching leaderboards...");
            errorFontTime = 30f;
            context.fetchLeaderboard((success) -> {
                errorFont.setText(success ? "Leaderboards fetched!" : "Error fetching leaderboards...");
                errorFontTime = 3f;
            });
        }

        playButton = new Button(context.getImage("play"), Constants.WIDTH / 2f - 80, 60);
        scoresButton = new Button(context.getImage("scores"), Constants.WIDTH / 2f + 80, 60);
        helpButton = new Button(context.getImage("help"), 26, 26);
    }

    @Override
    public void input() {
        if (ignoreInput) return;
        if (Gdx.input.justTouched()) {
            unproject();
            if (playerFont.contains(m.x, m.y, 5, 3)) {
                ignoreInput = true;
                out = new Transition(context, Transition.Type.FLASH_OUT, 0.5f, () -> context.sm.replace(new NameScreen(context)));
                out.start();
                context.audio.playSound("click");
            }
            if (playButton.contains(m.x, m.y, 2, 2)) {
                ignoreInput = true;
                out.setCallback(() -> context.sm.replace(new PlayScreen(context)));
                out.start();
                context.audio.playSound("click");
            }
            if (scoresButton.contains(m.x, m.y, 2, 2)) {
                ignoreInput = true;
                context.sm.push(new ScoreScreen(context));
                context.audio.playSound("click");
            }
            if (helpButton.contains(m.x, m.y, 2, 2)) {
                ignoreInput = true;
                context.sm.push(new HelpScreen(context));
                context.audio.playSound("click");
            }
        }
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);
        errorFontTime -= dt;
    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(Constants.GRAY);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        sb.setColor(Constants.WHITE);
        sb.draw(pixel, 0, 130, Constants.WIDTH, 100);

        sb.setColor(1, 1, 1, 1);
        Utils.drawCentered(sb, title, Constants.WIDTH / 2f, Constants.HEIGHT / 2f + 20);

        playerFont.render(sb);
        versionFont.render(sb);
        if (errorFontTime > 0) errorFont.render(sb);

        playButton.render(sb);
        scoresButton.render(sb);
        helpButton.render(sb);

        in.render(sb);
        out.render(sb);

        sb.end();

    }
}
