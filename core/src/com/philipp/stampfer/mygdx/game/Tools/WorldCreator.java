package com.philipp.stampfer.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;
import com.philipp.stampfer.mygdx.game.Sprites.TileObjects.Brick;
import com.philipp.stampfer.mygdx.game.Sprites.TileObjects.Coin;
import com.philipp.stampfer.mygdx.game.Sprites.Enemies.Goomba;

public class WorldCreator {
    private Array<Goomba> goombas;

    public WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        // create body and fixture variables
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        Body body;

        //
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            bodyDef.position.set(
                    (rectangle.getX() + rectangle.getWidth() / 2) / MarioBros.PIXELS_PER_METER,
                    (rectangle.getY() + rectangle.getHeight() / 2) / MarioBros.PIXELS_PER_METER
            );

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(
                    (rectangle.getWidth() / 2) / MarioBros.PIXELS_PER_METER,
                    (rectangle.getHeight() / 2) / MarioBros.PIXELS_PER_METER
            );

            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fixtureDef);
        }

        // create pipe bodies/fixture
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            bodyDef.position.set(
                    (rectangle.getX() + rectangle.getWidth() / 2) / MarioBros.PIXELS_PER_METER,
                    (rectangle.getY() + rectangle.getHeight() / 2) / MarioBros.PIXELS_PER_METER
            );

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(
                    (rectangle.getWidth() / 2) / MarioBros.PIXELS_PER_METER,
                    (rectangle.getHeight() / 2) / MarioBros.PIXELS_PER_METER
            );

            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        // create brick bodies/fixture
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rectangle);
        }

        // create coin bodies/fixture
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rectangle);
        }

        //create all goombas
        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rectangle.getX() / MarioBros.PIXELS_PER_METER, rectangle.getY() / MarioBros.PIXELS_PER_METER));
        }

    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

}
