package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.trees.Fruits;
import pepse.world.trees.Tree;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Avatar  extends GameObject {

    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final float GRAVITY = 600;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    public static final float RUN_ENERGY_LOSS = 0.5F;
    public static final int JUMP_ENERGY_LOSS = 11;
    public static final int IDLE_ENERGY_GAIN = 1;
    public static final int MAX_ENERGY = 100;
    public static final int MIN_ENERGY = 0;
    public static final int FACTOR = 50;
    public static final String SRC_ASSETS_IDLE_0_PNG = "src/assets/idle_0.png";
    public static final String SRC_ASSETS_IDLE_1_PNG = "src/assets/idle_1.png";
    public static final String SRC_ASSETS_IDLE_2_PNG = "src/assets/idle_2.png";
    public static final String SRC_ASSETS_IDLE_3_PNG = "src/assets/idle_3.png";
    public static final String SRC_ASSETS_RUN_0_PNG = "src/assets/run_0.png";
    public static final String SRC_ASSETS_RUN_1_PNG = "src/assets/run_1.png";
    public static final String SRC_ASSETS_RUN_2_PNG = "src/assets/run_2.png";
    public static final String SRC_ASSETS_RUN_3_PNG = "src/assets/run_3.png";
    public static final String SRC_ASSETS_RUN_4_PNG = "src/assets/run_4.png";
    public static final String SRC_ASSETS_RUN_5_PNG = "src/assets/run_5.png";
    public static final String SRC_ASSETS_JUMP_0_PNG = "src/assets/jump_0.png";
    public static final String SRC_ASSETS_JUMP_1_PNG = "src/assets/jump_1.png";
    public static final String SRC_ASSETS_JUMP_2_PNG = "src/assets/jump_2.png";
    public static final String SRC_ASSETS_JUMP_3_PNG = "src/assets/jump_3.png";
    public static final String AVATAR = "avatar";

    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private boolean touchingTerrain;
    private ArrayList<ArrayList<GameObject>> clouds;

    private EnergyUpdateCallback energyUpdateCallback;
    private Runnable rainCallback;




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

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other.getTag().equals(Block.BLOCK_TAG)) {
            this.transform().setVelocityY(0); // Stop any vertical motion
        }

        if (other.getTag().equals(Terrain.GROUND)) {
            this.touchingTerrain = true;
        }

        if (other.getTag().equals(Fruits.FRUIT)) {
            increaseEnergy(JUMP_ENERGY_LOSS);
        }

        if (other.getTag().equals(Tree.TREE_TRUNK)) {
            Vector2 collisionNormal = collision.getNormal();

            // Check if collision is from above
            if (collisionNormal.y() > 0) {
                // Stop downward motion
                this.transform().setVelocityY(0);

                // Snap the avatar's position to the top of the tree trunk
                this.transform().setTopLeftCorner(
                        new Vector2(
                                this.transform().getTopLeftCorner().x(),
                                other.getTopLeftCorner().y() - this.getDimensions().y()
                        )
                );

                // Treat the tree trunk as terrain
                this.touchingTerrain = true;
            } else {
                // Restrict movement into the tree trunk from the sides
                resolveSideCollision(collisionNormal);
            }
        }
    }

    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);

        if (other.getTag().equals(Terrain.GROUND)) {
            this.touchingTerrain = true;
        }

        if (other.getTag().equals(Tree.TREE_TRUNK)) {
            Vector2 collisionNormal = collision.getNormal();

            // Ensure the avatar stays on top of the tree trunk
            if (collisionNormal.y() > 0) {
                this.transform().setVelocityY(0); // Stop any vertical motion

                // Snap the avatar's position to remain on the top of the tree trunk
                this.transform().setTopLeftCorner(
                        new Vector2(
                                this.transform().getTopLeftCorner().x(),
                                other.getTopLeftCorner().y() - this.getDimensions().y()
                        )
                );

                // Treat the tree trunk as terrain
                this.touchingTerrain = true;
            } else {
                // Restrict movement into the tree trunk from the sides
                resolveSideCollision(collisionNormal);
            }
        }
    }

    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);

        if (other.getTag().equals(Terrain.GROUND) || other.getTag().equals(Tree.TREE_TRUNK)) {
            this.touchingTerrain = false;
        }
    }

    /**
     * Restricts the avatar's movement into the tree trunk during side collisions.
     */
    private void resolveSideCollision(Vector2 collisionNormal) {
        Vector2 velocity = this.transform().getVelocity();
        float dotProduct = velocity.dot(collisionNormal);

        // If the avatar is moving into the tree trunk, neutralize the movement in that direction
        if (dotProduct < 0) {
            this.transform().setVelocity(
                    velocity.subtract(collisionNormal.mult(dotProduct)).subtract(velocity)
            );
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

        idleAnimation = new AnimationRenderable(idleAnimations, 1F);
        runAnimation = new AnimationRenderable(runAnimations, 1F);
        jumpAnimation = new AnimationRenderable(jumpAnimations, 1F);

    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = MIN_ENERGY;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && !inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy >= RUN_ENERGY_LOSS) {
            handleRunning(true);
            xVel -= VELOCITY_X;
        } else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && !inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy >= RUN_ENERGY_LOSS) {
            handleRunning(false);
            xVel += VELOCITY_X;
        } else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && touchingTerrain && energy >= JUMP_ENERGY_LOSS && getVelocity().y() == MIN_ENERGY) {
            handleJumping();
        } else {
            handleIdle();
        }
        transform().setVelocityX(xVel);
        energyUpdateCallback.onEnergyUpdate((int) energy);
    }


    private void decreaseEnergy(float amount) {
        energy = Math.max(MIN_ENERGY, energy - amount);
    }

    private void increaseEnergy(float amount) {
        energy = Math.min(MAX_ENERGY, energy + amount);
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
        transform().setVelocityY(VELOCITY_Y * 0.5F);
        decreaseEnergy(JUMP_ENERGY_LOSS);
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

    private void updateAvatarRunImage() {
        renderer().setRenderable(runAnimation);
    }


    public void setEnergyUpdateCallback(EnergyUpdateCallback callback) {
        this.energyUpdateCallback = callback;
    }

    public void setRainCallback(Runnable rainCallback) {
        this.rainCallback = rainCallback;
    }


    public void setEnergy(float energy) {
        this.energy = energy;
    }
    public float getEnergy() {
        return energy;
    }

    public void setCloud(ArrayList<ArrayList<GameObject>> cloudBlocks) {
        this.clouds = cloudBlocks;
    }
}
