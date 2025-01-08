package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Cloud;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.treeParts.Fruit;
import pepse.world.trees.TreeInfo;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The PepseGameManager class is responsible for managing the entire game.
 * It initializes the world elements (terrain, trees, avatar, clouds, day/night cycle),
 * handles user input, and updates game objects during the game loop.
 */
public class PepseGameManager extends GameManager {

    private static final int AVATAR_TERRAIN_DIST = 40;
    private static final int RAIN_DROP_SIZE = 10;
    private static final Color RAIN_DROP_COLOR = new Color(173, 216, 230);
    private static final float CYCLE_LENGTH = 30;
    private static final float FACTOR = 0.5f;
    private static final int WHITE = 255;
    private static final int MOVE_FACTOR = 500;
    private static final float TRANSITION_TIME = 3f;
    private static final float STARTING_TIME = 1f;
    private static final float FINISH_TIME = 0f;
    private static final float DELAY = 2f;
    private static final int BOUND = 5;
    private static final int X_CLOUD = -500;
    private static final int ONE_HUNDRED = 100;
    private static final int THIRTY = 30;
    private static final int THREE_HUNDRED = 300;
    private static final int FORTY = 40;


    private UserInputListener userInputListener;
    private WindowController windowController;
    private Terrain terrain;
    private float currentCameraCenterX;
    private float currentMinX;
    private float currentMaxX;
    private Flora flora;
    private int seed;
    private ArrayList<ArrayList<GameObject>> allClouds;
    private Vector2 windowDimensions;



