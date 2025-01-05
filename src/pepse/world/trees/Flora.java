package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Flora {

    private static final float HALF = 0.5f;
    private static final float FACTOR = 0.1f;
    private static final int MIDDLE = 100;
    private static final int RANGE = 40;
    private final Terrain terrain;
    private final Vector2 windowDimensions;
    private final int seed;
    Random rand;

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
            if (this.rand.nextFloat() < FACTOR) {
                if (Math.abs(x - (windowDimensions.x() * HALF)) < MIDDLE) {
                    continue;
                }
                Tree.create(trees, terrain, x, this.rand);

                x += RANGE;
            }
        }
        return trees;
    }


}
