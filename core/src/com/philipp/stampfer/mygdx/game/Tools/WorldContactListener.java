package com.philipp.stampfer.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Sprites.Enemies.Enemy;
import com.philipp.stampfer.mygdx.game.Sprites.Items.Item;
import com.philipp.stampfer.mygdx.game.Sprites.Mario;
import com.philipp.stampfer.mygdx.game.Sprites.TileObjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collissionDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        if (fixtureA.getUserData() == "head" || fixtureB.getUserData() == "head") {
            Fixture head = fixtureA.getUserData() == "head" ? fixtureA : fixtureB;
            Fixture object = head == fixtureA ? fixtureB : fixtureA;

            if (object.getUserData() != null &&
                    InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch (collissionDefinition) {
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                if (fixtureA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
                    ((Enemy) fixtureA.getUserData()).hitOnHead();
                else
                    ((Enemy) fixtureB.getUserData()).hitOnHead();
                break;
            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
                if (fixtureA.getFilterData().categoryBits == MarioBros.ENEMY_BIT)
                    ((Enemy) fixtureA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
                Gdx.app.log("MARIO", "died..");
                break;
            case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
                ((Enemy) fixtureA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
                if (fixtureA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item) fixtureA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
                if (fixtureA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item) fixtureA.getUserData()).use((Mario) fixtureB.getUserData());
                else
                if(!(fixtureA.getUserData() instanceof String))
                    ((Item) fixtureB.getUserData()).use((Mario) fixtureA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
