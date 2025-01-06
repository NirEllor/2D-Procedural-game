package pepse.world.trees;

import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * This class is in charge of creating all the trees (the trunk, leaves and fruits)
 */
public class Flora {

    // Constants
    private static final float HALF = 0.5f;
    private static final float FACTOR = 0.1f;
    private static final int MIDDLE = 100;
    private static final int RANGE = 40;

    // Fields
    private final Function<Integer, Float> groundHeightAtFunc;
    private final Vector2 windowDimensions;
    private final int seed;
    Random rand;

    /**
     * Constructor - Creates a new Flora object
     * @param groundHeightAtFunc - Function<Integer, Float> : Function that gets an x,
     *                             calculates the ground height at specific x and returns
     *                             the result
     * @param windowDimensions - Vector2 : The game window dimensions (height and width)
     * @param seed - int : The seed for the Random object to make the world consistent
     */
    public Flora(Function<Integer, Float> groundHeightAtFunc, Vector2 windowDimensions,
                 int seed){

        this.groundHeightAtFunc = groundHeightAtFunc;
        this.seed = seed;
        this.windowDimensions = windowDimensions;

    }

    /**
     * Creates randomly placed trees at the given range.
     * @param minX - int : The start of the range
     * @param maxX - int : The end of the range
     * @return ArrayList<TreeInfo> : Array of TreeInfo object, each object contains the Trunk,
     *                               leaves and fruits.
     */
    public ArrayList<TreeInfo> createInRange(int minX, int maxX) {

        ArrayList<TreeInfo> trees = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            this.rand = new Random(Objects.hash(x, seed));
            if (this.rand.nextFloat() < FACTOR) {
                if (Math.abs(x - (windowDimensions.x() * HALF)) < MIDDLE) {
                    continue;
                }
                Tree.create(trees, this.groundHeightAtFunc, x, this.rand);
                x += RANGE;
            }
        }

        return trees;
    }



}
