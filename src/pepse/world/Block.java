package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * The Block class represents a single, immovable block in the terrain.
 * Each block has a fixed size and is used to construct the ground in the game world.
 * The block prevents intersections from all directions and is treated as an immovable object in the game
 * physics.
 */
public class Block extends GameObject {

     /** Tag assigned to all block objects */
     public static final String BLOCK_TAG = "Block";
    /** Fixed size of each block (width and height in pixels) */
    public static final int SIZE = 30;

    /**
     * The Block class represents a single, immovable block in the terrain.
     * Each block has a fixed size and is used to construct the ground in the game world.
     * The block prevents intersections from all directions and is treated as an immovable object in
     * the game physics.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {

        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(BLOCK_TAG);

    }
}

