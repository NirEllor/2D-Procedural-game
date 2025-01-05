package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    public static final String SUN_HALO = "SunHalo";
    public static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    public static final int SUN_HALO_COORDINATE = 150;

    public static GameObject create(GameObject sun){

        // Create the halo GameObject with the defined position, size, and color
        GameObject sunHalo = new GameObject(sun.getCenter(), new Vector2(SUN_HALO_COORDINATE,
                SUN_HALO_COORDINATE), new OvalRenderable(SUN_HALO_COLOR));

        // Set the coordinate space to CAMERA_COORDINATES, like the sun
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));

        // Set a unique tag for the halo for debugging purposes
        sunHalo.setTag(SUN_HALO);

        return sunHalo;

    }
}
