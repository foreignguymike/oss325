package com.distraction.oss325;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.oss325.audio.AudioHandler;
import com.distraction.oss325.gj.GameJoltClient;
import com.distraction.oss325.screens.NameScreen;
import com.distraction.oss325.screens.ScreenManager;

import java.util.ArrayList;
import java.util.List;

import de.golfgl.gdxgamesvcs.leaderboard.ILeaderBoardEntry;

public class Context {

    public static final String FONT_NAME_VCR20 = "fonts/vcr20.fnt";
    public static final String FONT_NAME_M5X716 = "fonts/m5x716.fnt";

    private static final String ATLAS = "oss325.atlas";

    public static final int MAX_SCORES = 10;
    private static final int MINIMUM_SCORE = 1;

    public AssetManager assets;
    public AudioHandler audio;

    public ScreenManager sm;
    public SpriteBatch sb;

    public GameJoltClient client;
    public boolean leaderboardsRequesting;
    public boolean leaderboardsInitialized;
    public List<ILeaderBoardEntry> entries = new ArrayList<>();

    public PlayerData data = new PlayerData();

    public Context() {
        assets = new AssetManager();
        assets.load(ATLAS, TextureAtlas.class);
        assets.load(FONT_NAME_M5X716, BitmapFont.class);
        assets.load(FONT_NAME_VCR20, BitmapFont.class);
        assets.finishLoading();

        sb = new SpriteBatch();

        audio = new AudioHandler();

        sm = new ScreenManager(new NameScreen(this));
    }

    public TextureRegion getImage(String key) {
        TextureRegion region = assets.get(ATLAS, TextureAtlas.class).findRegion(key);
        if (region == null) throw new IllegalArgumentException("image " + key + " not found");
        return region;
    }

    public TextureRegion getPixel() {
        return getImage("pixel");
    }

    public BitmapFont getFont(String name) {
        return getFont(name, 1f);
    }

    public BitmapFont getFont(String name, float scale) {
        BitmapFont originalFont = assets.get(name, BitmapFont.class);
        BitmapFont scaledFont = new BitmapFont(originalFont.getData().getFontFile(), originalFont.getRegion(), false);
        scaledFont.getData().setScale(scale);
        return scaledFont;
    }

    public void fetchLeaderboard(SuccessCallback callback) {
        if (Constants.LEADERBOARD_ID == 0) {
            callback.callback(false);
            return;
        }
        entries.clear();
        if (leaderboardsRequesting) return;
        leaderboardsRequesting = true;
        client.fetchLeaderboardEntries("", MAX_SCORES, false, leaderBoard -> {
            if (leaderBoard != null) {
                leaderboardsRequesting = false;
                leaderboardsInitialized = true;
                entries.clear();
                for (int i = 0; i < leaderBoard.size; i++) {
                    entries.add(leaderBoard.get(i));
                }
            }
            callback.callback(leaderBoard != null);
        });
    }

    public void submitScore(String name, int score, Net.HttpResponseListener listener) {
        client.setGuestName(name);
        client.submitToLeaderboard("", score, null, 10000, listener);
    }

    public boolean isHighscore(String name, int score) {
        if (!leaderboardsInitialized) return false;
        if (score < MINIMUM_SCORE) return false;
        ILeaderBoardEntry existingEntry = null;
        for (ILeaderBoardEntry entry : entries) {
            if (entry.getUserDisplayName().equalsIgnoreCase(name)) {
                existingEntry = entry;
                break;
            }
        }
        boolean top = entries.size() < MAX_SCORES || score > Integer.parseInt(Utils.getLast(entries).getFormattedValue());
        if (existingEntry != null) {
            return score > Integer.parseInt(existingEntry.getFormattedValue()) && top;
        } else {
            return top;
        }
    }

    public void dispose() {
        sb.dispose();
    }

}
