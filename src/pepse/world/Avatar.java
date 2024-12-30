package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.Color;
import java.awt.event.KeyEvent;


public class Avatar  extends GameObject {

    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final float GRAVITY = 600;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    public static final float RUN_ENERGY_LOSS = 0.5F;
    public static final int JUMP_ENERGY_LOSS = 10;
    public static final int IDLE_ENERGY_GAIN = 1;

    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final float terrainY;
    private Terrain terrain;

    private float energy;
    Renderable[] idleAnimations;
    Renderable[] runAnimations;
    Renderable[] jumpAnimations;

    AnimationRenderable idleAnimation;
    AnimationRenderable runAnimation;
    AnimationRenderable jumpAnimation;



    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {

        super(topLeftCorner, Vector2.ONES.mult(50), new OvalRenderable(AVATAR_COLOR));
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.terrainY = topLeftCorner.y();
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        readImages();
        energy = 100;

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals(Block.BLOCK_TAG)){
            this.transform().setVelocityY(0);
        }
    }


    private void readImages() {

         idleAnimations = new Renderable[]{
                imageReader.readImage("src/assets/idle_0.png", true),
                imageReader.readImage("src/assets/idle_1.png", true),
                imageReader.readImage("src/assets/idle_2.png", true),
                imageReader.readImage("src/assets/idle_3.png", true)
        };
        runAnimations = new Renderable[]{

                imageReader.readImage("src/assets/run_0.png", true),
                imageReader.readImage("src/assets/run_1.png", true),
                imageReader.readImage("src/assets/run_2.png", true),
                imageReader.readImage("src/assets/run_3.png", true),
                imageReader.readImage("src/assets/run_4.png", true),
                imageReader.readImage("src/assets/run_5.png", true)
        };
        jumpAnimations = new Renderable[]{
                imageReader.readImage("src/assets/jump_0.png", true),
        imageReader.readImage("src/assets/jump_1.png", true),
        imageReader.readImage("src/assets/jump_2.png", true),
        imageReader.readImage("src/assets/jump_3.png", true)
        };

        idleAnimation = new AnimationRenderable(idleAnimations, 1F);
        runAnimation = new AnimationRenderable(runAnimations, 0.2F);
        jumpAnimation = new AnimationRenderable(jumpAnimations, 1F);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;

        // Run Left
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                !(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) && (energy >= RUN_ENERGY_LOSS)) {
            xVel -= VELOCITY_X;
            renderer().setIsFlippedHorizontally(true);
            energy = Math.max(0, energy - RUN_ENERGY_LOSS);
            updateAvatarRunImage();

        }
        // Run Right
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                !(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) && (energy >= RUN_ENERGY_LOSS)) {
            xVel += VELOCITY_X;
            renderer().setIsFlippedHorizontally(false);
            energy = Math.max(0, energy - RUN_ENERGY_LOSS);
            updateAvatarRunImage();

        }
        // Jump
        else if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && (getVelocity().y() == 0) &&
                        (energy >= JUMP_ENERGY_LOSS)) {
            transform().setVelocityY(VELOCITY_Y * 0.75F);
            if (getTopLeftCorner().y() <= terrain.groundHeightAt(getTopLeftCorner().x())) {
                energy = Math.max(0, energy - JUMP_ENERGY_LOSS);

            }


            updateAvatarJumpImage();

        }
        // Idle stay
        else {
            System.out.println(getTopLeftCorner().y());
            System.out.println(terrain.groundHeightAt(getTopLeftCorner().x()));
            if (getTopLeftCorner().y() >= terrain.groundHeightAt(getTopLeftCorner().x())) {
                energy = Math.min(100, energy + IDLE_ENERGY_GAIN);
                System.out.println(energy);

            }
                updateAvatarIdleImage();
            }
        transform().setVelocityX(xVel);


    }


    private void updateAvatarIdleImage() {
        renderer().setRenderable(idleAnimation);
    }

    private void updateAvatarJumpImage() {
        renderer().setRenderable(jumpAnimation);
    }

    private void updateAvatarRunImage() {
        renderer().setRenderable(runAnimation);
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }
    public float getEnergy() {
        return energy;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
