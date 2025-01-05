package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static pepse.world.trees.Tree.LEAF_SIZE;

public class Leaf extends GameObject {

    public static final float INITIAL_MOVEMENT = 8f;
    public static final float TRANSITION_TIME = 3f;
    public static final float SIZE_FACTOR = 1.1f;

    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, float delay) {
        super(topLeftCorner, dimensions, renderable);
        addDelayedMovement(this, delay);
    }

    private void addLeafMovement(GameObject leaf) {
        new Transition<>(
                leaf,
                (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                -INITIAL_MOVEMENT, INITIAL_MOVEMENT,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );


        new Transition<>(leaf, leaf::setDimensions, new Vector2(LEAF_SIZE, LEAF_SIZE),
                new Vector2(LEAF_SIZE * SIZE_FACTOR, LEAF_SIZE * SIZE_FACTOR),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

    }

    private void addDelayedMovement(GameObject leaf, float delay) {
        new ScheduledTask(
                leaf,
                delay,
                false,
                () -> addLeafMovement(leaf)
        );
    }
}
