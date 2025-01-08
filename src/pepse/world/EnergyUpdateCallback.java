package pepse.world;

/**
 * Functional interface for handling energy updates.
 * Classes implementing this interface should define the behavior for reacting to changes in the
 * player's energy level.
 */
@FunctionalInterface
public interface EnergyUpdateCallback {
    /**
     * Method to be called whenever the player's energy level is updated.
     *
     * @param currentEnergy The current energy level of the player.
     */
    void onEnergyUpdate(int currentEnergy);
}