    /**
     * Initializes the game, including terrain, trees, clouds, avatar, and day/night cycle.
     *
     * @param imageReader Reader for loading images.
     * @param soundReader Reader for loading sounds.
     * @param inputListener Listener for handling user input.
     * @param windowController Controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener
            inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        // create the seed
        seed = new Random().nextInt();

        // UserInput and WindowController
        this.userInputListener = inputListener;
        this.windowController = windowController;
        windowController.setTargetFramerate(FORTY);
        this.windowDimensions = windowController.getWindowDimensions();

        // Night. Day and Clouds
        createDayNight();
        createClouds();

        // Terrain
        terrain = new Terrain(windowDimensions, seed);
        makeBlocks(0, (int) windowDimensions.x());

        // Trees, Leafs and Fruits
        flora = new Flora(terrain::groundHeightAt, windowDimensions, seed);
        makeTrees(0, (int) windowDimensions.x());

        // Avatar
        createAvatar(inputListener, imageReader);

    }

    private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {

        float xInitialAvatarLocation = windowDimensions.mult(FACTOR).x();
        float yInitialAvatarLocation = terrain.groundHeightAt(xInitialAvatarLocation) - AVATAR_TERRAIN_DIST;
        Vector2 initialAvatarLocation = new Vector2(xInitialAvatarLocation, yInitialAvatarLocation);

        Avatar avatar = new Avatar(initialAvatarLocation, inputListener, imageReader);
        avatar.setRainCallback(() -> createRain(allClouds));

        createDisplayEnergy(avatar);

        gameObjects().addGameObject(avatar);

        this.currentCameraCenterX = avatar.getCenter().x();
        this.currentMinX = 0;
        this.currentMaxX = windowDimensions.x();

        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

    }

    private void createDisplayEnergy(Avatar avatar) {
        EnergyDisplay energyDisplay = new EnergyDisplay(
                new Vector2(windowDimensions.x() - THREE_HUNDRED, ONE_HUNDRED),
                new Vector2(ONE_HUNDRED, THIRTY));
        gameObjects().addGameObject(energyDisplay);
        avatar.setEnergyUpdateCallback(energyDisplay::run);
    }

    private void createClouds() {
        allClouds = new ArrayList<>();
        ArrayList<GameObject> cloud;
        for (int i = 0; i < 3; i++) {
            cloud = Cloud.createCloud(new Vector2(X_CLOUD * (i), ONE_HUNDRED * (i + 1)), seed,
                    windowDimensions);
            allClouds.add(cloud);
            for (GameObject obj : cloud) {
                gameObjects().addGameObject(obj, Layer.BACKGROUND);
                obj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            }
        }
    }

    private void createDayNight(){

        gameObjects().addGameObject(Sky.create(windowDimensions), Layer.BACKGROUND);

        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.BACKGROUND);

        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

    }


    private void createRain(ArrayList<ArrayList<GameObject>> finalCloud) {
        Random rainRand = new Random(seed);

        for (ArrayList<GameObject> arr : finalCloud) {
            for (GameObject obj : arr) {
                int numRaindrops = rainRand.nextInt(BOUND) + 1;

                for (int i = 0; i < numRaindrops; i++) {
                    GameObject rainDrop = new GameObject(
                            obj.getTopLeftCorner(),
                            new Vector2(RAIN_DROP_SIZE, RAIN_DROP_SIZE),
                            new OvalRenderable(RAIN_DROP_COLOR)
                    );
                    rainDrop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

                    float delay = rainRand.nextFloat() * DELAY; // Random delay between 0 and 2 seconds
                    scheduleRainDropMovement(rainDrop, obj, delay);
                }
            }
        }
    }

    private void scheduleRainDropMovement(GameObject rainDrop, GameObject cloud, float delay) {
        new ScheduledTask(
                cloud, // Use cloud as the anchor for the task
                delay,
                false, // One-time task
                () -> {
                    // Add the raindrop to the game right before it starts falling
                    gameObjects().addGameObject(rainDrop, Layer.BACKGROUND);
                    startRainDropMovement(rainDrop, cloud);
                }
        );
    }

    private void startRainDropMovement(GameObject rainDrop, GameObject cloud) {
        // Transition for falling movement (Y-axis)
        new Transition<>(
                rainDrop,
                pos -> rainDrop.setTopLeftCorner(new Vector2(cloud.getTopLeftCorner().x(), pos)),
                cloud.getTopLeftCorner().y(),
                cloud.getTopLeftCorner().y() + MOVE_FACTOR, // Move 500 pixels down
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TRANSITION_TIME, // Duration of fall (3 seconds)
                Transition.TransitionType.TRANSITION_ONCE,
                () -> gameObjects().removeGameObject(rainDrop, Layer.BACKGROUND) // Remove when done
        );

        // Transition for transparency change (alpha)
        new Transition<>(
                rainDrop,
                alpha -> rainDrop.renderer().setRenderable(
                        new OvalRenderable(
                                new Color(
                                        RAIN_DROP_COLOR.getRed(),
                                        RAIN_DROP_COLOR.getGreen(),
                                        RAIN_DROP_COLOR.getBlue(),
                                        (int) (alpha * WHITE) // Set alpha value
                                )
                        )
                ),
                STARTING_TIME, // Starting alpha (fully opaque)
                FINISH_TIME, // Ending alpha (fully transparent)
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TRANSITION_TIME, // Duration of fade-out matches the fall duration
                Transition.TransitionType.TRANSITION_ONCE,
                null // No callback needed
        );
    }


    /**
     * Updates the game state on each frame.
     * Handles camera movement, dynamically generates and removes game objects (terrain, trees) as needed,
     * and checks for user input to close the game window.
     *
     * @param deltaTime Time passed since the last frame (in seconds).
     */
    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);

        if (userInputListener.isKeyPressed(KeyEvent.VK_ESCAPE)) windowController.closeWindow();

        if (camera().getCenter().x() != currentCameraCenterX) {

            float cameraMovementRange = (currentCameraCenterX - camera().getCenter().x());
            if (cameraMovementRange != 0) {
                if (cameraMovementRange > 0) { //means avatar went left
                    makeBlocks((int)(currentMinX - cameraMovementRange), (int)currentMinX);

                    makeTrees((int)(currentMinX - cameraMovementRange), (int)currentMinX);

                    currentMinX -= Block.SIZE;

                }
                else { //means avatar went right

                    makeBlocks((int) currentMaxX, (int) (currentMaxX - cameraMovementRange));

                    makeTrees((int) currentMaxX, (int) (currentMaxX - cameraMovementRange));

                    currentMaxX += Block.SIZE;

                }

            }
            currentCameraCenterX = camera().getCenter().x();
        }

        removeObjects();
    }

    private void removeObjects() {
        for(GameObject gameObject: gameObjects()) { //deleting unnecessary game objects
            if (gameObject.getCenter().x() > currentMaxX |
                    ((gameObject.getCenter().x()) < currentMinX &
                            (!gameObject.getTag().equals(Cloud.CLOUD)) |
                    gameObject.getCenter().y() > this.windowController.getWindowDimensions().y() |
                    (gameObject.getCenter().y() < 0 & !gameObject.getTag().equals(Fruit.FRUIT)))) {
                    gameObjects().removeGameObject(gameObject);
                }
            }
        }


    private void makeTrees(int min, int max) {

        ArrayList<TreeInfo> trees = flora.createInRange(min, max);

        for (TreeInfo tree : trees) {
            gameObjects().addGameObject(tree.getTreeTrunk(), Layer.STATIC_OBJECTS);
            for (GameObject leaf : tree.getLeaves()) {
                gameObjects().addGameObject(leaf, Layer.BACKGROUND);
            }
            for (GameObject fruit : tree.getFruits()) {
                gameObjects().addGameObject(fruit, Layer.STATIC_OBJECTS);
            }
        }
    }

    private void makeBlocks(int min, int max) {
        List<Block> blocks = terrain.createInRange(min, max);
        for (Block b : blocks) {
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Runs the game by starting the game loop.
     */
    public void run() {
        super.run();
    }

    /**
     * Main method to launch the game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

