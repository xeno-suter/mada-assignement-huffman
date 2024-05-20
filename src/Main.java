package src;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static Map<Integer, Integer> occurences = new HashMap<>();

    public static void main(String[] args) {
        // fill map with 0s
        for(int i = 0; i < 128; i++){
            occurences.put(i, 0);
        }

        // read file and count occurences
        var text = IOUtility.readFile("resources/in.txt");
        for (var c : text.toCharArray()){
            occurences.put((int)c, occurences.get((int)c) + 1);
        }

        // print occurences
        for (var entry : occurences.entrySet()){
            System.out.println((char)(int)entry.getKey() + " " + entry.getValue());
        }
    }
}
