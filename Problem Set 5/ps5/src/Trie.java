import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    TrieNode root;

    private class TrieNode {
        // TODO: Create your TrieNode class here.
        int[] presentChars = new int[62];
        TrieNode[] children = new TrieNode[62];
        char value;
        boolean isEndOfWord;
        public TrieNode() {
            for (int i = 0; i < 62; i++) {
                presentChars[i] = 0;
                children[i] = null;
            }
        }


    }

    public Trie() {
        // TODO: Initialise a trie class here.
        this.root = new TrieNode();
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        TrieNode current = this.root;
        for (int i = 0; i < s.length(); i++) {
            int index = findIndex(s.charAt(i));
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
                current.children[index].value = s.charAt(i);
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }

    int findIndex(char c) {
        int ascii = (int) c;
        int index;
        if (ascii >= 48 && ascii <= 57) {
            index = ascii - 48;
        } else if (ascii >= 65 && ascii <= 90) {
            index = ascii - 65 + 10;
        } else {
            index = ascii - 97 + 10 + 26;
        }
        return index;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        TrieNode current = this.root;
        for (int i = 0; i < s.length(); i++) {
            int index = findIndex(s.charAt(i));
            TrieNode child = current.children[index];
            if (child == null) {
                return false;
            }
            System.out.println("found character " + s.charAt(i));
            current = current.children[index];
        }
        return current.isEndOfWord;
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        helper(s, limit, 0, "", root, results);
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }

    void helper(String s, int limit, int i, String word, TrieNode current, ArrayList<String> results) {
        if (results.size() == limit) {
            return;
        }

        if (i >= s.length()) {
            if (current.isEndOfWord) {
                results.add(word);
                for (int j = 0; j < 62; j++) {
                    TrieNode nextNode = current.children[j];
                    if (current.children[j] != null) {
                        char nextCharacter = nextNode.value;
                        String updatedWord = word + nextCharacter;
                        helper(s, limit, i + 1, updatedWord, nextNode, results);
                    }
                }

            } else {
                for (int j = 0; j < 62; j++) {
                    TrieNode nextNode = current.children[j];
                    if (nextNode != null) {
                        char nextCharacter = nextNode.value;
                        String updatedWord = word + nextCharacter;
                        helper(s, limit, i + 1, updatedWord, nextNode, results);
                    }
                }
            }
        } else {
            if (s.charAt(i) == WILDCARD) {
                for (int j = 0; j < 62; j++) {
                    TrieNode nextNode = current.children[j];
                    if (nextNode != null) {
                        char nextCharacter = nextNode.value;
                        String updatedWord = word + nextCharacter;
                        helper(s, limit, i + 1, updatedWord, current.children[j], results);
                    }
                }
            } else {
                int index = findIndex(s.charAt(i));
                TrieNode nextNode = current.children[index];
                if (nextNode != null) {
                    char nextCharacter = nextNode.value;
                    String updatedWord = word + nextCharacter;
                    helper(s, limit, i + 1, updatedWord, current.children[index], results);
                }
            }
        }
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);

        System.out.println(result1[4]);
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
