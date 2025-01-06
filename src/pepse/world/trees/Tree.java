package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import static java.lang.Math.max;

/**
 * This class creates a single Tree object
 */
public class Tree {

    // Constants
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final Color FRUIT_COLOR = Color.RED;
    private static final String TREE_TRUNK = "TreeTrunk";
    private static final int FRUIT_SIZE = 15;
    private static final int LEAF_SIZE = 20;
    private static final int GAP = 70;
    private static final int FRUIT_AMOUNT = 12;
    private static final int X_BOUND = 60;
    private static final int Y_BOUND_MAX = 90;
    private static final int Y_BOUND_MIN = 50;
    private static final int LEAVES_AMOUNT = 80;
    private static final int LEAVES_AMOUNT_MIN = 30;
    private static final int X_BOUND_LEAF = 70;
    private static final int Y_BOUND_LEAF = 100;
    private static final int LEAF_OFFSET = 20;


    /**
     * Creates a single Tree with TreeTrunk, leaves and fruits
     * @param trees - ArrayList<TreeInfo> : The array of trees to add the tree to
     * @param groundHeightAtFunc - Function<Integer, Float> : Function that gets an x,
     *                             calculates the ground height at specific x and returns
     *                             the result
     * @param x - int : The x coordinate to create the tree on
     * @param rand - Random : The Random object to randomize the creation with
     */
    public static void create(ArrayList<TreeInfo> trees, Function<Integer, Float> groundHeightAtFunc, int x,
                              Random rand) {

        float groundHeight = groundHeightAtFunc.apply(x);
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

    /**
     * Creates the fruits of the tree
     * @param position - Vector2 : The position of the tree
     * @param fruitCount - int : The amount of fruits to create
     * @param rand - Random : The Random object to randomize the creation with
     * @return ArrayList<GameObject> - Array of fruits
     */
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

    /**
     * Creates a single fruit
     * @param position  - Vector2 : The position of the tree
     * @return GameObject : The fruit
     */
    private static GameObject createFruit(Vector2 position) {

        OvalRenderable fruitRenderer = new OvalRenderable(FRUIT_COLOR);
        return new Fruit(position, new Vector2(FRUIT_SIZE, FRUIT_SIZE), fruitRenderer);

    }

    /**
     * Creates the leaves of the tree
     * @param position - Vector2 : The position of the tree
     * @param rand - Random : The Random object to randomize the creation with
     * @return ArrayList<GameObject> - Array of leaves
     */
    private static ArrayList<GameObject> createLeaves(Vector2 position, Random rand) {

        ArrayList<GameObject> leaves = new ArrayList<>();
        int leafCount = rand.nextInt(LEAVES_AMOUNT);
        leafCount = Math.max(leafCount, LEAVES_AMOUNT_MIN);

        for (int i = 0; i < leafCount; i++) {

            int xOffset = rand.nextInt(X_BOUND_LEAF);
            int minus = rand.nextBoolean() ? 1 : -1;
            xOffset *= minus;
            int yOffset = rand.nextInt(Y_BOUND_LEAF) + LEAF_OFFSET;

            float delay = rand.nextFloat();

            GameObject leaf = new Leaf(new Vector2((position.x() + xOffset),
                    position.y() - yOffset),
                    new Vector2(LEAF_SIZE, LEAF_SIZE),
                    new RectangleRenderable(LEAF_COLOR), delay, LEAF_SIZE);

            leaves.add(leaf);

        }

        return leaves;

    }

}
