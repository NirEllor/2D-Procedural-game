package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class Avatar  extends GameObject {

    private static final String avatarImagePath = "assets/idle_0.png";
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final float GRAVITY = 600;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final int WALKING_CYCLE_LENGTH = 6;

    private UserInputListener inputListener;
    private ImageReader imageReader;
    private int walkingCycleStepNumber;

    private ImageRenderable downAvatarImgReader;
    private ImageRenderable upAvatarImgReader;
    private ImageRenderable leftAvatarImgReader;
    private ImageRenderable rightAvatarImgReader;
    private ImageRenderable jumpMoveImageReader;



    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {

        super(topLeftCorner, Vector2.ONES.mult(50), new OvalRenderable(AVATAR_COLOR));
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        readImages();
        walkingCycleStepNumber = 0;

    }

    private void readImages() {
        downAvatarImgReader = imageReader.readImage("src/assets/idle_0.png", true);
        upAvatarImgReader = imageReader.readImage("src/assets/idle_1.png",true);
        leftAvatarImgReader = imageReader.readImage("",true);
        rightAvatarImgReader = imageReader.readImage("",true);
        jumpMoveImageReader = imageReader.readImage("",true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            renderer().setIsFlippedHorizontally(true);

        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            renderer().setIsFlippedHorizontally(false);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(VELOCITY_Y * 0.75F);
            renderer().setRenderable(jumpMoveImageReader);

        }
        updateAvatarMovementCycle();
        transform().setVelocityX(xVel);

    }


    private void updateAvatarMovementCycle() {
        walkingCycleStepNumber = (walkingCycleStepNumber + 1) % WALKING_CYCLE_LENGTH;
        if (walkingCycleStepNumber < (WALKING_CYCLE_LENGTH / 2)) {
            renderer().setRenderable(downAvatarImgReader);
        } else {
            renderer().setRenderable(upAvatarImgReader);
        }
    }
}
