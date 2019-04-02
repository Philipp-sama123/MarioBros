package com.philipp.stampfer.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Scenes.Hud;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 4;
    private final int COIN_SCORE = 100;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);

        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("COIN", "Head hit");
        if (getCell().getTile().getId() == BLANK_COIN)
            MarioBros.assetManager.get("audio/sounds/bump.wav", Sound.class).play();
        else
            MarioBros.assetManager.get("audio/sounds/coin.wav", Sound.class).play();
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(COIN_SCORE);
    }
}
