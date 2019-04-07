package com.philipp.stampfer.mygdx.game.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;
import com.philipp.stampfer.mygdx.game.Sprites.Mario;

public class Mushroom extends Item {

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getTextureAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6 / MarioBros.PIXELS_PER_METER);
        fixtureDef.filter.categoryBits = MarioBros.ITEM_BIT;
        fixtureDef.filter.maskBits = MarioBros.MARIO_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT ;

        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
        destroy();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
