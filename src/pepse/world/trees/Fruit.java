package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

/**
 * This class creates a Fruit object
 */
public class Fruit extends GameObject {

    // Constants
    private static final float RESPAWN_TIME = 30;
    public static final int MOVE_COORDINATE = 400;
    public static final String FRUIT = "Fruit";

    // Fields
    private Vector2 saveCenter;

    /**
     * Constructor - Creates a Fruit object
     * @param topLeftCorner - Vector2 : The coordinates to place the fruit at on the screen
     * @param dimensions - Vector2 : The fruit dimensions
     * @param renderable - Renderable : The renderer of the fruit
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {

        super(topLeftCorner, dimensions, renderable);
        this.setTag(FRUIT);

    }

    /**
     * Handles the fruit actions when it enters collision
     * @param other - GameObject : The other game object the fruit collided with
     * @param collision - Collision : Information about the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {

        super.onCollisionEnter(other, collision);

        if (other.getTag().equals(Avatar.AVATAR)) {
            this.saveCenter = this.getCenter();
            setCenter(new Vector2(-MOVE_COORDINATE, -MOVE_COORDINATE));
            this.respawnFruit();

        }

    }

    /**
     * After the fruit is eaten, the function is responsible for returning it to its original
     * position after 30 seconds
     */
    private void respawnFruit() {

        new ScheduledTask(
                this,
                RESPAWN_TIME,
                false,
                () -> this.setCenter(this.saveCenter)
        );

    }
}
