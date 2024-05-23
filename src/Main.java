package src;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {
    public static Map<Character, Integer> occurrences = new HashMap<>();

    public static final String ENCIN = "resources/in.txt";
    public static final String ENCHUF = "resources/dec_tab.txt";
    public static final String ENCOUT = "resources/output.dat";
    public static final String DECIN = "resources/output-mada.dat";
    public static final String DECHUF = "resources/dec_tab-mada.txt";
    public static final String DECOUT = "resources/decompress.txt";
    
    public static void main(String[] args) {
        Encrypt();
        Decrypt();
    }
    
    public static void Encrypt(){
        System.out.println("--- Encryption ---");
        
        // fill map with 0s
        for(int i = 0; i < 128; i++){
            occurrences.put((char)i, 0);
        }

        // read file and count occurences
        var text = IOUtility.readFile(ENCIN);
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
        IOUtility.writeLines(ENCHUF, huffmanCodes.entrySet().stream().map(s -> (int)s.getKey().charValue() + ":" + s.getValue()).toArray(String[]::new), "-");

        // encode text to bitstring
        StringBuilder bitString = new StringBuilder();
        for (char c : text.toCharArray()) {
            bitString.append(huffmanCodes.get(c));
        }

        // extend bitstring with 1 and 0s to be a multiple of 8
        bitString.append('1');
        while (bitString.length() % 8 != 0) {
            bitString.append('0');
        }
        System.out.println("Bitstring length: " + bitString.length());
        System.out.println("Bitstring: " + bitString);

        // generate byte array
        byte[] bytes = new byte[bitString.length() / 8];
        for (int i = 0; i < bitString.length(); i += 8) {
            bytes[i / 8] = (byte) Integer.parseInt(bitString.substring(i, i + 8), 2);
        }

        // write byte array to file
        IOUtility.writeBytes(ENCOUT, bytes);
    }
    
    public static void Decrypt(){
        System.out.println("--- Decryption ---");
        
        // read files
        var bytes = IOUtility.readBytes(DECIN);
        var huffmanTableContent = IOUtility.readFile(DECHUF);
        var huffmanCodes = new HashMap<String, Character>();
        
        // fill huffman table (reverse map for lookup)
        for (var line : huffmanTableContent.split("-")) {
            var parts = line.split(":");
            var character = (char) Integer.parseInt(parts[0]);
            var code = parts[1];
            huffmanCodes.put(code, character);            
        }
        
        // convert byte array to bitstring
        var bitString = new StringBuilder();
        for (var b : bytes) {
            bitString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        System.out.println("Bitstring length: " + bitString.length());
        System.out.println("Bitstring: " + bitString);
        
        // cleanup bitstring (remove last 1 and trailing 0s)
        while(bitString.charAt(bitString.length() - 1) == '0'){
            bitString.deleteCharAt(bitString.length() - 1);
        }
        bitString.deleteCharAt(bitString.length() - 1);
        System.out.println("Bitstring clean: " + bitString);
        
        // decode bitstring
        var decodedText = new StringBuilder();
        var currentCode = new StringBuilder();
        for (var c : bitString.toString().toCharArray()) {
            currentCode.append(c);
            if (huffmanCodes.containsKey(currentCode.toString())) {
                decodedText.append(huffmanCodes.get(currentCode.toString()));
                currentCode = new StringBuilder();
            }
        }
        System.out.println("Decoded text: " + decodedText);
        IOUtility.writeLines(DECOUT, new String[]{decodedText.toString()}, "");
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
