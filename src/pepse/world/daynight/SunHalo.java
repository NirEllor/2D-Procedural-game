package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * This class creates a GameObject representing the Sun in the game
 */
public class SunHalo {

    // Constants
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int SUN_HALO_COORDINATE = 150;

    /**
     * Creates a GameObject representing the Sun in the game.
     * Constructs its movement according to the given GameObject, that is the sun
     * @param sun - GameObject : The sun the halo needs to move accordingly
     * @return GameObject : The Sun of the world
     */
    public static GameObject create(GameObject sun){

        GameObject sunHalo = new GameObject(sun.getCenter(), new Vector2(SUN_HALO_COORDINATE,
                SUN_HALO_COORDINATE), new OvalRenderable(SUN_HALO_COLOR));

        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;

    }
}
