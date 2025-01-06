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


public class Cloud {

    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final int BLOCK_SIZE = 20; // גודל כל בלוק בענן
    private static final float CLOUD_SPEED = 50f; // מהירות הענן (פיקסלים בשנייה)
    public static final int MOVE_FACTOR = 1;
    public static final float INITIAL_VALUE = 0f;
    public static final float END_VALUE = 1f;
    public static final float TRANSITION_TIME = 3f;
    public static final String CLOUD = "cloud";
    static int[][] cloudShape = new int[][]{
            {0, 1, 1, 0, 0, 0},
            {1, 1, 1, 0, 1, 0},
            {1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1},
            {0, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0}};

    // Method to create the cloud
    public static ArrayList<GameObject> createCloud(Vector2 position, int seed) {
        ArrayList<GameObject> cloudBlocks = new ArrayList<>();

        Random randCloud = new Random(seed);
        float delay = randCloud.nextFloat();
        // Iterate over the cloud shape array
        for (int row = 0; row < cloudShape.length; row++) {
            for (int col = 0; col < cloudShape[row].length; col++) {
                if (cloudShape[row][col] == 1) {  // If there's a block at this position
                    // Create a block at the appropriate position
                    Vector2 blockPosition = new Vector2(position.x() + col * BLOCK_SIZE,
                            position.y() - row * BLOCK_SIZE);

                    GameObject block = new GameObject(blockPosition, new Vector2(BLOCK_SIZE, BLOCK_SIZE),
                            new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));

                    addDelayedMovement(block, delay);

                    block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

                    block.setTag(CLOUD);


                    cloudBlocks.add(block);  // Add the block to the cloud
                }
            }
        }

        return cloudBlocks;
    }

    private static void cloudMovement(GameObject block) {
        new Transition<>(
                block,  // The object to apply the transition to (the block)
                (Float t) -> {
                    // This lambda defines how the transition will update the block's position
                    // 't' represents the time progress of the transition (from 0 to 1)
                    block.setCenter(new Vector2(block.getCenter().x() + MOVE_FACTOR, block.getCenter().y()));
                },
                INITIAL_VALUE,  // Starting value for the transition
                END_VALUE,  // Ending value for the transition (progress from 0 to 1)
                Transition.LINEAR_INTERPOLATOR_FLOAT,  // The interpolation function (linear interpolation)
                TRANSITION_TIME,  // Duration of the transition in seconds (move every 3 seconds)
                Transition.TransitionType.TRANSITION_LOOP,  // Loop the transition continuously
                null  // No additional logic on completion
        );
    }

    private static void addDelayedMovement(GameObject block, float delay) {
        new ScheduledTask(
                block,
                delay,
                false,
                () -> cloudMovement(block)
        );
    }



}

