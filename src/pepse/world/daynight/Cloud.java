package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Cloud class represents a cloud in the game world.
 * A cloud is composed of multiple rectangular blocks arranged in a specific shape.
 * The class provides methods for creating clouds at a given position and animating their movement
 * across the screen. The clouds move horizontally at a fixed speed, and each block starts moving
 * after a random delay to create a natural and dynamic effect.
 */
public class Cloud {

    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final int BLOCK_SIZE = 20;
    private static final int MOVE_FACTOR = 3;
    private static final float INITIAL_VALUE = 0f;
    private static final float TRANSITION_TIME = 3f;
    private static final int STARTING_POINT = -100;
    private static final int[][] cloudShape = new int[][]{
            {0, 1, 1, 0, 0, 0},
            {1, 1, 1, 0, 1, 0},
            {1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1},
            {0, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0}};

    /** Tag for cloud objects */
    public static final String CLOUD = "cloud";


    /**
     * Method to create a cloud composed of multiple blocks.
     * Each block is created at a specific position based on the cloud shape.
     *
     * @param position The starting position of the cloud.
     * @param seed Random seed to control delays in cloud block movement.
     * @return ArrayList<GameObject> containing all blocks of the cloud.
     */
    public static ArrayList<GameObject> createCloud(Vector2 position, int seed, Vector2 windowDimensions) {
        ArrayList<GameObject> cloudBlocks = new ArrayList<>();

        Random randCloud = new Random(seed);
        float delay = randCloud.nextFloat();
        // Iterate over the cloud shape array
        for (int row = 0; row < cloudShape.length; row++) {
            for (int col = 0; col < cloudShape[row].length; col++) {
                if (cloudShape[row][col] == 1) {

                    Vector2 blockPosition = new Vector2(position.x() + col * BLOCK_SIZE,
                            position.y() - row * BLOCK_SIZE);

                    GameObject block = new GameObject(blockPosition, new Vector2(BLOCK_SIZE, BLOCK_SIZE),
                            new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));

                    addDelayedMovement(block, delay, windowDimensions);

                    block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

                    block.setTag(CLOUD);

                    cloudBlocks.add(block);
                }
            }
        }

        return cloudBlocks;
    }

    private static void cloudMovement(GameObject block, Vector2 windowDimensions) {
        new Transition<>(
                block,
                (Float t) -> {
                    float newX = block.getCenter().x() + MOVE_FACTOR;

                    if (newX > windowDimensions.x()) {
                        newX = STARTING_POINT;
                    }

                    block.setCenter(new Vector2(newX, block.getCenter().y()));
                },
                INITIAL_VALUE,
                windowDimensions.x(),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );
    }


    private static void addDelayedMovement(GameObject block, float delay, Vector2 windowDimensions) {
        new ScheduledTask(
                block,
                delay,
                false,
                () -> cloudMovement(block, windowDimensions)
        );
    }
}

