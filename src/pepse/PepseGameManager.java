package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Cloud;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.TreeInfo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PepseGameManager extends GameManager {
    public static final Random RANDOM = new Random();
    private static final int AVATAR_TERRAIN_DIST = 100;
    private static final float NUMERIC_LIFE_SIZE = 2000;
    private static final float NUMERIC_LIFE_SPACE_X = 160;
    private static final float NUMERIC_LIFE_SPACE_Y = 36;
    private static final float GRAVITY = 600f;
    private static final float RAIN_DROP_TRANSPARENCY_DECREMENT = 0.02f;
    private static final int RAIN_DROP_SIZE = 10;
    private static final Color RAIN_DROP_COLOR = new Color(173, 216, 230);




    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(50);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        Terrain terrain = new Terrain(windowDimensions, 42);

        gameObjects().addGameObject(Sky.create(windowDimensions), Layer.BACKGROUND);

        GameObject night = Night.create(windowDimensions, 30);
        gameObjects().addGameObject(night, Layer.BACKGROUND);

        GameObject sun = Sun.create(windowDimensions, 30);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

        List<Block> blocks = terrain.createInRange(0, (int) windowDimensions.x());
        for (Block b : blocks) {
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }

        Flora flora = new Flora(terrain);
        ArrayList<TreeInfo> trees = flora.createInRange(0, (int) windowDimensions.x());

        //TODO : check layers - The fruits should collide with the player, the player should be stopped by the trunk,
        // the leaves should NOT collide with the player
        for (TreeInfo tree : trees) {
            gameObjects().addGameObject(tree.getTree());
            for (GameObject leaf : tree.getLeaves()) {
                gameObjects().addGameObject(leaf, Layer.STATIC_OBJECTS);

            }
            for (GameObject fruit : tree.getFruits()) {
                gameObjects().addGameObject(fruit, Layer.STATIC_OBJECTS);
            }
        }

        ArrayList<ArrayList<GameObject>> allClouds = new ArrayList<>();
        ArrayList<GameObject> cloud = null;
        for (int i = 0; i < 6; i++) {
            cloud = Cloud.createCloud(
                    new Vector2(-200 * (i), 100 * (i % 3 + 1)));
            allClouds.add(cloud);
            for (GameObject obj : cloud) {
                gameObjects().addGameObject(obj, Layer.BACKGROUND);
            }
        }
        // create Avatar
        float xInitialAvatarLocation = windowDimensions.mult(0.5f).x();
        float yInitialAvatarLocation = terrain.groundHeightAt(xInitialAvatarLocation) - AVATAR_TERRAIN_DIST;
        Vector2 initialAvatarLocation = new Vector2(xInitialAvatarLocation, yInitialAvatarLocation);
        Avatar avatar = new Avatar(initialAvatarLocation, inputListener, imageReader);
        avatar.setCloud(allClouds);
        ArrayList<ArrayList<GameObject>> finalCloud = allClouds;
        avatar.setRainCallback(() -> createRain(finalCloud));

        EnergyDisplay energyDisplay = new EnergyDisplay(
                new Vector2(windowDimensions.x() * 0.5F, 250),
                new Vector2(100, 30)
        );
        gameObjects().addGameObject(energyDisplay);
        avatar.setEnergyUpdateCallback(energyDisplay::run);


        gameObjects().addGameObject(avatar);

//        float x =  windowController.getWindowDimensions().x() * 0.5f - initialAvatarLocation.x() * 0.5f;
//        float y = windowController.getWindowDimensions().y() * 0.5f - initialAvatarLocation.y() * 0.5f;
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    private void addDelayedMovement(GameObject block, float delay) {
        new ScheduledTask(
                block,
                delay,
                false,
                () -> rainMovement(block)
        );
    }

    private void rainMovement(GameObject rainDrop){
        // Transition for transparency and removal
        new Transition<>(
                rainDrop,
                alpha -> {
                    if (alpha <= 0) {
                        gameObjects().removeGameObject(rainDrop, Layer.UI);
                    } else {
                        rainDrop.renderer().setRenderable(
                                new OvalRenderable(
                                        new Color(RAIN_DROP_COLOR.getRed(), RAIN_DROP_COLOR.getGreen(),
                                                RAIN_DROP_COLOR.getBlue(), (int) (alpha * 255))
                                )
                        );
                    }
                },
                1f, // Starting alpha
                0f, // Ending alpha
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                10f, // Duration of fade-out
                Transition.TransitionType.TRANSITION_ONCE,
                null
        );
    }

    private void createRain(ArrayList<ArrayList<GameObject>> finalCloud) {
        float delay = PepseGameManager.RANDOM.nextFloat();
        for (ArrayList<GameObject> arr : finalCloud) {
            for ( GameObject obj : arr) {
                GameObject rainDrop = new GameObject(
                        obj.getTopLeftCorner(),
                        new Vector2(RAIN_DROP_SIZE, RAIN_DROP_SIZE),
                        new OvalRenderable(RAIN_DROP_COLOR)
                );


                addDelayedMovement(rainDrop, delay);
                delay = PepseGameManager.RANDOM.nextFloat();

                rainDrop.transform().setAccelerationY(GRAVITY);

                gameObjects().addGameObject(rainDrop, Layer.UI); // Use a layer guaranteed to be visible
            }

        }
    }


    public void run() {
        super.run();
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

