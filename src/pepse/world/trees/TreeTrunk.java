package pepse.world.trees;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

public class TreeTrunk extends Block {
    public TreeTrunk(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        setDimensions(new Vector2(30, 100));
    }
}
