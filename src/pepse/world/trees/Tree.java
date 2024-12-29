package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Tree {

    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    public static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int TRUNK_WIDTH = 10;  // רוחב הגזע
    public static final int LEAF_SIZE = 20;  // גודל העלים


    public static void create(ArrayList<TreeInfo> trees, Terrain terrain, int x) {
        float groundHeight = terrain.groundHeightAt(x);
        Vector2 treePosition = new Vector2(x, groundHeight - 100);

        RectangleRenderable trunkRenderer = new RectangleRenderable(TRUNK_COLOR);
        GameObject trunk = new GameObject(treePosition, new Vector2(30, 100), trunkRenderer);
        trunk.setTag("TreeTrunk");

        ArrayList<GameObject> leaves = Leaves.create(treePosition);
        int fruitAmount = PepseGameManager.RANDOM.nextInt(5);
        ArrayList<GameObject> fruits = Fruits.createFruits(treePosition, fruitAmount);

        TreeInfo tree = new TreeInfo(trunk, leaves, fruits);
        trees.add(tree);

    }

}
