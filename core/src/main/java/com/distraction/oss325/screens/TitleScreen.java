package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;
import com.distraction.oss325.entity.FontEntity;

public class TitleScreen extends Screen {

    private final TextureRegion title;

    private final FontEntity playerFont;
    private final FontEntity versionFont;

    public TitleScreen(Context context) {
        super(context);

        title = context.getImage("title");

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.CHECKERED_OUT, 0.5f, () -> context.sm.replace(new PlayScreen(context)));

        playerFont = new FontEntity(context.getFont(Context.FONT_NAME_M5X716, 2f), "Player: " + context.data.name, 10, Constants.HEIGHT - 20);
        versionFont = new FontEntity(context.getFont(Context.FONT_NAME_M5X716), Constants.VERSION, Constants.WIDTH - 5, 5, FontEntity.Alignment.RIGHT);
    }

    @Override
    public void input() {
        if (ignoreInput) return;
        if (Gdx.input.isTouched()) {
            unproject();
            if (playerFont.contains(m.x, m.y, 5, 3)) {
                ignoreInput = true;
                out = new Transition(context, Transition.Type.FLASH_OUT, 0.5f, () -> context.sm.replace(new NameScreen(context)));
                out.start();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            ignoreInput = true;
            out.setCallback(() -> context.sm.replace(new PlayScreen(context)));
            out.start();
        }
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);
    }

    @Override
    public void render() {

        sb.begin();

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(Constants.BLACK);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        sb.setColor(Constants.WHITE);
        sb.draw(pixel, 0, 130, Constants.WIDTH, 100);

        Utils.drawCentered(sb, title, Constants.WIDTH / 2f, Constants.HEIGHT / 2f);

        playerFont.render(sb);
        versionFont.render(sb);

        in.render(sb);
        out.render(sb);

        sb.end();

    }
}
