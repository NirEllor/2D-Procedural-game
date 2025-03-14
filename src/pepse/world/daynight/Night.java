package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * This class creates a GameObject representing the Night of the world
 */

public class Night {

    // Constants
    private static final float INITIAL_OPACITY = 0f;
    private static final float MIDNIGHT_OPACITY = 0.5f;
    /**
     * A tag for a night GameObject
     */
    public static final String NIGHT = "Night";
    private static final Color NIGHT_COLOR = new Color(0, 0, 0, 1f);


    /**
     * Creates a GameObject representing the Night of the world
     * Constructs its movement according to the given cycle
     * @param windowDimensions - Vector2 : The game window dimensions (height and width)
     * @param cycleLength - Float : The length of the Night cycle
     * @return GameObject : The Night of the world
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){

        Renderable blackRectangle = new RectangleRenderable(NIGHT_COLOR);

        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, blackRectangle);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT);

        new Transition<>(night, night.renderer()::setOpaqueness, INITIAL_OPACITY,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength*MIDNIGHT_OPACITY,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        return night;
    }
}
