package com.distraction.oss325;

public class PlayerData {
    public String name = "";
    public int score;
    public boolean submitted;

    public void reset() {
        score = 0;
        submitted = false;
    }
}
