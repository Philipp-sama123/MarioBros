package com.philipp.stampfer.mygdx.game.Sprites.TileObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;

public abstract class InteractiveTileObject {
    public static final int TILE_SIZE = 16;

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tiledMapTile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected PlayScreen screen;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.screen = screen;
        world = screen.getWorld();
        map = screen.getMap();
        this.bounds = bounds;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(
                (bounds.getX() + bounds.getWidth() / 2) / MarioBros.PIXELS_PER_METER,
                (bounds.getY() + bounds.getHeight() / 2) / MarioBros.PIXELS_PER_METER
        );

        body = world.createBody(bodyDef);

        polygonShape.setAsBox(
                (bounds.getWidth() / 2) / MarioBros.PIXELS_PER_METER,
                (bounds.getHeight() / 2) / MarioBros.PIXELS_PER_METER
        );

        fixtureDef.shape = polygonShape;
        fixture = body.createFixture(fixtureDef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell(
                (int) (body.getPosition().x * MarioBros.PIXELS_PER_METER / TILE_SIZE),
                ((int) (body.getPosition().y * MarioBros.PIXELS_PER_METER / TILE_SIZE))
        );
    }
}



