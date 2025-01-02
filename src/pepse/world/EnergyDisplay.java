package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import javax.swing.*;
import java.awt.*;

public class EnergyDisplay extends GameObject {

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     *                      the GameObject will not be rendered.
     */
    public EnergyDisplay(Vector2 topLeftCorner, Vector2 dimensions) {

        super(topLeftCorner, dimensions, null);
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

    }

    private void updateEnergy(float currentEnergy) {
        TextRenderable textRenderable = new TextRenderable("Energy: " + (int) currentEnergy);
        textRenderable.setColor(Color.YELLOW);
        renderer().setRenderable(textRenderable);


    }
    public void run(float currentEnergy) {
        updateEnergy(currentEnergy);
    }

}
