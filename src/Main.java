package src;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {
    public static Map<Character, Integer> occurrences = new HashMap<>();

    public static void main(String[] args) {
        // fill map with 0s
        for(int i = 0; i < 128; i++){
            occurrences.put((char)i, 0);
        }

        // read file and count occurences
        var text = IOUtility.readFile("resources/in.txt");
        for (var c : text.toCharArray()){
            occurrences.put(c, occurrences.get(c) + 1);
        }

        // print occurences
        for (var entry : occurrences.entrySet()){
            System.out.println((char)(int)entry.getKey() + " " + entry.getValue());
        }

        // fill priority queue with nodes
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (var entry : occurrences.entrySet()) {
            if (entry.getValue() > 0) {
                priorityQueue.add(new HuffmanNode(entry.getValue(), entry.getKey(), null, null));
            }
        }

        // build huffman tree
        while (priorityQueue.size() > 1) {
            var left = priorityQueue.poll();
            var right = priorityQueue.poll();
            var parent = new HuffmanNode(left.frequency + right.frequency, null, left, right);
            priorityQueue.add(parent);
        }

        // generate huffman Codes
        var root = priorityQueue.poll();
        printTree(root, "", true);
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);

        // print huffman Codes
        System.out.println("Huffman Codes:");
        for (var entry : huffmanCodes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        // write huffman table
        IOUtility.writeLines("resources/dec_tab.txt", huffmanCodes.entrySet().stream().map(s -> s.getKey() + ":" + s.getValue()).toArray(String[]::new));
    }

    private static void generateCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.character, code);
            return;
        }

        if (node.left != null) {
            generateCodes(node.left, code + "0", huffmanCodes);
        }

        if (node.right != null) {
            generateCodes(node.right, code + "1", huffmanCodes);
        }
    }

    private static void printTree(HuffmanNode node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.println(prefix + (isLeft ? "|-- " : "\\-- ") + (node.character != null ? node.character : "") + " (" + node.frequency + ")");
            printTree(node.left, prefix + (isLeft ? "|   " : "    "), true);
            printTree(node.right, prefix + (isLeft ? "|   " : "    "), false);
        }
    }
}
