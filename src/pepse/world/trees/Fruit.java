package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

public class Fruit extends GameObject {

    private static final float RESPAWN_TIME = 30;
    public static final int MOVE_COORDINATE = 400;
    private Vector2 saveCenter;
    public static final String FRUIT = "Fruit";

    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(FRUIT);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Avatar.AVATAR)) {
            this.saveCenter = this.getCenter();
            setCenter(new Vector2(-MOVE_COORDINATE, -MOVE_COORDINATE));
            this.respawnFruit();

        }
    }

    private void respawnFruit() {
        new ScheduledTask(
                this,
                RESPAWN_TIME,
                false,
                () -> this.setCenter(this.saveCenter)
        );
    }
}
