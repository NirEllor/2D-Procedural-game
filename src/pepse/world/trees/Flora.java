package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.Random;

public class Flora {

    private final Terrain terrain;
    private final Vector2 windowDimensions;
    private static Random rand;

    public Flora(Terrain terrain, Vector2 windowDimensions, Random rand){

        this.terrain = terrain;
        this.windowDimensions = windowDimensions;
        Flora.rand = rand;
    }
    public ArrayList<TreeInfo> createInRange(int minX, int maxX) {
        return plantTrees(this.terrain, minX, maxX, windowDimensions);
    }

    public static ArrayList<TreeInfo> plantTrees(Terrain terrain, int minX, int maxX, Vector2 windowDimensions) {
        ArrayList<TreeInfo> trees = new ArrayList<>();
        for (int x = minX; x < maxX; x++) {
            if (Math.random() < 0.015) {  // 10% chance to plant a tree
                if(Math.abs(x - (windowDimensions.x() / 2)) < 100) {
                    continue;
                }
                Tree.create(trees, terrain, x, rand);
            }
        }
        return trees;
    }

}
