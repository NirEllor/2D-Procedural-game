package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The Terrain class represents a terrain generator for a 2D world.
 * It generates ground blocks using Perlin noise to create natural and varying terrain heights.
 * The terrain is generated within a specified range and has a fixed depth.
 */
public class Terrain {

    private static final float SHRINK_HEIGHT = (float) 2 / 3;
    private static final int SEVEN = 7;
    private static final int TERRAIN_DEPTH = 20;
    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    private final Vector2 windowDimensions;
    private final NoiseGenerator noiseGenerator;

    /** Tag assigned to ground blocks */
    public static final String GROUND = "ground";


    /**
     * Constructor for the Terrain class.
     * Initializes the ground height and noise generator using a random seed.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed A random seed for generating consistent terrain patterns.
     */
    public Terrain(Vector2 windowDimensions, int seed) {

        this.groundHeightAtX0 = windowDimensions.y() * SHRINK_HEIGHT;
        this.noiseGenerator = new NoiseGenerator(seed, (int) Math.floor(groundHeightAtX0));
        this.windowDimensions = windowDimensions;

    }

    /**
     * Computes the ground height at a given x-coordinate using Perlin noise.
     *
     * @param x The x-coordinate for which the ground height is computed.
     * @return The y-coordinate representing the ground height at the given x.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * SEVEN);
        return groundHeightAtX0 + noise;
    }


    /**
     * Creates ground blocks in a specified range of x-coordinates.
     * For each x-coordinate, blocks are generated from the ground height down to a fixed depth.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of Block objects representing the ground within the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        int minXFloor = (int) Math.floor((float) minX / Block.SIZE);
        int maxXCeil = (int) Math.ceil((float) maxX / Block.SIZE);
        for (int x = minXFloor; x <= maxXCeil; x++) {
            int xBlock = x * Block.SIZE;
            int yValue = (int)
                    Math.max(groundHeightAt(xBlock), windowDimensions.y() - (TERRAIN_DEPTH * Block.SIZE));
            yValue = (int) Math.floor((float) yValue / Block.SIZE);
            for (int yBlock = yValue; yBlock < yValue + TERRAIN_DEPTH; yBlock++) {
                RectangleRenderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(xBlock, yBlock * Block.SIZE), blockRenderable);
                block.setTag(GROUND);
                blocks.add(block);
            }
        }
        return blocks;

    }

}


