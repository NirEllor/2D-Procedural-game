package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.daynight.Cloud;
import pepse.world.daynight.Night;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.TreeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PepseGameManager extends GameManager {
    public static final Random RANDOM = new Random();

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
            gameObjects().addGameObject(tree.getTree(), Layer.STATIC_OBJECTS);
            for (GameObject leaf : tree.getLeaves()) {
                gameObjects().addGameObject(leaf, Layer.BACKGROUND);

            }
            for (GameObject fruit : tree.getFruits()) {
                gameObjects().addGameObject(fruit, Layer.STATIC_OBJECTS);
            }
        }

        for (int i = 0; i < 6; i++) {
            ArrayList<GameObject> cloud = Cloud.createCloud(
                    new Vector2(-200*(i), 100*(i%3+1) ));

            for (GameObject obj : cloud) {
                gameObjects().addGameObject(obj, Layer.BACKGROUND);
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

