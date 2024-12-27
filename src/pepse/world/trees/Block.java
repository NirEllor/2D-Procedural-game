package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Block extends GameObject {

    private static final String BLOCK_TAG = "Block";
    public static final int SIZE = 30;

    public Block(Vector2 topLeftCorner, Renderable renderable) {

        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(BLOCK_TAG);

    }
}

