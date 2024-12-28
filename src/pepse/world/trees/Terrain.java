package pepse.world.trees;

import danogl.util.Vector2;
import pepse.NoiseGenerator;

public class Terrain {

    public static final float SHRINK_HEIGHT = (float) 2 / 3;
    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;

    public Terrain(Vector2 windowDimensions, int seed) {

        this.groundHeightAtX0 = windowDimensions.y() * SHRINK_HEIGHT;
        this.noiseGenerator = new NoiseGenerator(seed, (int) Math.floor(groundHeightAtX0));

    }
    public float groundHeightAt(float x) {
       float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
       return groundHeightAtX0 + noise;
    }

}
