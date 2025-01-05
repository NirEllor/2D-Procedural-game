package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;

public class Tree {

    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    public static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int FRUIT_SIZE = 15;
    private static final Color FRUIT_COLOR = Color.RED;
    public static final int LEAF_SIZE = 20;  // גודל העלים
    public static final String TREE_TRUNK = "TreeTrunk";
    public static final int GAP = 70;
    public static final int FRUIT_AMOUNT = 12;
    public static final int X_BOUND = 60;
    public static final int Y_BOUND_MAX = 90;
    public static final int Y_BOUND_MIN = 50;
    public static final int LEAVES_AMOUNT = 80;
    public static final int LEAVES_AMOUNT_MIN = 30;
    public static final int X_BOUND_LEAF = 70;
    public static final int Y_BOUND_LEAF = 100;
    public static final int LEAF_OOFSET = 20;


    public static void create(ArrayList<TreeInfo> trees, Terrain terrain, int x, Random rand) {
        float groundHeight = terrain.groundHeightAt(x);
        Vector2 treePosition = new Vector2(x, groundHeight - GAP);

        RectangleRenderable trunkRenderer = new RectangleRenderable(TRUNK_COLOR);
        GameObject trunk = new TreeTrunk(treePosition, trunkRenderer);
        trunk.setTag(TREE_TRUNK);

        ArrayList<GameObject> leaves = createLeaves(treePosition, rand);
        int fruitAmount = rand.nextInt(FRUIT_AMOUNT);
        fruitAmount = max(3, fruitAmount);
        ArrayList<GameObject> fruits = createFruits(treePosition, fruitAmount, rand);

        TreeInfo tree = new TreeInfo(trunk, leaves, fruits);
        trees.add(tree);

    }


    private static ArrayList<GameObject> createFruits(Vector2 position, int fruitCount, Random rand) {
        ArrayList<GameObject> fruits = new ArrayList<>();
        for (int i = 0; i < fruitCount; i++) {
            int xOffset = rand.nextInt(X_BOUND);
            int minus = rand.nextBoolean() ? 1 : -1;
            xOffset *= minus;
            int yOffset = rand.nextInt(Y_BOUND_MAX);
            yOffset = Math.max(Y_BOUND_MIN, yOffset);

            Vector2 fruitPosition = new Vector2(position.x() + xOffset, position.y() - yOffset);
            GameObject fruit = createFruit(fruitPosition);
            fruits.add(fruit);
        }
        return fruits;
    }

    private static GameObject createFruit(Vector2 position) {
        // יצירת פרי
        OvalRenderable fruitRenderer = new OvalRenderable(FRUIT_COLOR);
        return new Fruit(position, new Vector2(FRUIT_SIZE, FRUIT_SIZE), fruitRenderer);
    }

    public static ArrayList<GameObject> createLeaves(Vector2 position, Random rand) {
        ArrayList<GameObject> leaves = new ArrayList<>();

        int leafCount = rand.nextInt(LEAVES_AMOUNT);
        leafCount = Math.max(leafCount, LEAVES_AMOUNT_MIN);
        for (int i = 0; i < leafCount; i++) {

            int xOffset = rand.nextInt(X_BOUND_LEAF);
            int minus = rand.nextBoolean() ? 1 : -1;
            xOffset *= minus;
            int yOffset = rand.nextInt(Y_BOUND_LEAF) + LEAF_OOFSET;

            float delay = rand.nextFloat();

            GameObject leaf = new Leaf(new Vector2((position.x() + xOffset),
                    position.y() - yOffset),
                    new Vector2(LEAF_SIZE, LEAF_SIZE),
                    new RectangleRenderable(LEAF_COLOR), delay);

            leaves.add(leaf);

        }

        return leaves;
    }

}
