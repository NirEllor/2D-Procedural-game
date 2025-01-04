package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.util.Vector2;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import danogl.gui.rendering.OvalRenderable;
import pepse.world.Avatar;

public class Fruits {

    private static final int FRUIT_SIZE = 15;
    private static final int RESPAWN_TIME = 30;
    private static final Color FRUIT_COLOR = new Color(255, 100, 100);
    public static final String FRUIT = "Fruit";


    public static ArrayList<GameObject> createFruits(Vector2 position, int fruitCount, Random rand) {
        ArrayList<GameObject> fruits = new ArrayList<>();
        for (int i = 0; i < fruitCount; i++) {
            int xOffset = rand.nextInt(60);
            int minus = rand.nextBoolean() ? 1 : -1;
            xOffset *= minus;
            int yOffset = rand.nextInt(90);
            yOffset = Math.max(50, yOffset);
            minus = rand.nextBoolean() ? 1 : -1/9;
            yOffset *= minus;
//            Random rand = new Random();
//            int xOffset = PepseGameManager.RANDOM.nextInt(-60, 60);
//            int yOffset = PepseGameManager.RANDOM.nextInt(-10, 90);

            Vector2 fruitPosition = new Vector2(position.x() + xOffset, position.y() - yOffset);
            GameObject fruit = createFruit(fruitPosition);
            fruits.add(fruit);
        }
        return fruits;
    }

    public static GameObject createFruit(Vector2 position) {
        // יצירת פרי
        OvalRenderable fruitRenderer = new OvalRenderable(FRUIT_COLOR);
        GameObject fruit = new GameObject(position, new Vector2(FRUIT_SIZE, FRUIT_SIZE), fruitRenderer) {
            @Override
            public void onCollisionEnter(GameObject other, Collision collision) {
                super.onCollisionEnter(other, collision);
                if (other.getTag().equals(Avatar.AVATAR)) {
                    setCenter(new Vector2(-100, -100));
                    respawnFruit(this);

                }
            }
        };
        fruit.setTag(FRUIT);
        return fruit;
    }

//    // TODO : Delete when Yuli approves
//    public static void handleCollision(GameObject fruit, GameObject character, Runnable onEaten) {
//        if (fruit.getTag().equals(FRUIT) && character.getTag().equals("Player")) {
//            fruit.setCenter(new Vector2(-100, -100));
//            //onEaten.run();  // פעולה שתוגדר להענקת נקודות אנרגיה
//            respawnFruit(fruit);
//        }
//    }

    //TODO - how to get back to the center if the tree moves?
    private static void respawnFruit(GameObject fruit) {
        new ScheduledTask(
                fruit,
                RESPAWN_TIME,
                false, //TODO: maybe yes?
                () -> {
                    Vector2 originalPosition = fruit.getDimensions();  // או מיקום אחר שנשמר מראש
                    fruit.setCenter(originalPosition);
                }
        );
    }

}
