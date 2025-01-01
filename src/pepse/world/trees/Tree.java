package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;

public class Tree {

    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    public static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int TRUNK_WIDTH = 10;  // רוחב הגזע
    public static final int LEAF_SIZE = 20;  // גודל העלים
    public static final String TREE_TRUNK = "TreeTrunk";


    public static void create(ArrayList<TreeInfo> trees, Terrain terrain, int x, Random rand) {
        float groundHeight = terrain.groundHeightAt(x);
        Vector2 treePosition = new Vector2(x, groundHeight - 100);

        RectangleRenderable trunkRenderer = new RectangleRenderable(TRUNK_COLOR);
        GameObject trunk = new GameObject(treePosition, new Vector2(30, 100), trunkRenderer) {
            @Override
            public boolean shouldCollideWith(GameObject other) {
                if (other.getTag().equals(Avatar.AVATAR)) {
                    return true;
                }
                return super.shouldCollideWith(other);
            }
        };
        trunk.setTag(TREE_TRUNK);

        ArrayList<GameObject> leaves = Leaves.create(treePosition, rand);
        int fruitAmount = rand.nextInt(12);
        fruitAmount = max(3, fruitAmount);
        ArrayList<GameObject> fruits = Fruits.createFruits(treePosition, fruitAmount, rand);

        TreeInfo tree = new TreeInfo(trunk, leaves, fruits);
        trees.add(tree);

    }

}
