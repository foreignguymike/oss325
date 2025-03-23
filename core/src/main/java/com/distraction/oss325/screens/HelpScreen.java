package com.distraction.oss325.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.Utils;
import com.distraction.oss325.entity.Button;

public class HelpScreen extends Screen {

    private final TextureRegion bg;
    private float dim;
    private final TextureRegion[] pages;

    private final Button backButton;

    private int page;

    private final Button left;
    private final Button right;
    private boolean showLeft;
    private boolean showRight;

    public HelpScreen(Context context) {
        super(context);
        bg = context.getImage("helpbg");
        pages = new TextureRegion[]{
            context.getImage("page1"),
            context.getImage("page2"),
            context.getImage("page3")
        };
        left = new Button(context.getImage("helpleft"), 35, Constants.HEIGHT / 2f);
        right = new Button(context.getImage("helpright"), Constants.WIDTH - 35, Constants.HEIGHT / 2f);

        transparent = true;
        ignoreInput = true;

        Vector2 start = new Vector2(Constants.WIDTH / 2f, -Constants.HEIGHT / 2f);
        in = new Transition(
            context,
            Transition.Type.PAN, cam,
            start,
            new Vector2(Constants.WIDTH / 2f, Constants.HEIGHT / 2f),
            0.2f,
            () -> ignoreInput = false
        );
        in.start();
        out = new Transition(
            context,
            Transition.Type.PAN, cam,
            new Vector2(cam.position.x, cam.position.y),
            new Vector2(cam.position.x, cam.position.y - Constants.HEIGHT),
            0.2f,
            () -> {
                context.sm.pop();
                context.sm.peek().ignoreInput = false;
            }
        );
        cam.position.x = start.x;
        cam.position.y = start.y;
        cam.update();

        showLeft = page > 0;
        showRight = page < pages.length - 1;

        backButton = new Button(context.getImage("back"), Constants.WIDTH - 26, Constants.HEIGHT - 26);
    }

    private void setPage(int newPage) {
        page = newPage;
        showLeft = page > 0;
        showRight = page < pages.length - 1;
        context.audio.playSound("pluck", 0.4f);
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        if (Gdx.input.justTouched() && in.isFinished() && !out.started()) {
            unproject();
            if (backButton.contains(m.x, m.y, 2, 2)) {
                ignoreInput = true;
                out = new Transition(
                    context,
                    Transition.Type.PAN, cam,
                    new Vector2(cam.position.x, cam.position.y),
                    new Vector2(cam.position.x, cam.position.y - Constants.HEIGHT),
                    0.2f,
                    () -> {
                        context.sm.pop();
                        context.sm.peek().ignoreInput = false;
                    }
                );
                out.start();
                context.audio.playSound("click");
            } else if (showLeft && left.contains(m.x, m.y, 2, 2)) {
                if (page > 0) setPage(page - 1);
            } else if (showRight && right.contains(m.x, m.y, 2, 2)) {
                if (page < pages.length - 1) setPage(page + 1);
            }
        }
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        if (out != null) out.update(dt);
        if (out != null && out.started()) {
            dim -= 5 * dt;
            if (dim < 0f) dim = 0f;
        } else if (in.started()) {
            dim += 5 * dt;
            if (dim > 0.8f || in.isFinished()) dim = 0.8f;
        }

        // move cam
        float targetx = Constants.WIDTH / 2f + page * Constants.WIDTH;
        float camSpeed = 5000;
        if (cam.position.x < targetx) {
            cam.position.x += camSpeed * dt;
            if (cam.position.x > targetx) cam.position.x = targetx;
        }
        if (cam.position.x > targetx) {
            cam.position.x -= camSpeed * dt;
            if (cam.position.x < targetx) cam.position.x = targetx;
        }
        cam.update();
    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(0, 0, 0, dim);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        // pages
        sb.setProjectionMatrix(cam.combined);
        sb.setColor(1, 1, 1, 1);
        for (int i = 0; i < pages.length; i++) {
            Utils.drawCentered(sb, bg, Constants.WIDTH / 2f + Constants.WIDTH * i, Constants.HEIGHT / 2f);
            Utils.drawCentered(sb, pages[i], Constants.WIDTH / 2f + Constants.WIDTH * i, Constants.HEIGHT / 2f);
        }

        if (in.isFinished() && !out.started()) {
            sb.setProjectionMatrix(uiCam.combined);
            if (showLeft) left.render(sb);
            if (showRight) right.render(sb);
            backButton.render(sb);
        }

        sb.end();
    }
}
