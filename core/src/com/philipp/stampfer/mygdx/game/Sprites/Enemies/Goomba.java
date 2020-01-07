package com.philipp.stampfer.mygdx.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;
import com.philipp.stampfer.mygdx.game.Sprites.Enemies.Enemy;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean goombaGetsDestroyed;
    private boolean isGoombaDestroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getTextureAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PIXELS_PER_METER, 16 / MarioBros.PIXELS_PER_METER);
        goombaGetsDestroyed = false;
        isGoombaDestroyed = false;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (goombaGetsDestroyed && !isGoombaDestroyed) {
            world.destroyBody(body2d);
            isGoombaDestroyed = true;
            setRegion(new TextureRegion(screen.getTextureAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        } else if (!isGoombaDestroyed) {
            body2d.setLinearVelocity(velocity);

            setPosition(body2d.getPosition().x - getWidth() / 2 , body2d.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(getX(), getY());
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
        body2d.createFixture(fixtureDef).setUserData(this);

        //Create the Head
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PIXELS_PER_METER);
        vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PIXELS_PER_METER);
        vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PIXELS_PER_METER);
        vertice[3] = new Vector2(3, 3).scl(1 / MarioBros.PIXELS_PER_METER);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;

        fixtureDef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        body2d.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead() {
        goombaGetsDestroyed = true;
    }

    public void draw(Batch batch) {
        // goomba gets destroyed after 1 s
        if (!isGoombaDestroyed || stateTime < 1) {
            super.draw(batch);
        }
    }
}
