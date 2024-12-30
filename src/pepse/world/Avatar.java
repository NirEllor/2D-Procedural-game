package pepse.world;

import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Avatar {
    private static final String avatarImagePath = "src/assets/__MACOSX/assets/._idle_0.png";
    private Vector2 topLeftCorner;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {
        this.topLeftCorner = topLeftCorner;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        ImageRenderable heartImage = imageReader.readImage(avatarImagePath,true);

    }
}
