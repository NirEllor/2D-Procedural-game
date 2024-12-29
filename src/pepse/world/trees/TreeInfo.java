package pepse.world.trees;

import danogl.GameObject;

import java.util.ArrayList;

public class TreeInfo {

    private final GameObject tree;
    private final ArrayList<GameObject> leaves;
    private final ArrayList<GameObject> fruits;

    public TreeInfo(GameObject tree, ArrayList<GameObject> leaves, ArrayList<GameObject> fruits) {
        this.tree = tree;
        this.leaves = leaves;
        this.fruits = fruits;
    }

    public ArrayList<GameObject> getFruits() {
        return fruits;
    }

    public ArrayList<GameObject> getLeaves() {
        return leaves;
    }

    public GameObject getTree() {
        return tree;
    }
}
