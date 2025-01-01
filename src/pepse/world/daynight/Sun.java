package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

    private static final float SHRINK_HEIGHT = (float) 2 / 3;

    public static GameObject create(Vector2 windowDimensions, float cycleLength){

        float y = windowDimensions.y() * SHRINK_HEIGHT;
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, y/2);

        GameObject sun = new GameObject(initialSunCenter,
                 new Vector2(100, 100), new OvalRenderable(Color.YELLOW));

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // TODO : check with nir what to choose
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, y);  // The ground center


        new Transition<Float>(sun, (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter).
                rotated(angle).add(cycleCenter)), 0f, 360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);

        sun.setTag("Sun");

        return sun;

    }





}
