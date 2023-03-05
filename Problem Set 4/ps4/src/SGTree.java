/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */
import java.util.Arrays;

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        if (node == null) {
            return 0;
        }

        TreeNode root = null;

        if (child == Child.LEFT) {
            root = node.left;
        } else if (child == Child.RIGHT) {
            root = node.right;
        }

        if (root == null) {
            return 0;
        } else {
            return 1 + countNodes(root, Child.LEFT) + countNodes(root, Child.RIGHT);
        }
    }

    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {

        if (node == null || child == null) {
            return null;
        }

        TreeNode[] result = new TreeNode[countNodes(node, child)];

        TreeNode root = null;
        if (child == Child.LEFT) {
            root = node.left;
        } else if (child == Child.RIGHT){
            root = node.right;
        }

        //first do left side cos in order
        int leftNumOfNodes = countNodes(root, Child.LEFT);
        TreeNode[] leftArray = new TreeNode[leftNumOfNodes];
        if (root.left != null) {
            leftArray = enumerateNodes(root, Child.LEFT);
        }
        for (int i = 0; i < leftNumOfNodes; i++) {
            result[i] = leftArray[i];
        }
        
        //then this node
        result[leftNumOfNodes] = root;

        //then do right side
        int rightNumOfNodes = countNodes(root, Child.RIGHT);
        TreeNode[] rightArray = new TreeNode[rightNumOfNodes];

        if (root.right != null) {
            rightArray = enumerateNodes(root, Child.RIGHT);
        }
        for (int i = leftNumOfNodes + 1; i < countNodes(node, child); i++) {
            result[i] = rightArray[i - 1 - leftNumOfNodes];
        }

        return result;

    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        if (nodeList.length == 0) {
            return null;
        }
        int mid = nodeList.length /2;
        TreeNode root = nodeList[mid];
        TreeNode[] leftSubArray = Arrays.copyOfRange(nodeList, 0, mid);
        TreeNode[] rightSubArray = Arrays.copyOfRange(nodeList, mid + 1, nodeList.length);
        root.left = buildTree(leftSubArray);
        root.right = buildTree(rightSubArray);
        return root;
    }

    /**
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }


    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
    }
}
