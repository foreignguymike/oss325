package com.distraction.oss325;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.List;
import java.util.NoSuchElementException;

public class Utils {

    public static void drawCentered(SpriteBatch sb, TextureRegion image, float x, float y) {
        sb.draw(image, x - image.getRegionWidth() / 2f, y - image.getRegionHeight() / 2f);
    }

    public static void drawCentered(SpriteBatch sb, TextureRegion image, float x, float y, boolean flipped) {
        if (flipped) {
            sb.draw(image, x + image.getRegionWidth() / 2f, y - image.getRegionHeight() / 2f, -image.getRegionWidth(), image.getRegionHeight());
        } else {
            sb.draw(image, x - image.getRegionWidth() / 2f, y - image.getRegionHeight() / 2f);
        }
    }

    public static void drawRotated(SpriteBatch sb,  TextureRegion image, float x, float y, float rad) {
        sb.draw(
            image,
            x - image.getRegionWidth() / 2f,
            y - image.getRegionHeight() / 2f,
            image.getRegionWidth() / 2f,
            image.getRegionHeight() / 2f,
            image.getRegionWidth(),
            image.getRegionHeight(),
            1f,
            1f,
            MathUtils.radDeg * rad
        );
    }

    public static int ceilTo(int number, int n) {
        return ((number + n - 1) / n) * n;
    }

    public static int floorTo(int number, int n) {
        return ((number - 1) / n) * n;
    }

    public static <T> T getLast(List<T> list) {
        if (list == null || list.isEmpty()) throw new NoSuchElementException("List is empty");
        return list.get(list.size() - 1);
    }

}
