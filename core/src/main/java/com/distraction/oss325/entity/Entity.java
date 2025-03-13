package com.distraction.oss325.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

abstract class Entity {

    // position, size, speed
    public float x, y;
    public float w, h;
    public float dx, dy;

    public boolean remove = false;

    public boolean contains(float x1, float y1) {
        return x1 > x - w / 2 && x1 < x + w / 2
            && y1 > y - h / 2 && y1 < y + h / 2;
    }

    public boolean intersects(Entity o) {
        return x - w / 2 < o.x + o.w / 2 && x + w / 2 > o.x - o.w / 2
            && y - h / 2 < o.y + o.h / 2 && y + h / 2 > o.y - o.h / 2;
    }

    public void update(float dt) {}
    public void render(SpriteBatch sb) {}

}
