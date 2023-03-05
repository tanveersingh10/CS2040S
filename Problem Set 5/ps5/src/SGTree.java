/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */
import java.util.LinkedList;
import java.util.Queue;
public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;
        public int weight = 1;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
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
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
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

        if (root == null) {
            return new TreeNode[0];
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
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        return helper(nodeList, 0, nodeList.length - 1);
    }

    TreeNode helper(TreeNode[] nodeList, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = ((end - start) / 2) + start;
        TreeNode root = nodeList[mid];
        root.left = helper(nodeList, start, mid - 1);
        root.right = helper(nodeList, mid + 1, end);

        return root;
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {

        //checks for edge cases
        if (node == null) {
            return true;
        } else if (node.left == null && node.right == null) {
            return true;
        } else if (node.left == null) {
            if (node.right.weight <=2) {
                return true;
            } else {
                return false;
            }
        } else if (node.right == null) {
            if (node.left.weight <=2) {
                return true;
            } else {
                return false;
            }
        }

        if (node.left.weight > (2.0/3 * (node.weight))) {
            return false;
        } else if (node.right.weight > (2.0/3 * (node.weight))) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
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
        fixWeights(node, Child.LEFT);
        fixWeights(node, child.RIGHT);
    }

    public void fixWeights(TreeNode u, Child child) {

        if (child == Child.LEFT) {
            u = u.left;
        } else if (child == Child.RIGHT) {
            u = u.right;
        }

        if (u == null) {
            return;
        }

        u.weight = 1;
        if (u.left != null) {
            fixWeights(u, Child.LEFT);
            u.weight += u.left.weight;
        }
        if (u.right != null) {
            fixWeights(u, Child.RIGHT);
            u.weight += u.right.weight;
        }
    }

    /**
     * Inserts a key into the tree.
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
            node.weight++;
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

        TreeNode u = root;
        while (u != null ) {
            if (key <= u.key) {
                if (!checkBalance(u.left)) {
                    rebuild(u, Child.LEFT);
                    break;
                } else {
                    u = u.left;
                }
            } else {
                if (!checkBalance(u.right)) {
                    rebuild(u, Child.RIGHT);
                    break;
                } else {
                    u = u.right;
                }
            }
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
