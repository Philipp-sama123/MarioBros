package com.philipp.stampfer.mygdx.game.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Screens.PlayScreen;
import com.philipp.stampfer.mygdx.game.Sprites.Mario;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean isToDestroy;
    protected boolean isDestroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / MarioBros.PIXELS_PER_METER, 16 / MarioBros.PIXELS_PER_METER);
        defineItem();
        isToDestroy = false;
        isDestroyed = false;
    }

    public abstract void defineItem();

    public abstract void use(Mario mario);

    public void update(float deltaTime) {
        if (isToDestroy && !isDestroyed) {
            world.destroyBody(body);
            isDestroyed = true;
        }
    }

    public void draw(Batch batch) {
        if (!isDestroyed) {
            super.draw(batch);
        }
    }

    public void destroy() {
        isToDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }
}
