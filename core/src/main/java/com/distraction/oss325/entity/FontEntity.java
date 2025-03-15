package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import java.util.Objects;

public class FontEntity extends Entity {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    private final GlyphLayout glyphLayout;
    private final BitmapFont font;

    public Alignment alignment = Alignment.LEFT;

    private String currentText = "";
    private Color color = Color.WHITE;

    public FontEntity(BitmapFont font, String text, float x, float y, Alignment alignment) {
        this(font, text, x, y);
        this.alignment = alignment;
    }

    public FontEntity(BitmapFont font, String text, float x, float y) {
        this.font = font;
        glyphLayout = new GlyphLayout();
        setText(text);
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return currentText;
    }

    public void setText(String text) {
        if (!Objects.equals(currentText, text)) {
            currentText = text;
            glyphLayout.setText(font, currentText, 0, currentText.length(), color, 0, Align.left, false, null);
            w = glyphLayout.width;
            h = glyphLayout.height;
        }
    }

    public void setColor(Color color) {
        if (color != this.color) {
            this.color = color;
            glyphLayout.setText(font, currentText, 0, currentText.length(), color, 0, Align.left, false, null);
        }
    }

    @Override
    public boolean contains(float x, float y, float px, float py) {
        if (alignment == Alignment.CENTER) return contains(x, y, px, py);
        else if (alignment == Alignment.LEFT) return contains(x - w / 2, y, px, py);
        else return contains(x + w / 2 + 1, y, px, py);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (currentText.isEmpty()) return;
        if (alignment == Alignment.CENTER) {
            font.draw(sb, glyphLayout, x - glyphLayout.width / 2f, y + glyphLayout.height / 2f);
        } else if (alignment == Alignment.LEFT) {
            font.draw(sb, glyphLayout, x, y + glyphLayout.height / 2f);
        } else {
            font.draw(sb, glyphLayout, x - w + 1, y + glyphLayout.height / 2f);
        }
    }

}
