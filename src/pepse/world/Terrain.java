package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Terrain {

    public static final float SHRINK_HEIGHT = (float) 2 / 3;
    public static final String GROUND = "ground";
    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private final Vector2 windowDimensions;

    public Terrain(Vector2 windowDimensions, int seed) {

        this.groundHeightAtX0 = windowDimensions.y() * SHRINK_HEIGHT;
        this.noiseGenerator = new NoiseGenerator(seed, (int) Math.floor(groundHeightAtX0));
        this.windowDimensions = windowDimensions;

    }

    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        int minXFloor = (int) Math.floor((float) minX / Block.SIZE);
        int maxXCeil = (int) Math.ceil((float) maxX / Block.SIZE);
        for (int x = minXFloor; x <= maxXCeil; x++) {
            int xValue = x * Block.SIZE;
            int yValue = (int)
                    Math.max(groundHeightAt(xValue), windowDimensions.y() - (TERRAIN_DEPTH * Block.SIZE));
            yValue = (int) Math.floor((float) yValue / Block.SIZE);
            for (int y = yValue; y < yValue + TERRAIN_DEPTH; y++) {
                RectangleRenderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(xValue, y * Block.SIZE), blockRenderable);
//                if (y == (yValue) || y == (yValue + 1)) block.setTag(SURFACE);
                block.setTag(GROUND);
                blocks.add(block);
            }
        }
        return blocks;

    }

}


