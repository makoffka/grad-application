
import java.io.*;
import java.util.*;

/**
 * * Anastasia Kurakova CS211 12/1/22 Assignment 10 Huffman Coding Assignment
 *
 * Huffman tree is a class where the binary tree is constructed out of
 * HuffmanNodes using priority queue. This program helps to compress text files
 * to occupy smaller amount of space. This class contains methods traversal,
 * printSideways, compress, and decompress.
 *
 */
public class HuffmanTree {

    private PriorityQueue<HuffmanNode> pq = new PriorityQueue<HuffmanNode>();
    private Map<Character, String> encodingMap = new HashMap<>();

    public HuffmanTree(Map<Character, Integer> counts) {
// Creating priority queue that will store HuffmanNodes in sorted order 
// pre: Map containing characters and their counts.
// post: Priority queue consisting of HuffmanNodes with characters with smaller 
// counts in front of the queue
        for (Map.Entry<Character, Integer> element : counts.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.character = element.getKey();
            node.frequency = element.getValue();
            pq.add(node);

        }
        
        //System.out.println(pq.size());
        
// Building a binary tree based on the priority queue. With each step two 
// of the HuffmanNodes from the front of the pq are removed. They are joined into
// a new node whise frequency is their sum.
// The new node is added to back to the pq

        while (pq.size() > 1) {
            HuffmanNode leftNode = pq.remove();
            HuffmanNode rightNode = pq.remove();
            HuffmanNode newNode = new HuffmanNode();
            newNode.frequency = leftNode.frequency + rightNode.frequency;
            newNode.left = leftNode;
            newNode.right = rightNode;
            pq.add(newNode);
            //    System.out.println(pq.size());

        }
        encodingMap = traversal();

    }
    
// Traversal Method. Recursive method that performs an in-order traversal through the binary tree.

    // pre: HuffmanNode
    // post: The method performs in-order traversal through the binary tree and creates a path for each leafNode.
    // The path is a binary representation of each character that we will store in the encodingMap
    public Map<Character, String> traversal() {
        Map<Character, String> result = new HashMap<>();
        traversal(pq.element(), result, "");
        return result;
    }

    private void traversal(HuffmanNode rootNode, Map<Character, String> mapping, String path) {

        if (rootNode != null) {
            traversal(rootNode.left, mapping, path + "0");
            if (rootNode.isLeaf()) {
                mapping.put(rootNode.character, path);
            }
            traversal(rootNode.right, mapping, path + "1");

        }
    }

//  Recursive method printSideways that helps to represent our data as a binary tree printed sideways.
//    pre: HuffmanNode
//    post: printSideways will produce string representation of the binary tree that we created printed sideways.  
    public String printSideways() {
        StringBuilder treeImage = new StringBuilder();
        printSideways(pq.element(), treeImage, 0);
        return treeImage.toString();

    }

    private void printSideways(HuffmanNode root, StringBuilder tree, int level) {
        if (root != null) {
            //Using System.lineSeparator() to create a new line in StringBuilder.
            printSideways(root.right, tree.append(System.lineSeparator()), level + 1);
            for (int i = 0; i < level; i++) {
                tree.append("    ");
            }
            tree.append(root.frequency + "=");
            if (root.isLeaf()) {
                tree.append("char(" + (int) root.character + ")"); //adding ASCII character values to each leaf node
            } else {
                tree.append("count");
            }
            printSideways(root.left, tree.append(System.lineSeparator()), level + 1);

        }

    }
    
// Method that creates a binary string of the textfile.
    //pre: InputStream file
    //post: encodedString of StringBuilder data type that is created using encodingMap.

    public StringBuilder compress(InputStream inputFile) throws IOException {
        int i;
        StringBuilder encodedString = new StringBuilder();
        while ((i = inputFile.read()) != -1) {
            Character x = (char) i;
            encodedString.append(encodingMap.get(x));

        }
        encodedString.append(encodingMap.get((char) 256));
        return encodedString;

    }
    
// Method that decodes the binary string by traversing the HuffmanTree. 
    // pre: takes in a binary string 
    // post: recreates initial text file by decoding the binary string. the decompress method 
    // traverses the HuffmanTree by following the path and every time it hits a Leaf Node it gets its 
    // character value  and resets rootNode to the overall root

    public StringBuilder decompress(StringBuilder inputString) {
        StringBuilder decodedString = new StringBuilder();
        HuffmanNode rootNode = pq.element();
        //for loop that iterates through each character of the string. 
        for (int i = 0; i < inputString.length(); i++) {
            char x = inputString.charAt(i);
            if (x == '1') {
                rootNode = rootNode.right;
            } else {
                rootNode = rootNode.left;
            }

            // If the node is a Leaf Node we get its character value
            if (rootNode.isLeaf()) {
                char decodedChar = rootNode.character;
                if (decodedChar != (char) 256) {
                    decodedString.append(rootNode.character);
                }
                // Resetting a rootNode to the overall root        
                rootNode = pq.element();

            }
        }

        return decodedString;
    }

}
