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
import pepse.world.trees.Fruit;
import pepse.world.trees.TreeInfo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PepseGameManager extends GameManager {

    private static final int AVATAR_TERRAIN_DIST = 100;
    private static final int RAIN_DROP_SIZE = 10;
    private static final Color RAIN_DROP_COLOR = new Color(173, 216, 230);
    private static final float CYCLE_LENGTH = 30;
    public static final float FACTOR = 0.5f;


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


    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {

        seed = new Random().nextInt();
        this.userInputListener = inputListener;
        this.windowController = windowController;

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(40);
        this.windowDimensions = windowController.getWindowDimensions();

        createDayNight();
        createClouds();

        terrain = new Terrain(windowDimensions, seed);
        makeBlocks(0, (int) windowDimensions.x());

        flora = new Flora(terrain::groundHeightAt, windowDimensions, seed);
        makeTrees(0, (int) windowDimensions.x());

        createAvatar(inputListener, imageReader);

    }

    private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {

        float xInitialAvatarLocation = windowDimensions.mult(FACTOR).x();
        float yInitialAvatarLocation = terrain.groundHeightAt(xInitialAvatarLocation) - AVATAR_TERRAIN_DIST;
        Vector2 initialAvatarLocation = new Vector2(xInitialAvatarLocation, yInitialAvatarLocation);

        Avatar avatar = new Avatar(initialAvatarLocation, inputListener, imageReader);
//        avatar.setCloud(allClouds);
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
                new Vector2(windowDimensions.x() - 300, 100),
                new Vector2(100, 30));
        gameObjects().addGameObject(energyDisplay);
        avatar.setEnergyUpdateCallback(energyDisplay::run);
    }

    private void createClouds() {
        allClouds = new ArrayList<>();
        ArrayList<GameObject> cloud;
        for (int i = 0; i < 12; i++) {
            cloud = Cloud.createCloud(new Vector2(-400 * (i), 100 * (i % 3 + 1)), seed);
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
                int numRaindrops = rainRand.nextInt(5) + 1; // Random number of raindrops between 1 and 5

                for (int i = 0; i < numRaindrops; i++) {
                    GameObject rainDrop = new GameObject(
                            obj.getTopLeftCorner(),
                            new Vector2(RAIN_DROP_SIZE, RAIN_DROP_SIZE),
                            new OvalRenderable(RAIN_DROP_COLOR)
                    );
                    rainDrop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

                    float delay = rainRand.nextFloat() * 2f; // Random delay between 0 and 2 seconds
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
                cloud.getTopLeftCorner().y() + 500, // Move 500 pixels down
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                3f, // Duration of fall (3 seconds)
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
                                        (int) (alpha * 255) // Set alpha value
                                )
                        )
                ),
                1f, // Starting alpha (fully opaque)
                0f, // Ending alpha (fully transparent)
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                3f, // Duration of fade-out matches the fall duration
                Transition.TransitionType.TRANSITION_ONCE,
                null // No callback needed
        );
    }



    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);

        if (userInputListener.isKeyPressed(KeyEvent.VK_ESCAPE)) windowController.closeWindow();

        if (camera().getCenter().x() != currentCameraCenterX) {

            float cameraMovementRange = (currentCameraCenterX - camera().getCenter().x()); //checking how much the camera moved

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
            if (gameObject.getCenter().x() > currentMaxX | gameObject.getCenter().x() < currentMinX |
                    gameObject.getCenter().y() > this.windowController.getWindowDimensions().y() |
                    (gameObject.getCenter().y() < 0 && !gameObject.getTag().equals(Fruit.FRUIT))) {
                if (gameObject.getTag().equals("cloud") && gameObject.getCenter().x() > currentMaxX) {
                        gameObject.setCenter(new Vector2(currentMinX + 10, gameObject.getCenter().y()));
                } else {
                    gameObjects().removeGameObject(gameObject);
                }
            }
        }
    }

    private void makeTrees(int min, int max) {

        ArrayList<TreeInfo> trees = flora.createInRange(min, max);

        //TODO : check layers - The fruits should collide with the player, the player should be stopped by the trunk,
        // the leaves should NOT collide with the player

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

    public void run() {
        super.run();
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

