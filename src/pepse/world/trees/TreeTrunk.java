package pepse.world.trees;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

public class TreeTrunk extends Block {

    private static final int TRUNK_WIDTH = 40;
    private static final int TRUNK_HEIGHT = 100;

    public TreeTrunk(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        setDimensions(new Vector2(TRUNK_WIDTH, TRUNK_HEIGHT));
    }
}
