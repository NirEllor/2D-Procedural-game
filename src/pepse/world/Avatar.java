package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.trees.treeParts.Fruit;
import pepse.world.trees.treeParts.TreeTrunk;

import java.awt.Color;
import java.awt.event.KeyEvent;

/**
 * The Avatar class represents the player-controlled character in the game.
 * It handles movement, animations, energy management, and interaction with game objects.
 * The avatar can run, jump, and lose or gain energy based on its actions.
 */
public class Avatar  extends GameObject {

    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final float GRAVITY = 600;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float RUN_ENERGY_LOSS = 0.5F;
    private static final int JUMP_ENERGY_LOSS_FIX = 5;
    private static final int JUMP_ENERGY_LOSS = 10;
    private static final int IDLE_ENERGY_GAIN = 1;
    private static final int MAX_ENERGY = 100;
    private static final int MIN_ENERGY = 0;
    private static final int FACTOR = 50;
    private static final float VELOCITY_FACTOR = 0.75F;
    private static final float TIME_BETWEEN_CLIPS = 1F;
    private static final String SRC_ASSETS_IDLE_0_PNG = "src/assets/idle_0.png";
    private static final String SRC_ASSETS_IDLE_1_PNG = "src/assets/idle_1.png";
    private static final String SRC_ASSETS_IDLE_2_PNG = "src/assets/idle_2.png";
    private static final String SRC_ASSETS_IDLE_3_PNG = "src/assets/idle_3.png";
    private static final String SRC_ASSETS_RUN_0_PNG = "src/assets/run_0.png";
    private static final String SRC_ASSETS_RUN_1_PNG = "src/assets/run_1.png";
    private static final String SRC_ASSETS_RUN_2_PNG = "src/assets/run_2.png";
    private static final String SRC_ASSETS_RUN_3_PNG = "src/assets/run_3.png";
    private static final String SRC_ASSETS_RUN_4_PNG = "src/assets/run_4.png";
    private static final String SRC_ASSETS_RUN_5_PNG = "src/assets/run_5.png";
    private static final String SRC_ASSETS_JUMP_0_PNG = "src/assets/jump_0.png";
    private static final String SRC_ASSETS_JUMP_1_PNG = "src/assets/jump_1.png";
    private static final String SRC_ASSETS_JUMP_2_PNG = "src/assets/jump_2.png";
    private static final String SRC_ASSETS_JUMP_3_PNG = "src/assets/jump_3.png";

    /**
     *  A tag for the Avatar
     */
    public static final String AVATAR = "avatar";


    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private boolean touchingTerrain;
    private EnergyUpdateCallback energyUpdateCallback;
    private Runnable rainCallback;
    private float energy;
    Renderable[] idleAnimations;
    Renderable[] runAnimations;
    Renderable[] jumpAnimations;

    AnimationRenderable idleAnimation;
    AnimationRenderable runAnimation;
    AnimationRenderable jumpAnimation;


    /**
     * Constructor for the Avatar class.
     * Initializes the avatar's position, input listener, image reader, animations, and energy.
     *
     * @param topLeftCorner Initial position of the avatar.
     * @param inputListener Listener for handling user input.
     * @param imageReader Reader for loading images used in animations.
     */
    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {

        super(topLeftCorner, Vector2.ONES.mult(FACTOR), new OvalRenderable(AVATAR_COLOR));
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        readImages();
        energy = MAX_ENERGY;
        touchingTerrain = false;
        setTag(AVATAR);
    }

