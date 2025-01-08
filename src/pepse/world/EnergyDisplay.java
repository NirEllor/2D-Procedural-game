package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * The EnergyDisplay class represents an on-screen display for showing the player's current energy level.
 * It updates dynamically based on the energy value provided and is rendered as text in the game window.
 */
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

    /**
     * Public method to update and render the current energy level.
     * Calls the private updateEnergy method to handle the display.
     *
     * @param currentEnergy The current energy level to display.
     */
    public void run(float currentEnergy) {
        updateEnergy(currentEnergy);
    }

}
