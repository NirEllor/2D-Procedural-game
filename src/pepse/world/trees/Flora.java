package pepse.world.trees;

import danogl.GameObject;
import pepse.world.Terrain;

import java.util.ArrayList;

public class Flora {

    private final Terrain terrain;

    public Flora(Terrain terrain){
        this.terrain = terrain;
    }
    public ArrayList<GameObject> createInRange(int minX, int maxX) {
        return Tree.plantTrees(this.terrain, minX, maxX);
    }

}