    /**
     * Handles actions when a collision with another object begins.
     * Stops vertical motion when colliding with the ground and increases energy when colliding with fruit.
     *
     * @param other The other GameObject involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other.getTag().equals(Block.BLOCK_TAG)) {
            this.transform().setVelocityY(0); // Stop any vertical motion
        }

        if (other.getTag().equals(Terrain.GROUND) || other.getTag().equals(TreeTrunk.TREE_TRUNK)) {
            this.touchingTerrain = true;
        }

        if (other.getTag().equals(Fruit.FRUIT)) {
            increaseEnergy(JUMP_ENERGY_LOSS);
        }
    }

    /**
     * Handles actions when a collision with another object continues.
     *
     * @param other The other GameObject involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);

        if (other.getTag().equals(Terrain.GROUND) || other.getTag().equals(TreeTrunk.TREE_TRUNK)) {
            this.touchingTerrain = true;
        }
    }

    /**
     * Handles actions when a collision with another object ends.
     *
     * @param other The other GameObject involved in the collision.
     */
    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);

        if (other.getTag().equals(Terrain.GROUND) || other.getTag().equals(TreeTrunk.TREE_TRUNK)) {
            this.touchingTerrain = false;
        }
    }

    private void readImages() {

         idleAnimations = new Renderable[]{
                imageReader.readImage(SRC_ASSETS_IDLE_0_PNG, true),
                imageReader.readImage(SRC_ASSETS_IDLE_1_PNG, true),
                imageReader.readImage(SRC_ASSETS_IDLE_2_PNG, true),
                imageReader.readImage(SRC_ASSETS_IDLE_3_PNG, true)
        };
        runAnimations = new Renderable[]{

                imageReader.readImage(SRC_ASSETS_RUN_0_PNG, true),
                imageReader.readImage(SRC_ASSETS_RUN_1_PNG, true),
                imageReader.readImage(SRC_ASSETS_RUN_2_PNG, true),
                imageReader.readImage(SRC_ASSETS_RUN_3_PNG, true),
                imageReader.readImage(SRC_ASSETS_RUN_4_PNG, true),
                imageReader.readImage(SRC_ASSETS_RUN_5_PNG, true)
        };
        jumpAnimations = new Renderable[]{
                imageReader.readImage(SRC_ASSETS_JUMP_0_PNG, true),
        imageReader.readImage(SRC_ASSETS_JUMP_1_PNG, true),
        imageReader.readImage(SRC_ASSETS_JUMP_2_PNG, true),
        imageReader.readImage(SRC_ASSETS_JUMP_3_PNG, true)
        };

        idleAnimation = new AnimationRenderable(idleAnimations, TIME_BETWEEN_CLIPS);
        runAnimation = new AnimationRenderable(runAnimations, TIME_BETWEEN_CLIPS);
        jumpAnimation = new AnimationRenderable(jumpAnimations, TIME_BETWEEN_CLIPS);

    }

    /**
     * Updates the avatar's state based on user input, energy level, and terrain contact.
     *
     * @param deltaTime Time passed since the last frame (in seconds).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = MIN_ENERGY;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)  && !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)
                && getEnergy() >= RUN_ENERGY_LOSS) {
            handleRunning(true);
            xVel -= VELOCITY_X;
        } else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)  &&
                !inputListener.isKeyPressed(KeyEvent.VK_LEFT) && getEnergy() >= RUN_ENERGY_LOSS) {
            handleRunning(false);
            xVel += VELOCITY_X;
        } else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && touchingTerrain &&
                getEnergy() >= JUMP_ENERGY_LOSS) {
            handleJumping();
        } else {
            handleIdle();
        }
        transform().setVelocityX(xVel);
        energyUpdateCallback.onEnergyUpdate((int) getEnergy());
    }


    private void decreaseEnergy(float amount) {
        setEnergy(Math.max(MIN_ENERGY, getEnergy() - amount));
    }

    private void increaseEnergy(float amount) {
        setEnergy(Math.min(MAX_ENERGY, getEnergy() + amount));
    }

    private void handleRunning(boolean isLeft) {
        float velocityChange = isLeft ? -VELOCITY_X : VELOCITY_X;
        transform().setVelocityX(velocityChange);
        renderer().setIsFlippedHorizontally(isLeft);
        if (touchingTerrain) {
            decreaseEnergy(RUN_ENERGY_LOSS);
            updateAvatarRunImage();
        }
        else {
            updateAvatarIdleImage();
        }

    }

    private void handleJumping() {
        transform().setVelocityY(VELOCITY_Y * VELOCITY_FACTOR);
        decreaseEnergy(JUMP_ENERGY_LOSS_FIX);
        updateAvatarJumpImage();
        rainCallback.run();

    }

    private void handleIdle() {
        if (touchingTerrain) {
            increaseEnergy(IDLE_ENERGY_GAIN);
        }
        updateAvatarIdleImage();
    }


    private void updateAvatarIdleImage() {
        renderer().setRenderable(idleAnimation);
    }

    private void updateAvatarJumpImage() {
        renderer().setRenderable(jumpAnimation);
    }

    /**
     * Updates the avatar's renderable to the running animation.
     */
    private void updateAvatarRunImage() {
        renderer().setRenderable(runAnimation);
    }

    /**
     * Sets the callback function for updating the energy display.
     *
     * @param callback The callback function to set.
     */
    public void setEnergyUpdateCallback(EnergyUpdateCallback callback) {
        this.energyUpdateCallback = callback;
    }

    /**
     * Sets the callback function for triggering rain when the avatar jumps.
     *
     * @param rainCallback The callback function to set.
     */
    public void setRainCallback(Runnable rainCallback) {
        this.rainCallback = rainCallback;
    }

    /**
     * Sets the avatar's energy level.
     *
     * @param energy The new energy level.
     */
    public void setEnergy(float energy) {
        this.energy = energy;
    }

    /**
     * Gets the current energy level of the avatar.
     *
     * @return The current energy level.
     */
    public float getEnergy() {
        return energy;
    }

}
