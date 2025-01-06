package pepse.world.trees;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

/**
 * This class creates TreeTrunk object
 */
public class TreeTrunk extends Block {

    // Constants
    private static final int TRUNK_WIDTH = 40;
    private static final int TRUNK_HEIGHT = 100;
    public static final String TREE_TRUNK = "TreeTrunk";

    /**
     * Constructor - Creates a TreeTrunk object
     * @param topLeftCorner - Vector2 : The coordinates to place the trunk at on the screen
     * @param renderable - Renderable : The renderer of the trunk
     */
    public TreeTrunk(Vector2 topLeftCorner, Renderable renderable) {

        super(topLeftCorner, renderable);
        setDimensions(new Vector2(TRUNK_WIDTH, TRUNK_HEIGHT));
        setTag(TREE_TRUNK);

    }
}
