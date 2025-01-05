package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Flora {

    private final Terrain terrain;
    private final Vector2 windowDimensions;
    private final int seed;
    Random rand;
    private static final int FRUIT_SIZE = 15;
    private static final Color FRUIT_COLOR = new Color(255, 100, 100);

    public Flora(Terrain terrain, Vector2 windowDimensions, int seed){
        this.seed = seed;
        this.terrain = terrain;
        this.windowDimensions = windowDimensions;
    }
    public ArrayList<TreeInfo> createInRange(int minX, int maxX) {
        return plantTrees(this.terrain, minX, maxX, windowDimensions);
    }

    public ArrayList<TreeInfo> plantTrees(Terrain terrain, int minX, int maxX, Vector2 windowDimensions) {
        ArrayList<TreeInfo> trees = new ArrayList<>();
        for (int x = minX; x < maxX; x++) {
            this.rand = new Random(Objects.hash(x, seed));
            if (this.rand.nextFloat() < 0.1) {
                if (Math.abs(x - (windowDimensions.x() / 2)) < 100) {
                    continue;
                }
                Tree.create(trees, terrain, x, this.rand);

                x+=100;
            }
        }
        return trees;
    }


}
