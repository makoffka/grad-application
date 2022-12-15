
import java.io.*;
import java.util.*;

/**
 * Anastasia Kurakova CS211 12/1/22 Assignment 10 Huffman Coding Assignment
 *
 * Huffman node is a component that is used by the HuffmanTree to perform the
 * operations. HuffmanNode class contains isLeaf, getCounts and compareTo
 * methods.
 *
 */
public class HuffmanNode implements Comparable<HuffmanNode> {

    public int frequency;
    public char character;
    public HuffmanNode left;
    public HuffmanNode right;

// Boolean method isLeaf analyzes a HuffmanNode and determines if that HuffmanNode
// is a leafNode of the binary tree.
//     pre: HuffmanNode
//     post: returns true is the Huffman node is a leaf node of the binary tree in our Priority queue.
    public boolean isLeaf() {
        boolean leaf = false;
        //the node is a Leaf node if it does not contain any children.
        if (left == null && right == null) {
            leaf = true;
        }
        return leaf;

    }

// Method getCounts analyzes the file input and walks through each character creating a map that contains 
// character as a key and number of times it appears in the text as its value.
//    pre: text file input.
//    post: the method creates a map with characters from the text file as its keys and their counts as values.    
    public static Map<Character, Integer> getCounts(FileInputStream input) throws IOException {
        Map<Character, Integer> counts = new HashMap<>();
        int i;
        //method iterates through the string and counts how many times each character appears in the string.
        //it creates a map where each character is a key and stores the charCount as their values 
        while ((i = input.read()) != -1) {
            Character x = (char) i;
            int charCount = 0;
            if (counts.containsKey(x)) {
                charCount = counts.get(x) + 1;
            } else {
                charCount = 1;

            }
            counts.put(x, charCount);
        }
        //Special case for the end of function character (EOF-256 ASCII)
        counts.put((char) 256, 1);
        return counts;

    }

    @Override
// Method CompareTo is used by the Priority Queue for sorting
    public int compareTo(HuffmanNode node) {
        return Integer.compare(frequency, node.frequency);
    }
}
