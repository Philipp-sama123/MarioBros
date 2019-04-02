package com.philipp.stampfer.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Scenes.Hud;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;


public class Brick extends InteractiveTileObject {
    public static final int BRICK_SCORE = 200;

    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("BRICK", "Head hit");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(BRICK_SCORE);// TODO: 31.03.19 pass HUD to game objects -- and don't make it static
        MarioBros.assetManager.get("audio/sounds/breakblock.wav", Sound.class).play();

    }

}
