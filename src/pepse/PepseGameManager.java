package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.trees.Block;
import pepse.world.trees.Sky;
import pepse.world.trees.Terrain;
import java.util.List;

public class PepseGameManager extends GameManager {

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        Terrain terrain = new Terrain(windowDimensions, 42);
        gameObjects().addGameObject(Sky.create(windowDimensions), Layer.BACKGROUND);
        List<Block> blocks = terrain.createInRange(0, (int) windowDimensions.x());
        for (Block b : blocks) {
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }

//        GameObject cloud1 = createCloud(this.windowDimensions, new Vector2(200, 100)); // ענן ראשון
//        GameObject cloud2 = createCloud(this.windowDimensions, new Vector2(500, 150)); // ענן שני
//
//        // הוספת העננים לשכבה
//        gameObjects().addGameObject(cloud1, Layer.BACKGROUND);
//        gameObjects().addGameObject(cloud2, Layer.BACKGROUND);
    }


//    private GameObject createCloud(Vector2 windowDimensions, Vector2 position) {
//        Color cloudColor = Color.decode("#FFFFFF"); // צבע הענן לבן
//        Vector2 cloudSize = new Vector2(150, 50); // גודל הענן
//
//        // יצירת הענן (מלבן לבן)
//        GameObject cloud = new GameObject(position, cloudSize, new RectangleRenderable(cloudColor));
//        cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // הענן ינוע עם המצלמה
//        cloud.setTag("cloud"); // הוספת תג עבור הדיבוג
//
//        return cloud;
//    }

    public void run() {
        super.run();
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

