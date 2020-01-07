package com.philipp.stampfer.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;

public class Mario extends Sprite {
    public static final int LITTLE_MARIO_HEIGHT = 16;
    public static final int LITTLE_MARIO_WIDTH = 16;

    public enum State {
        FALLING, JUMPING, STANDING, RUNNING
    }

    public State currentState;
    public State previousState;

    public World world;
    public Body body2d;

    private TextureRegion marioStand;

    private Animation marioRun;
    private Animation marioJump;

    private float stateTimer;
    private boolean runningRight;

    public Mario(PlayScreen playScreen) {
        super(playScreen.getTextureAtlas().findRegion("little_mario"));
        this.world = playScreen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;

        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion((getTexture()), i * 16, 0, LITTLE_MARIO_WIDTH, LITTLE_MARIO_HEIGHT));
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++)
            frames.add(new TextureRegion(getTexture(), i * 16, 0, LITTLE_MARIO_WIDTH, LITTLE_MARIO_HEIGHT));
        marioJump = new Animation(0.1f, frames);

        marioStand = new TextureRegion(getTexture(), 0, 0, LITTLE_MARIO_WIDTH, LITTLE_MARIO_HEIGHT);

        defineMario();
        setBounds(0, 0, LITTLE_MARIO_WIDTH / MarioBros.PIXELS_PER_METER, LITTLE_MARIO_WIDTH / MarioBros.PIXELS_PER_METER);
        setRegion(marioStand);
    }

    public void update(float deltaTime) {
        setPosition(body2d.getPosition().x - getWidth() / 2, body2d.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaTime));
    }

    private TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;

        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }
        if ((body2d.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body2d.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if ((body2d.getLinearVelocity().y > 0) || (body2d.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (body2d.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (body2d.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(
                32 / MarioBros.PIXELS_PER_METER,
                32 / MarioBros.PIXELS_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body2d = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6 / MarioBros.PIXELS_PER_METER);

        fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ITEM_BIT |
                MarioBros.ENEMY_HEAD_BIT;

        fixtureDef.shape = circleShape;
        body2d.createFixture(fixtureDef);

        EdgeShape feet = new EdgeShape();
        feet.set(
                new Vector2(-2 / MarioBros.PIXELS_PER_METER, -8 / MarioBros.PIXELS_PER_METER),
                new Vector2(2 / MarioBros.PIXELS_PER_METER, -8 / MarioBros.PIXELS_PER_METER)
        );
        body2d.createFixture(fixtureDef);

        EdgeShape head = new EdgeShape();
        head.set(
                new Vector2(-2 / MarioBros.PIXELS_PER_METER, 5 / MarioBros.PIXELS_PER_METER),
                new Vector2(2 / MarioBros.PIXELS_PER_METER, 5 / MarioBros.PIXELS_PER_METER)
        );

        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        body2d.createFixture(fixtureDef).setUserData("head");
    }
}
