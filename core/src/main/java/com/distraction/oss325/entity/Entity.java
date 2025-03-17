package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {

    // position, size, speed
    public float x, y;
    public float w, h;
    public float dx, dy;

    public float a = 1;
    public boolean visible = true;
    public boolean remove = false;

    /**
     * Contains with padding.
     */
    public boolean contains(float x, float y, float px, float py) {
        return x > this.x - w / 2 - px
            && x < this.x + w / 2 + px
            && y > this.y - h / 2 - py
            && y < this.y + h / 2 + py;
    }

    public boolean intersects(Entity o) {
        return x - w / 2 < o.x + o.w / 2 && x + w / 2 > o.x - o.w / 2
            && y - h / 2 < o.y + o.h / 2 && y + h / 2 > o.y - o.h / 2;
    }

    public void update(float dt) {}
    public void render(SpriteBatch sb) {}

}
