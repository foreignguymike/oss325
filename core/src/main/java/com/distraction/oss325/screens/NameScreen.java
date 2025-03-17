package com.distraction.oss325.screens;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.B;
import static com.badlogic.gdx.Input.Keys.BACKSPACE;
import static com.badlogic.gdx.Input.Keys.C;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.E;
import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.F;
import static com.badlogic.gdx.Input.Keys.G;
import static com.badlogic.gdx.Input.Keys.H;
import static com.badlogic.gdx.Input.Keys.I;
import static com.badlogic.gdx.Input.Keys.J;
import static com.badlogic.gdx.Input.Keys.K;
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.M;
import static com.badlogic.gdx.Input.Keys.N;
import static com.badlogic.gdx.Input.Keys.NUM_0;
import static com.badlogic.gdx.Input.Keys.NUM_1;
import static com.badlogic.gdx.Input.Keys.NUM_2;
import static com.badlogic.gdx.Input.Keys.NUM_3;
import static com.badlogic.gdx.Input.Keys.NUM_4;
import static com.badlogic.gdx.Input.Keys.NUM_5;
import static com.badlogic.gdx.Input.Keys.NUM_6;
import static com.badlogic.gdx.Input.Keys.NUM_7;
import static com.badlogic.gdx.Input.Keys.NUM_8;
import static com.badlogic.gdx.Input.Keys.NUM_9;
import static com.badlogic.gdx.Input.Keys.O;
import static com.badlogic.gdx.Input.Keys.P;
import static com.badlogic.gdx.Input.Keys.Q;
import static com.badlogic.gdx.Input.Keys.R;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SHIFT_LEFT;
import static com.badlogic.gdx.Input.Keys.SHIFT_RIGHT;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.T;
import static com.badlogic.gdx.Input.Keys.U;
import static com.badlogic.gdx.Input.Keys.V;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.Input.Keys.X;
import static com.badlogic.gdx.Input.Keys.Y;
import static com.badlogic.gdx.Input.Keys.Z;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.Constants;
import com.distraction.oss325.Context;
import com.distraction.oss325.entity.Button;
import com.distraction.oss325.entity.Entity;
import com.distraction.oss325.entity.FontEntity;

import java.util.HashMap;
import java.util.Map;

public class NameScreen extends Screen {

    private static final int MAX_CHARS = 12;

    private static final Map<Integer, String> INPUT_MAP = new HashMap<>() {{
        put(NUM_0, "0");
        put(NUM_1, "1");
        put(NUM_2, "2");
        put(NUM_3, "3");
        put(NUM_4, "4");
        put(NUM_5, "5");
        put(NUM_6, "6");
        put(NUM_7, "7");
        put(NUM_8, "8");
        put(NUM_9, "9");
        put(A, "a");
        put(B, "b");
        put(C, "c");
        put(D, "d");
        put(E, "e");
        put(F, "f");
        put(G, "g");
        put(H, "h");
        put(I, "i");
        put(J, "j");
        put(K, "k");
        put(L, "l");
        put(M, "m");
        put(N, "n");
        put(O, "o");
        put(P, "p");
        put(Q, "q");
        put(R, "r");
        put(S, "s");
        put(T, "t");
        put(U, "u");
        put(V, "v");
        put(W, "w");
        put(X, "x");
        put(Y, "y");
        put(Z, "z");
        put(SPACE, " ");
    }};

    private boolean shift = false;

    private final TextureRegion pixel;

    private final FontEntity enterNameFont;
    private final FontEntity nameFont;
    private final Entity submitButton;

    private float caretTime;

    public NameScreen(Context context) {
        super(context);
        pixel = context.getPixel();

        enterNameFont = new FontEntity(context.getFont(Context.FONT_NAME_VCR20, 2f), "Enter Name", Constants.WIDTH / 2f, Constants.HEIGHT / 2f + 20, FontEntity.Alignment.CENTER);
        nameFont = new FontEntity(context.getFont(Context.FONT_NAME_M5X716, 2f), context.data.name, Constants.WIDTH / 2f, Constants.HEIGHT / 2f - 30, FontEntity.Alignment.CENTER);
        submitButton = new Button(context.getImage("submit"), Constants.WIDTH / 2f, Constants.HEIGHT / 4f);

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.FLASH_OUT, 0.5f, () -> context.sm.replace(new TitleScreen(context)));

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!ignoreInput) {
                    m.set(screenX, screenY, 0);
                    unproject();
                    if (submitButton.contains(m.x, m.y, 4, 4)) {
                        submitName();
                    }
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == SHIFT_LEFT || keycode == SHIFT_RIGHT) {
                    shift = false;
                }
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (ignoreInput) return true;
                if (keycode == SHIFT_LEFT || keycode == SHIFT_RIGHT) {
                    shift = true;
                }
                String letter = INPUT_MAP.get(keycode);
                if (letter != null) {
                    if (context.data.name.length() < MAX_CHARS) {
                        if (shift) context.data.name += letter.toUpperCase();
                        else context.data.name += letter;
                        caretTime = 0;
                    }
                }
                if (keycode == BACKSPACE) {
                    if (!context.data.name.isEmpty()) {
                        context.data.name = context.data.name.substring(0, context.data.name.length() - 1);
                        caretTime = 0;
                    }
                }
                if (keycode == ENTER) {
                    submitName();
                }
                if (context.data.name != null) {
                    nameFont.setText(context.data.name);
                }
                return true;
            }
        });
    }

    @Override
    public void input() {
        // noop
    }

    private boolean validName() {
        return !context.data.name.trim().isEmpty();
    }

    private void submitName() {
        if (validName()) {
            ignoreInput = true;
            context.data.name = context.data.name.trim();
            out.start();
            Gdx.input.setInputProcessor(null);
            context.audio.playSound("select");
        }
    }

    @Override
    public void update(float dt) {
        caretTime += dt;
        in.update(dt);
        out.update(dt);
    }

    @Override
    public void render() {
        sb.begin();
        sb.setProjectionMatrix(uiCam.combined);

        sb.setColor(Constants.GRAY);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(Color.WHITE);
        enterNameFont.render(sb);
        nameFont.render(sb);

        if (caretTime % 1 < 0.5f && context.data.name.length() < MAX_CHARS) {
            sb.draw(pixel, context.data.name.isEmpty() ? Constants.WIDTH / 2f - 3 : nameFont.x + nameFont.w / 2f + 1, nameFont.y - 10, 10, 2);
        }

        submitButton.a = validName() ? 1f : 0.3f;
        submitButton.render(sb);

        in.render(sb);
        out.render(sb);

        sb.end();
    }
}
