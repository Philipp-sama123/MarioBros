package com.philipp.stampfer.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;

public class MarioBros extends Game {
    public static final int VISUAL_WIDTH = 400;
    public static final int VISUAL_HEIGHT = 200;
    public static final float PIXELS_PER_METER = 100f;
    public static final int MAXIMUM_SPEED = 2;
    public static final int ENEMY_SPAWN_DISTANCE = 224;

    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;

    public SpriteBatch batch;

    // TODO: 01.04.19 pass the AssetManager to the classes -- now static context
    public static AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.load("audio/music/mario_music.ogg", Music.class);
        assetManager.load("audio/sounds/coin.wav", Sound.class);
        assetManager.load("audio/sounds/bump.wav", Sound.class);
        assetManager.load("audio/sounds/breakblock.wav", Sound.class);
        assetManager.finishLoading();

        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        batch.dispose();
    }
}
