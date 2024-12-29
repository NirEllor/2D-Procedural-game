package pepse.world.trees;

import danogl.GameObject;
import pepse.PepseGameManager;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.Random;

public class Flora {

    private final Terrain terrain;

    public Flora(Terrain terrain){
        this.terrain = terrain;
    }
    public ArrayList<TreeInfo> createInRange(int minX, int maxX) {
        return plantTrees(this.terrain, minX, maxX);
    }

    public static ArrayList<TreeInfo> plantTrees(Terrain terrain, int minX, int maxX) {
        ArrayList<TreeInfo> trees = new ArrayList<>();
        for (int x = minX; x < maxX; x++) {
            if (Math.random() < 0.005) {  // 10% chance to plant a tree
                Tree.create(trees, terrain, x);
            }
        }
        return trees;
    }

}
