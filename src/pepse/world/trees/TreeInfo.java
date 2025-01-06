package pepse.world.trees;

import danogl.GameObject;
import java.util.ArrayList;

/**
 * This class is an information saver. It concludes the TreeTrunk, leaves and
 * fruits objects of te Tree.
 */
public class TreeInfo {

    // Fields
    private final GameObject treeTrunk;
    private final ArrayList<GameObject> leaves;
    private final ArrayList<GameObject> fruits;

    /**
     * Constructor - Creates a Fruit object
     * @param treeTrunk - GameObject : The tree's trunk
     * @param leaves - ArrayList<GameObject> : The tree's array of leaves
     * @param fruits - ArrayList<GameObject> : The tree's array of fruits
     */
    public TreeInfo(GameObject treeTrunk, ArrayList<GameObject> leaves, ArrayList<GameObject> fruits) {

        this.treeTrunk = treeTrunk;
        this.leaves = leaves;
        this.fruits = fruits;

    }

    /**
     * Getter for the fruits array
     * @return ArrayList<GameObject> - Array of fruits
     */
    public ArrayList<GameObject> getFruits() {

        return fruits;

    }

    /**
     * Getter for the leaves array
     * @return ArrayList<GameObject> - Array of leaves
     */
    public ArrayList<GameObject> getLeaves() {

        return leaves;

    }

    /**
     * Getter for the tree's trunk
     * @return GameObject - The tree's trunk
     */
    public GameObject getTreeTrunk() {

        return treeTrunk;

    }
}
