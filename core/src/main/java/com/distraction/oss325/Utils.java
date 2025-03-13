package com.distraction.oss325;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utils {

    public static int roundNearestFloor(float number, int n) {
        return (int) (Math.floor((double) number / n) * n);
    }

    public static void drawCentered(SpriteBatch sb, TextureRegion image, float x, float y) {
        sb.draw(image, x - image.getRegionWidth() / 2f, y - image.getRegionHeight() / 2f);
    }

    public static int ceilTo(int number, int n) {
        return ((number + n - 1) / n) * n;
    }

}
