package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

    private static final float SHRINK_HEIGHT = (float) 2 / 3;
    public static final int SUN_COORDINATE = 100;
    private static final float HALF = 0.5f;
    public static final float INITIAL_VALUE = 0f;
    public static final float FINAL_VALUE = 360f;
    public static final String SUN = "Sun";

    public static GameObject create(Vector2 windowDimensions, float cycleLength){

        float y = windowDimensions.y() * SHRINK_HEIGHT;
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() * HALF, y * HALF);

        GameObject sun = new GameObject(initialSunCenter,
                 new Vector2(SUN_COORDINATE, SUN_COORDINATE), new OvalRenderable(Color.YELLOW));

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // TODO : check with nir what to choose
        Vector2 cycleCenter = new Vector2(windowDimensions.x() * HALF, y);  // The ground center


        new Transition<>(sun, (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter).
                rotated(angle).add(cycleCenter)), INITIAL_VALUE, FINAL_VALUE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);

        sun.setTag(SUN);

        return sun;

    }





}
