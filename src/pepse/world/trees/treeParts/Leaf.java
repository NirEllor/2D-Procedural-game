package pepse.world.trees.treeParts;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class creates a Leaf Object
 */
public class Leaf extends GameObject {

    // Constants
    private static final float INITIAL_MOVEMENT = 8f;
    private static final float TRANSITION_TIME = 3f;
    private static final float SIZE_FACTOR = 1.1f;

    // Fields
    private final int leafSize;

    /**
     * Constructor - Creates a Leaf object
     * @param topLeftCorner - Vector2 : The coordinates to place the leaf at on the screen
     * @param dimensions - Vector2 : The leaf dimensions
     * @param renderable - Renderable : The renderer of the leaf
     * @param delay - float : The delay in the movement of the leaf
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, float delay,
                int leafSize) {
        super(topLeftCorner, dimensions, renderable);
        this.leafSize = leafSize;
        addDelayedMovement(delay);
    }

    /**
     * Handles the leaf movement. Changes the angle of the leaf as well as its size.
     */
    private void addLeafMovement() {
        new Transition<>(
                this,
                (Float angle) -> this.renderer().setRenderableAngle(angle),
                -INITIAL_MOVEMENT, INITIAL_MOVEMENT,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );


        new Transition<>(this, this::setDimensions,
                new Vector2(this.leafSize, this.leafSize),
                new Vector2(this.leafSize * SIZE_FACTOR, this.leafSize * SIZE_FACTOR),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

    }

    /**
     * Delays the movement of the leaf
     * @param delay - float : The delay in the movement of the leaf
     */
    private void addDelayedMovement(float delay) {

        new ScheduledTask(
                this,
                delay,
                false,
                this::addLeafMovement
        );

    }
}
