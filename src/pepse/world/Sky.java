package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;

public class Sky {

    /**
     * This class creates a GameObject representing the Sky of the world
     */

    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    public static final String SKY = "sky";

    /**
     * Creates a GameObject representing the Sky of the world
     * @param windowDimensions - Vector2 : The game window dimensions (height and width)
     * @return GameObject : The Sky of the world
     */
    public static GameObject create(Vector2 windowDimensions){

        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));

        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        sky.setTag(SKY);

        return sky;
    }
}
