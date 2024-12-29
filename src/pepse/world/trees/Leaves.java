package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.Random;

import static pepse.world.trees.Tree.LEAF_COLOR;
import static pepse.world.trees.Tree.LEAF_SIZE;

public class Leaves {

    public static ArrayList<GameObject> create(Vector2 position, Random random) {
        ArrayList<GameObject> leaves = new ArrayList<>();

        int leafCount = random.nextInt(30, 50);
        for (int i = 0; i < leafCount; i++) {

            int xOffset = random.nextInt(-70, 70);
            int yOffset = random.nextInt(-20, 100);

            GameObject leaf = new GameObject(new Vector2((position.x() + xOffset),
                    position.y() - yOffset),
                    new Vector2(LEAF_SIZE, LEAF_SIZE),
                    new RectangleRenderable(LEAF_COLOR));


            float delay = random.nextFloat(2);
            addDelayedMovement(leaf, delay);

            leaves.add(leaf);

        }

        return leaves;
    }

    private static void addLeafMovement(GameObject leaf) {
        new Transition<>(
                leaf,
                (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                -8f, 8f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                3f,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );


        new Transition<>(leaf, leaf::setDimensions, new Vector2(LEAF_SIZE, LEAF_SIZE),
                new Vector2(LEAF_SIZE * 1.1f, LEAF_SIZE * 1.1f),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                3f,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

    }

    private static void addDelayedMovement(GameObject leaf, float delay) {
        new ScheduledTask(
                leaf,
                delay,
                false,
                () -> addLeafMovement(leaf)
        );
    }
}

