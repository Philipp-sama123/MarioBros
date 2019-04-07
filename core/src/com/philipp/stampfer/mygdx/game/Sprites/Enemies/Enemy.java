package com.philipp.stampfer.mygdx.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;

    public Body body2d;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        body2d.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public abstract void update(float deltaTime);

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }
}
