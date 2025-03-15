package com.distraction.oss325;

import com.badlogic.gdx.graphics.Color;

@SuppressWarnings("all")
public class Constants {

    public static final String TITLE = "something";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;
    public static final int SCALE = 2;
    public static final int SWIDTH = WIDTH * SCALE;
    public static final int SHEIGHT = HEIGHT * SCALE;

    // Island Joy 16
    // https://lospec.com/palette-list/island-joy-16
    public static final Color[] COLORS = new Color[] {
        Color.valueOf("ffffff"),
        Color.valueOf("6df7c1"),
        Color.valueOf("11adc1"),
        Color.valueOf("606c81"),
        Color.valueOf("393457"),
        Color.valueOf("1e8875"),
        Color.valueOf("5bb361"),
        Color.valueOf("a1e55a"),
        Color.valueOf("f7e476"),
        Color.valueOf("f99252"),
        Color.valueOf("cb4d68"),
        Color.valueOf("6a3771"),
        Color.valueOf("c92464"),
        Color.valueOf("f48cb6"),
        Color.valueOf("f7b69e"),
        Color.valueOf("9b9c82")
    };
    public static final Color WHITE = COLORS[0];
    public static final Color BLUE = COLORS[2];
    public static final Color BLACK = COLORS[4];
    public static final Color DARK_GREEN = COLORS[5];
    public static final Color GREEN = COLORS[6];
    public static final Color PEACH = COLORS[14];
    public static final Color OLIVE = COLORS[15];
}
