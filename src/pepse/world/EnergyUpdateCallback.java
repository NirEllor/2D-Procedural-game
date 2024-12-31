package pepse.world;

@FunctionalInterface
public interface EnergyUpdateCallback {
    void onEnergyUpdate(int currentEnergy);
}
