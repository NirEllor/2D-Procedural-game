package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Tree {

    private static final Random RANDOM = new Random();
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    public static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int TRUNK_WIDTH = 10;  // רוחב הגזע
    public static final int LEAF_SIZE = 20;  // גודל העלים


    private static void create(ArrayList<GameObject> trees, Terrain terrain, int x) {
        float groundHeight = terrain.groundHeightAt(x);
        Vector2 treePosition = new Vector2(x, groundHeight - 100);

        RectangleRenderable trunkRenderer = new RectangleRenderable(TRUNK_COLOR);
        GameObject trunk = new GameObject(treePosition, new Vector2(30, 100), trunkRenderer);
        trunk.setTag("TreeTrunk");

        ArrayList<GameObject> leaves = Leaves.create(treePosition, RANDOM);

        trees.add(trunk);
        trees.addAll(leaves);
    }


    public static ArrayList<GameObject> plantTrees(Terrain terrain, int minX, int maxX) {
        ArrayList<GameObject> trees = new ArrayList<>();
        for (int x = minX; x < maxX; x++) {
            if (Math.random() < 0.005) {  // 10% chance to plant a tree
                create(trees, terrain, x);
            }
        }
        return trees;
    }

}
