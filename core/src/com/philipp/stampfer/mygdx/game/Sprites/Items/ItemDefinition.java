package com.philipp.stampfer.mygdx.game.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

public class ItemDefinition {
    public Vector2 position;
    public Class<?> type;

    public ItemDefinition(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;

    }
}
