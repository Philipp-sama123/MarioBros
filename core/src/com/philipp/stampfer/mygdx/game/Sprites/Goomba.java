package com.philipp.stampfer.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getTextureAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PIXELS_PER_METER, 16 / MarioBros.PIXELS_PER_METER);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(
                32 / MarioBros.PIXELS_PER_METER,
                32 / MarioBros.PIXELS_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body2d = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6 / MarioBros.PIXELS_PER_METER);

        fixtureDef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.MARIO_BIT;

        fixtureDef.shape = circleShape;
        body2d.createFixture(fixtureDef);

        EdgeShape feet = new EdgeShape();
        feet.set(
                new Vector2(-2 / MarioBros.PIXELS_PER_METER, -8 / MarioBros.PIXELS_PER_METER),
                new Vector2(2 / MarioBros.PIXELS_PER_METER, -8 / MarioBros.PIXELS_PER_METER)
        );
        body2d.createFixture(fixtureDef);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        setPosition(body2d.getPosition().x - getWidth() / 2, body2d.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
    }
}
